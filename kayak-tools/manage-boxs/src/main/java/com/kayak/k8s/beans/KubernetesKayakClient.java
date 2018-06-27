package com.kayak.k8s.beans;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kayak.k8s.properties.KubModel;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import okhttp3.TlsVersion;

@Configuration
public class KubernetesKayakClient {

	@Resource
	private KubModel kubmodel;

	@Bean
	public KubernetesClient getKubernetesClient() {
		Config config = null;
		if (kubmodel.getMaster().toUpperCase().startsWith("https")) {
			config = new ConfigBuilder().withMasterUrl(kubmodel.getMaster()).withTrustCerts(true)
					.withNamespace(kubmodel.getNamespace()).removeFromTlsVersions(TlsVersion.TLS_1_0).withOauthToken(kubmodel.getToken())
					.removeFromTlsVersions(TlsVersion.TLS_1_1).removeFromTlsVersions(TlsVersion.TLS_1_2)
					.withRequestTimeout(kubmodel.getRequesttimeout())
					.withConnectionTimeout(kubmodel.getConnectiontimeout()).build();
		} else {
			config = new ConfigBuilder().withMasterUrl(kubmodel.getMaster()).withTrustCerts(true)
					.withNamespace(kubmodel.getNamespace()).removeFromTlsVersions(TlsVersion.TLS_1_0)
					.removeFromTlsVersions(TlsVersion.TLS_1_1).removeFromTlsVersions(TlsVersion.TLS_1_2)
					.withRequestTimeout(kubmodel.getRequesttimeout())
					.withConnectionTimeout(kubmodel.getConnectiontimeout()).build();
		}
		return new DefaultKubernetesClient(config);
	}

}
