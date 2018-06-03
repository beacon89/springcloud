package com.kayak.beans;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.kayak.model.KubModel;

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
		// Config config = new ConfigBuilder().build();
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

	@Bean
	public HttpMessageConverters fastJsonConverters() {
		FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastConf = new FastJsonConfig();

		fastConf.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastJsonConverter.setFastJsonConfig(fastConf);

		HttpMessageConverter<?> converter = fastJsonConverter;
		return new HttpMessageConverters(converter);
	}
}
