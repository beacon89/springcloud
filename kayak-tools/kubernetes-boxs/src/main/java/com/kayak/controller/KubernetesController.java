package com.kayak.controller;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kayak.beans.KayakReplicationControllerList;
import com.kayak.utils.KubernetesUtils;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

@RestController
public class KubernetesController {

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
	public ReplicationController createReplicationController(String str) {
		ReplicationController rc = KubernetesUtils.getReplicationControllerBySteam(this.kubernetesClient, str);
		if(rc != null) {
			return KubernetesUtils.createReplicationController(rc, this.kubernetesClient);
		}
		throw new KubernetesClientException("创建失败");
	}
	
	@RequestMapping(value="deleteReplicationController")
	public Boolean deleteReplicationController(String str) {
		System.out.println(str);
		ReplicationController rc = KubernetesUtils.getReplicationControllerBySteam(this.kubernetesClient, str);
		if(rc != null) {
			return KubernetesUtils.deleteReplicationController(rc, this.kubernetesClient);
		}
		throw new KubernetesClientException("删除失败");
	}
	
	@RequestMapping(value="getReplicationControllerLists")
	public KayakReplicationControllerList getReplicationControllerLists(int pageNumber,int pageSize) {
		try {
			return KubernetesUtils.getReplicationControllerLists(pageNumber,pageSize, this.kubernetesClient);
		}catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping(value="getPodLists")
	public List<Pod> getPodLists() {
		return KubernetesUtils.getPodLists(this.kubernetesClient).getItems();
	}
	
}
