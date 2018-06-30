package com.kayak.k8s.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kayak.k8s.model.KayakRcModel;
import com.kayak.k8s.model.KubRcShowModel;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerList;

public class KayakReplicationControllerList {
	
	private List<KayakRcModel> items;
	
	private Integer totalCount;
	
	private Integer pageNumber;
	
	private Integer pageSize;
	
	public  KayakReplicationControllerList() {
		this.items = new ArrayList<KayakRcModel>();
	}
	
	
	public KayakReplicationControllerList(int pageNumber,int pageSize,ReplicationControllerList rclist) throws JsonProcessingException {
		this.totalCount = rclist.getItems().size();
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		List<ReplicationController> list = null;
		List<Integer> portlist = new ArrayList<Integer>();
		if(totalCount > 0) {
			int start = (pageNumber -1) * pageSize;
			int end = pageNumber * pageSize > totalCount ? totalCount : pageNumber * pageSize;
			list = rclist.getItems().subList(start,end);
		}else {
			list = rclist.getItems();
		}
		items = new ArrayList<KayakRcModel>();
		ObjectMapper mapper = new ObjectMapper();
		for(ReplicationController rc :list) {
			KayakRcModel model = new KayakRcModel();
			model.setApiVersion(rc.getApiVersion());
			model.setCreationTimestamp(rc.getMetadata().getCreationTimestamp());
			model.setGeneration(rc.getMetadata().getGeneration());
			model.setKind(rc.getKind());
			model.setName(rc.getMetadata().getName());
			model.setNamespace(rc.getMetadata().getNamespace());
			model.setReplicas(rc.getSpec().getReplicas());
			List<Container> containerlist = rc.getSpec().getTemplate().getSpec().getContainers();
			for(Container container:containerlist) {
				List<ContainerPort> containerportlist = container.getPorts();
				for(ContainerPort containerport:containerportlist) {
					portlist.add(containerport.getContainerPort());
				}
			}
			model.setPorts(portlist.toString());
			model.setReplicationcontroller(rc);
			model.setModelflag(new KubRcShowModel(rc));
			model.setJson(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rc));
			items.add(model);
		}
	}


	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	public List<KayakRcModel> getItems() {
		return items;
	}


	public void setItems(List<KayakRcModel> items) {
		this.items = items;
	}
	
	
	
	
	
}
