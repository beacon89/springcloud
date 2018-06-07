package com.kayak.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kayak.remote.KubernetesHystrix;



@FeignClient(name = "kayak-kubernetes",fallback=KubernetesHystrix.class)
public class KubernetesFeignClient {

}
