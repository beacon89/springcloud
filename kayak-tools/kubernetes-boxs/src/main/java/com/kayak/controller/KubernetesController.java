package com.kayak.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kayak.beans.KayakReplicationControllerList;
import com.kayak.utils.KubernetesUtils;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.client.KubernetesClient;

@RestController
public class KubernetesController extends BaseController{

	@Resource
	private KubernetesClient kubernetesClient;
	/**
	 * 创建pod
	 * @param pod (object)
	 * @return Pod
	 */
	@RequestMapping(value="createPod")
	public Pod createPod(Pod pod,MultipartFile file) {
		return KubernetesUtils.createPod(pod, this.kubernetesClient);
	}
	
	@RequestMapping(value="deletePod")
	public Boolean deletePod(Pod pod) {
		return KubernetesUtils.deletePod(pod, this.kubernetesClient);
	}
	
	@RequestMapping(value="createReplicationController")
	public Map<String, Object> createReplicationController(String json) {
		ReplicationController rc = KubernetesUtils.getReplicationControllerBySteam(this.kubernetesClient, json);
		if(rc != null) {
			try {
				Map<String, Object> map = new HashMap<>();
				KubernetesUtils.createReplicationController(rc, this.kubernetesClient);
				return this.responseMessage(map);
			}catch (Exception e) {
				return this.responseMessage(TransactionCode.K8S9999.getCode(),e.getMessage());
			}
		}else {
			return this.responseMessage(TransactionCode.K8S9998.getCode(),TransactionCode.K8S9998.getMessage());
		}
	}
	
	@RequestMapping(value="deleteReplicationController")
	public Map<String, Object> deleteReplicationController(String json) {
		ReplicationController rc = KubernetesUtils.getReplicationControllerBySteam(this.kubernetesClient, json);
		if(rc != null) {
			try {
				if(KubernetesUtils.deleteReplicationController(rc, this.kubernetesClient)) {
					Map<String, Object> map = new HashMap<>();
					return this.responseMessage(map);
				}else {
					return this.responseMessage(TransactionCode.K8S9997.getCode(),TransactionCode.K8S9997.getMessage());
				}
			}catch (Exception e) {
				return this.responseMessage(TransactionCode.K8S9999.getCode(),e.getMessage());
			}
		}else {
			return this.responseMessage(TransactionCode.K8S9998.getCode(),TransactionCode.K8S9998.getMessage());
		}
	}
	
	@RequestMapping(value="getReplicationControllerLists")
	public Map<String, Object> getReplicationControllerLists(int pageNumber,int pageSize) {
		try {
			KayakReplicationControllerList rc =  KubernetesUtils.getReplicationControllerLists(pageNumber,pageSize, this.kubernetesClient);
			return this.responseMessage(rc.getItems(),rc.getPageNumber(),rc.getPageSize(),rc.getTotalCount(),null);
		}catch (Exception e) {
			return this.responseMessage(TransactionCode.K8S9999.getCode(),e.getMessage());
		}
	}
	
	@RequestMapping(value="getPodLists")
	public List<Pod> getPodLists() {
		return KubernetesUtils.getPodLists(this.kubernetesClient).getItems();
	}
	
}
