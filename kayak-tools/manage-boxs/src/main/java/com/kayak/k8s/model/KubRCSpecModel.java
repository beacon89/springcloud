package com.kayak.k8s.model;

import io.fabric8.kubernetes.api.model.ReplicationController;

public class KubRCSpecModel {
	
	private boolean properties;
	
	private KubRctTemplateModel template;
	
	public KubRCSpecModel(ReplicationController replicationcontroller) {
		if(!replicationcontroller.getSpec().getAdditionalProperties().isEmpty()) {
			this.properties = true;
		}
		if(replicationcontroller.getSpec().getTemplate() != null) {
			template = new KubRctTemplateModel(replicationcontroller);
		}
		
	}

	public boolean isProperties() {
		return properties;
	}

	public void setProperties(boolean properties) {
		this.properties = properties;
	}

	public KubRctTemplateModel getTemplate() {
		return template;
	}

	public void setTemplate(KubRctTemplateModel template) {
		this.template = template;
	}
	
	

}
