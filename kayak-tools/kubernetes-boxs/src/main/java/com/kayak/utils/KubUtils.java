package com.kayak.utils;


import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import com.kayak.beans.KayakReplicationControllerList;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * 工具类操作用的
 * @author beacon
 *
 */
public class KubUtils {
	
	public static Pod createPod(Pod pod, KubernetesClient kubClient) {
		return kubClient.pods().create(pod);
	}
	
	/**
	 * kubectl get nodes
	 * @param kubClient
	 * @return
	 */
	public static PodList getPodLists(KubernetesClient kubClient) {
		return kubClient.pods().list();
	}
	
	/**
	 * 获取版本
	 * @param kubClient
	 * @return
	 */
	public String getApiVersion(KubernetesClient kubClient) {
		return kubClient.getApiVersion();
	}
	
	public void load(KubernetesClient kubClient,String filename) {
		try {
			kubClient.load(new FileInputStream(filename)).get();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public static ReplicationController getReplicationControllerBySteam(KubernetesClient kubClient,String str) {
		List<HasMetadata> resources = kubClient.load(new ByteArrayInputStream(str.getBytes())).get();
		HasMetadata resource = resources.get(0);
		if(resource instanceof ReplicationController) {
			return (ReplicationController) resource;
		}
		return null;
	}

	public static void main(String[] args) {
//		Config config = new ConfigBuilder().build();
//		config.setNamespace("default");
//		KubernetesClient kubClient = new DefaultKubernetesClient(config);
//		try {
//			List<HasMetadata> resources = kubClient.load(new FileInputStream("/Users/cuiyifeng/pods/mysql.yml")).get();
//			 HasMetadata resource = resources.get(0);
//			 if (resource instanceof Pod){
//			        Pod pod = (Pod) resource;
//			        //KubUtils.createPod(pod, kubClient);
//			        KubUtils.deletePod(pod, kubClient);
//			        
//			 }else if(resource instanceof ReplicationController) {
//				 ReplicationController rc = (ReplicationController) resource;
//				 KubUtils.deleteReplicationController(rc, kubClient);
//			 }else {
//				 System.err.println("Loaded resource is not a Pod! " + resource);
//			 }
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}finally {
//			kubClient.close();
//		}
		System.out.println("/");
	}
	
	
	public static Boolean deletePod(Pod pod, KubernetesClient kubClient) {
		return kubClient.pods().delete(pod);
	}
	
	public static ReplicationController createReplicationController(ReplicationController rc, KubernetesClient kubClient) {
		return kubClient.replicationControllers().create(rc);
	}
	
	public static Boolean deleteReplicationController(ReplicationController rc, KubernetesClient kubClient) {
		return kubClient.replicationControllers().delete(rc);
	}
	
	public static KayakReplicationControllerList getReplicationControllerLists(int pageNumber,int pageSize,KubernetesClient kubClient) {
		return new KayakReplicationControllerList(pageNumber, pageSize, kubClient.replicationControllers().list());
		//测试专用
		//KayakReplicationControllerList tt =  new KayakReplicationControllerList().getKayakReplicationControllerListTest();
		//return tt;
	}

}
