package com.kayak.k8s.beans;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kayak.k8s.properties.KubModel;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

@Configuration
public class KubernetesKayakClient {

	@Resource
	private KubModel kubmodel;

	@Bean
	public KubernetesClient getKubernetesClient() {
		 Config config = new ConfigBuilder().withMasterUrl(kubmodel.getMaster()).withOauthToken(kubmodel.getToken()).withNamespace(kubmodel.getNamespace()).build();
		 return new DefaultKubernetesClient(config);
	}

}
