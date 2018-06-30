package com.kayak.k8s.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DeleteDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse.Record;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kayak.base.action.BaseController;
import com.kayak.base.system.RequestSupport;
import com.kayak.base.util.Tools;
import com.kayak.k8s.beans.KayakReplicationControllerList;
import com.kayak.util.KubernetesUtils;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.client.KubernetesClient;

@RestController
public class KubernetesController extends BaseController {

	@Resource
	private KubernetesClient kubernetesClient;
	
	@Autowired
	private DescribeDomainRecordsRequest describeDomainRecords;

	@Autowired
	private IAcsClient iacsClient;
	
	@Autowired
	private DeleteDomainRecordRequest deleteDomainRecord;
	/**
	 * 创建pod
	 * 
	 * @param pod
	 *            (object)
	 * @return Pod
	 */
	@RequestMapping(value = "createPod")
	public Pod createPod(Pod pod, MultipartFile file) {
		return KubernetesUtils.createPod(pod, this.kubernetesClient);
	}

	@RequestMapping(value = "deletePod")
	public Boolean deletePod(Pod pod) {
		return KubernetesUtils.deletePod(pod, this.kubernetesClient);
	}

	@RequestMapping(value = "/k8s/createReplicationController.json")
	public String createReplicationController() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			String jsons = Tools.obj2Str(parmas.get("k8scode"));
			ReplicationController rc = KubernetesUtils.getReplicationControllerByJson(jsons);
			KubernetesUtils.createReplicationController(rc, this.kubernetesClient);
			return super.updateSuccess("创建成功!");
		} catch (Exception e) {
			return super.updateFailure(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/k8s/createReplicationControlleredit.json")
	public String createReplicationControlleredit() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			String json = Tools.obj2Str(parmas.get("k8scode"));
			ReplicationController rc = KubernetesUtils.getReplicationControllerBySteam(this.kubernetesClient,json);
			if (rc == null) {
				return super.updateSuccess("无法将数据变成yml文件!");
			}
			KubernetesUtils.createReplicationController(rc, this.kubernetesClient);
			return super.updateSuccess("创建成功!");
		} catch (Exception e) {
			return super.updateFailure(e.getMessage());
		}
	}

	@RequestMapping(value = "/k8s/deleteReplicationController.json")
	public String deleteReplicationController() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			ReplicationController rc = KubernetesUtils.getReplicationControllerBySteam(this.kubernetesClient,Tools.obj2Str(parmas.get("k8scode")));
			this.describeDomainRecords.setRRKeyWord(rc.getSpec().getTemplate().getSpec().getHostname() + "." + rc.getSpec().getTemplate().getSpec().getSubdomain());
			DescribeDomainRecordsResponse response = this.iacsClient.getAcsResponse(this.describeDomainRecords);
			List<Record> list = response.getDomainRecords();
			if(list.isEmpty()) {
				throw new Exception("未找有关于该RC的域名,请先检查域名:"+rc.getSpec().getTemplate().getSpec().getHostname() + "." + rc.getSpec().getTemplate().getSpec().getSubdomain());
			}
			if(list.size() > 1) {
				throw new Exception("RC有多个域名,请先检查域名:"+rc.getSpec().getTemplate().getSpec().getHostname() + "." + rc.getSpec().getTemplate().getSpec().getSubdomain());
			}
			Record record = list.get(0);
			if (KubernetesUtils.deleteReplicationController(rc, this.kubernetesClient)) {
				this.deleteDomainRecord.setRecordId(record.getRecordId());
				this.iacsClient.getAcsResponse(this.deleteDomainRecord);
				return super.updateSuccess("删除成功!");
			} else {
				return super.updateSuccess("无法找到创建的yml");
			}
		} catch (Exception e) {
			return super.updateFailure(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/k8s/deleteReplicationControlleredit.json")
	public String deleteReplicationControlleredit() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			ReplicationController rc = KubernetesUtils.getReplicationControllerBySteam(this.kubernetesClient,Tools.obj2Str(parmas.get("k8scode")));
			if (KubernetesUtils.deleteReplicationController(rc, this.kubernetesClient)) {
				return super.updateSuccess("删除成功!");
			} else {
				return super.updateSuccess("无法找到创建的yml");
			}
		} catch (Exception e) {
			return super.updateFailure(e.getMessage());
		}
	}

	@RequestMapping(value = "/k8s/getReplicationControllerLists.json")
	public String getReplicationControllerLists() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			int pageNumber = Tools.str2Int(Tools.obj2Str(parmas.get("pageNumber")));
			int pageSize = Tools.str2Int(Tools.obj2Str(parmas.get("pageSize")));
			KayakReplicationControllerList rc = KubernetesUtils.getReplicationControllerLists(pageNumber, pageSize,this.kubernetesClient);
			Map<String, Object> map = new HashMap<>();
			map.put("rows", rc.getItems());
			map.put("pageNumber", rc.getPageNumber());
			map.put("pageSize", rc.getPageSize());
			map.put("totalCount", rc.getTotalCount());
			return super.updateSuccess(map);
		}catch (JsonProcessingException e) {
			return super.updateFailure("无法格式化为JSON对象"+e.getMessage());
		} catch (Exception e) {
			return super.updateFailure(e.getMessage());
		}
	}

	@RequestMapping(value = "getPodLists")
	public String getPodLists() {
		try {
			List<Pod> pod = KubernetesUtils.getPodLists(this.kubernetesClient).getItems();
			Map<String, Object> map = new HashMap<>();
			map.put("rows", pod);
			map.put("totalCount", pod.size());
			return super.updateSuccess(map);
		}catch (Exception e) {
			return super.updateFailure(e.getMessage());
		}
	}

}
