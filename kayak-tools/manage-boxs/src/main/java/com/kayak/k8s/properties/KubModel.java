package com.kayak.k8s.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "kubernetes")
public class KubModel {
	
	@Value("${kubernetes.namespace}")
	private String namespace;
	
	@Value("${kubernetes.master}")
	private String master;
	
	@Value("${kubernetes.connectiontimeout}")
	private int connectiontimeout;
	
	@Value("${kubernetes.requesttimeout}")
	private int requesttimeout;

	@Value("${kubernetes.username}")
	private String username;

	@Value("${kubernetes.password}")
	private String password;
	
	@Value("${kubernetes.token}")
	private String token;
	
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public int getConnectiontimeout() {
		return connectiontimeout;
	}

	public void setConnectiontimeout(int connectiontimeout) {
		this.connectiontimeout = connectiontimeout;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRequesttimeout() {
		return requesttimeout;
	}

	public void setRequesttimeout(int requesttimeout) {
		this.requesttimeout = requesttimeout;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	

}
