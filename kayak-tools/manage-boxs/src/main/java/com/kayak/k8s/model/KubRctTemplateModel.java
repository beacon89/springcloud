package com.kayak.k8s.model;

import io.fabric8.kubernetes.api.model.ReplicationController;

public class KubRctTemplateModel {
	
	private boolean properties;
	
	private KubTemplateMetadata metadata;
	
	public KubRctTemplateModel(ReplicationController replicationcontroller) {
		if(!replicationcontroller.getSpec().getTemplate().getAdditionalProperties().isEmpty()) {
			this.properties = true;
		}
		if(replicationcontroller.getSpec().getTemplate().getMetadata() != null) {
			this.metadata = new KubTemplateMetadata(replicationcontroller);
		}
		
	}

	public boolean isProperties() {
		return properties;
	}

	public void setProperties(boolean properties) {
		this.properties = properties;
	}

	public KubTemplateMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(KubTemplateMetadata metadata) {
		this.metadata = metadata;
	}
	
	
	

}
