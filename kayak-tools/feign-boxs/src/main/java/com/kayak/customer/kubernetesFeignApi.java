package com.kayak.customer;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.client.KubernetesFeignClient;


@RestController
public class kubernetesFeignApi {

	@Resource
	private KubernetesFeignClient kubernetesFeignClient;
	
	@RequestMapping(value="createReplicationController")
	public Map<String, Object> createReplicationController(@RequestParam("json") final String json){
		return this.kubernetesFeignClient.createReplicationController(json);
	}
	
	@RequestMapping(value="deleteReplicationController")
	public Map<String, Object> deleteReplicationController(@RequestParam("json") final String json){
		return this.kubernetesFeignClient.deleteReplicationController(json);
	}
	
	@RequestMapping(value="getReplicationControllerLists")
	public Map<String, Object> getReplicationControllerLists(@RequestParam("pageNumber") final int pageNumber,@RequestParam("pageSize") final int pageSize){
		return this.kubernetesFeignClient.getReplicationControllerLists(pageNumber, pageSize);
	}
	
}
