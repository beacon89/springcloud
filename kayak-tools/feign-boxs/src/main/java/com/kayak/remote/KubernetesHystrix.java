package com.kayak.remote;

import java.util.Map;

import org.springframework.context.annotation.Configuration;

import com.kayak.client.KubernetesFeignClient;
import com.kayak.controller.BeseHystrix;

@Configuration
public class KubernetesHystrix extends BeseHystrix implements KubernetesFeignClient{

	@Override
	public Map<String, Object> createReplicationController(String json) {
		return this.returnSystemfail("kayak-kubernetes");
	}

	@Override
	public Map<String, Object> deleteReplicationController(String json) {
		return this.returnSystemfail("kayak-kubernetes");
	}

	@Override
	public Map<String, Object> getReplicationControllerLists(int pageNumber, int pageSize) {
		return this.returnSystemfail("kayak-kubernetes");
	}

}
