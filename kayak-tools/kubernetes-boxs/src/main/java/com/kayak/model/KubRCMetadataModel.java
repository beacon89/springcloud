package com.kayak.model;

import io.fabric8.kubernetes.api.model.ReplicationController;

public class KubRCMetadataModel {
	
	private boolean properties;
	
	public KubRCMetadataModel(ReplicationController replicationcontroller) {
		if(!replicationcontroller.getMetadata().getAdditionalProperties().isEmpty()) {
			this.properties = true;
		}
	}

	public boolean isProperties() {
		return properties;
	}

	public void setProperties(boolean properties) {
		this.properties = properties;
	}
	
	

}
