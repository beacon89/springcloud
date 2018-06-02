package com.kayak.controller;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.kayak.beans.KayakReplicationControllerList;
import com.kayak.utils.KubUtils;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;

@RestController
public class KubController {

	@Resource
	private KubernetesClient kubernetesClient;
	/**
	 * 创建pod
	 * @param pod (object)
	 * @return Pod
	 */
	@CrossOrigin
	@RequestMapping(value="createPod")
	public Pod createPod(Pod pod,MultipartFile file) {
		return KubUtils.createPod(pod, this.kubernetesClient);
	}
	
	@CrossOrigin
	@RequestMapping(value="deletePod")
	public Boolean deletePod(Pod pod) {
		return KubUtils.deletePod(pod, this.kubernetesClient);
	}
	
	@CrossOrigin
	@RequestMapping(value="createReplicationController")
	public ReplicationController createReplicationController(String str) {
		JSONObject json = JSON.parseObject(str);
		System.out.println(json.toJSONString());
		
		
		ReplicationController rc = KubUtils.getReplicationControllerBySteam(this.kubernetesClient, str);
		if(rc != null) {
			return KubUtils.createReplicationController(rc, this.kubernetesClient);
		}
		throw new KubernetesClientException("创建失败");
	}
	
	@CrossOrigin
	@RequestMapping(value="deleteReplicationController")
	public Boolean deleteReplicationController(String str) {
		System.out.println(str);
		ReplicationController rc = KubUtils.getReplicationControllerBySteam(this.kubernetesClient, str);
		if(rc != null) {
			return KubUtils.deleteReplicationController(rc, this.kubernetesClient);
		}
		throw new KubernetesClientException("删除失败");
	}
	
	@CrossOrigin
	@RequestMapping(value="getReplicationControllerLists")
	public KayakReplicationControllerList getReplicationControllerLists(int pageNumber,int pageSize) {
		try {
			return KubUtils.getReplicationControllerLists(pageNumber,pageSize, this.kubernetesClient);
		}catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}
	
	@CrossOrigin
	@RequestMapping(value="getPodLists")
	public List<Pod> getPodLists() {
		return KubUtils.getPodLists(this.kubernetesClient).getItems();
	}
	
}
