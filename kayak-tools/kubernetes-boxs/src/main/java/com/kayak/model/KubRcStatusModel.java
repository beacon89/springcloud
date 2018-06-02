package com.kayak.model;

import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerCondition;

public class KubRcStatusModel {
	
	private boolean properties;
	
	private Boolean[] conditions = {};
	
	public KubRcStatusModel(ReplicationController replicationcontroller) {
		if(!replicationcontroller.getStatus().getAdditionalProperties().isEmpty()) {
			this.properties = true;
		}
		if(!replicationcontroller.getStatus().getConditions().isEmpty()) {
			int i = 0;
			conditions = new Boolean[replicationcontroller.getStatus().getConditions().size()];
			for(ReplicationControllerCondition condition:replicationcontroller.getStatus().getConditions()) {
				if(!condition.getAdditionalProperties().isEmpty()) {
					conditions[i] = true;
				}else {
					conditions[i] = false;
				}
				i++;
			}
		}
	}

	public boolean isProperties() {
		return properties;
	}

	public void setProperties(boolean properties) {
		this.properties = properties;
	}

	public Boolean[] getConditions() {
		return conditions;
	}

	public void setConditions(Boolean[] conditions) {
		this.conditions = conditions;
	}

	
	
	

}
