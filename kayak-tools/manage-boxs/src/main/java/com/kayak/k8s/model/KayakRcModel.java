package com.kayak.k8s.model;

import io.fabric8.kubernetes.api.model.ReplicationController;

public class KayakRcModel {

	private String apiVersion;

	private String kind;

	private String creationTimestamp;

	private String name;

	private String namespace;

	private Integer replicas;

	private String ports;

	private Long generation;

	private ReplicationController replicationcontroller;
	
	private KubRcShowModel modelflag;

	public KubRcShowModel getModelflag() {
		return modelflag;
	}

	public void setModelflag(KubRcShowModel modelflag) {
		this.modelflag = modelflag;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(String creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

	public ReplicationController getReplicationcontroller() {
		return replicationcontroller;
	}

	public void setReplicationcontroller(ReplicationController replicationcontroller) {
		this.replicationcontroller = replicationcontroller;
	}

	public Long getGeneration() {
		return generation;
	}

	public void setGeneration(Long generation) {
		this.generation = generation;
	}

	public Integer getReplicas() {
		return replicas;
	}

	public void setReplicas(Integer replicas) {
		this.replicas = replicas;
	}

}
