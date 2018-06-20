package com.kayak.k8s.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kayak.base.action.BaseController;
import com.kayak.k8s.beans.KayakReplicationControllerList;
import com.kayak.util.KubernetesUtils;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.client.KubernetesClient;

@RestController
public class KubernetesController extends BaseController {

	@Resource
	private KubernetesClient kubernetesClient;

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
	public String createReplicationController(@PathVariable("json") String json) {
		try {
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
	public String deleteReplicationController(@PathVariable("json") String json) {
		try {
			ReplicationController rc = KubernetesUtils.getReplicationControllerBySteam(this.kubernetesClient,json);
			if (KubernetesUtils.deleteReplicationController(rc, this.kubernetesClient)) {
				return super.updateSuccess("创建成功!");
			} else {
				return super.updateSuccess("无法找到创建的yml");
			}
		} catch (Exception e) {
			return super.updateFailure(e.getMessage());
		}
	}

	@RequestMapping(value = "/k8s/getReplicationControllerLists.json")
	public String getReplicationControllerLists(@PathVariable("pageNumber") int pageNumber, @PathVariable("pageSize") int pageSize) {
		try {
			KayakReplicationControllerList rc = KubernetesUtils.getReplicationControllerLists(pageNumber, pageSize,this.kubernetesClient);
			Map<String, Object> map = new HashMap<>();
			map.put("rows", rc.getItems());
			map.put("pageNumber", rc.getPageNumber());
			map.put("pageSize", rc.getPageSize());
			map.put("totalCount", rc.getTotalCount());
			return super.updateSuccess(map);
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
