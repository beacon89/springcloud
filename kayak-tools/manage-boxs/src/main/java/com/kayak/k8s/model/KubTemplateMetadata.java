package com.kayak.k8s.model;

import io.fabric8.kubernetes.api.model.ReplicationController;

public class KubTemplateMetadata {

	private boolean properties;
	
	public KubTemplateMetadata(ReplicationController replicationcontroller) {
		if(!replicationcontroller.getSpec().getTemplate().getMetadata().getAdditionalProperties().isEmpty()) {
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
