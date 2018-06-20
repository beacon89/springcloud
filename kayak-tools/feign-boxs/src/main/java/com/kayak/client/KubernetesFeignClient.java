package com.kayak.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kayak.remote.KubernetesHystrix;


@FeignClient(name = "kayak-kubernetes",fallback=KubernetesHystrix.class)
public interface KubernetesFeignClient {

	@RequestMapping(value="createReplicationController")
	public Map<String, Object> createReplicationController(@RequestParam("json") final String json);
	
	@RequestMapping(value="deleteReplicationController")
	public Map<String, Object> deleteReplicationController(@RequestParam("json") final String json);
	
	@RequestMapping(value="getReplicationControllerLists")
	public Map<String, Object> getReplicationControllerLists(@RequestParam("pageNumber") final int pageNumber,@RequestParam("pageSize") final int pageSize);
}
