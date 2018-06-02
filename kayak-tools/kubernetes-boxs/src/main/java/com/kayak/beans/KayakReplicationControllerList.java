package com.kayak.beans;

import java.util.ArrayList;
import java.util.List;

import com.kayak.model.KayakRcModel;
import com.kayak.model.KubRcShowModel;
import com.kayak.model.TestModel;

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
		this.pageNumber = 1;
		this.pageSize = 20;
		this.items = new ArrayList<KayakRcModel>();
	}
	
	
	//TEST
	public KayakReplicationControllerList getKayakReplicationControllerListTest() {
		List<Integer> portlist = new ArrayList<Integer>();
		List<ReplicationController> list =  new ArrayList<>();
		TestModel model1 = new TestModel();
		list.add(model1.getReplicationController());
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
			items.add(model);
		}
		return this;
	}
	
	
	public KayakReplicationControllerList(int pageNumber,int pageSize,ReplicationControllerList rclist) {
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
