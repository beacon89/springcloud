package com.kayak.k8s.model;

import io.fabric8.kubernetes.api.model.ReplicationController;

public class KubRcShowModel {
	
	private boolean properties;
	
	private KubRCSpecModel spec;
	
	private KubRCMetadataModel metadata;
	
	private KubRcStatusModel status;
	
	public KubRcShowModel(ReplicationController replicationcontroller) {
		if(replicationcontroller.getSpec() != null) {
			this.spec = new KubRCSpecModel(replicationcontroller);
		}
		if(replicationcontroller.getMetadata() != null) {
			this.metadata = new KubRCMetadataModel(replicationcontroller);
		}
		if(replicationcontroller.getStatus() != null) {
			this.status = new KubRcStatusModel(replicationcontroller);
		}
		if(!replicationcontroller.getAdditionalProperties().isEmpty()) {
			this.properties = true;
		}
	}

	public boolean isProperties() {
		return properties;
	}

	public void setProperties(boolean properties) {
		this.properties = properties;
	}

	public KubRCSpecModel getSpec() {
		return spec;
	}

	public void setSpec(KubRCSpecModel spec) {
		this.spec = spec;
	}

	public KubRCMetadataModel getMetadata() {
		return metadata;
	}

	public void setMetadata(KubRCMetadataModel metadata) {
		this.metadata = metadata;
	}

	public KubRcStatusModel getStatus() {
		return status;
	}

	public void setStatus(KubRcStatusModel status) {
		this.status = status;
	}
	
	
	
}
