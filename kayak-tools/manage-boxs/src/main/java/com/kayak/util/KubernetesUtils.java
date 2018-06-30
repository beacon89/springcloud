package com.kayak.util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kayak.k8s.beans.KayakReplicationControllerList;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.ExecAction;
import io.fabric8.kubernetes.api.model.Handler;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.HostPathVolumeSource;
import io.fabric8.kubernetes.api.model.Lifecycle;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerSpec;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * 工具类操作用的
 * @author beacon
 *
 */
public class KubernetesUtils {
	
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
	
	public static ReplicationController getReplicationControllerByJson(String jsons) {
		JSONObject json = new JSONObject(jsons);
		return KubernetesUtils.getReplicationController(json);
	}
	
	private static ExecAction getExecAction(JSONObject json) {
		ExecAction action = new ExecAction();
		JSONArray array = json.getJSONArray("commands");
		List<String> command = new ArrayList<>();
		for(int i = 0;i<array.length();i++) {
			command.add(array.getString(i));
		}
		if(!command.isEmpty()) {
			action.setCommand(command);
		}
		return action;
	}
	
	private static Handler getHandler(JSONObject json) {
		Handler handler = new Handler();
		handler.setExec(KubernetesUtils.getExecAction(json));
		return handler;
	}
	
	private static Lifecycle getLifecycle(JSONObject json) {
		Lifecycle lifecycle = new Lifecycle();
		lifecycle.setPostStart(KubernetesUtils.getHandler(json));
		return lifecycle;
	}
	
	private static List<Container> getListContainer(JSONObject json){
		List<Container> containers = new ArrayList<>();
		Container con = new Container();
		con.setName(json.getString("spec_template_spec_containers_name"));
		con.setImage(json.getString("spec_template_spec_containers_image"));
		con.setLifecycle(KubernetesUtils.getLifecycle(json));
		JSONArray array = json.getJSONArray("envs");
		List<EnvVar> envlist = new ArrayList<>();
		for(int i = 0;i<array.length();i++) {
			JSONObject env = array.getJSONObject(i);
			EnvVar envvar = new EnvVar();
			envvar.setName(env.getString("name"));
			envvar.setValue(env.getString("value"));
			envlist.add(envvar);
		}
		if(!envlist.isEmpty()) {
			con.setEnv(envlist);
		}
		array = json.getJSONArray("volumeMounts");
		List<VolumeMount> volumeMounts = new ArrayList<>();
		for(int i = 0;i<array.length();i++) {
			JSONObject mountobj = array.getJSONObject(i);
			VolumeMount mount = new VolumeMount();
			mount.setName(mountobj.getString("name"));
			mount.setMountPath(mountobj.getString("mountPath"));
			volumeMounts.add(mount);
		}
		if(!volumeMounts.isEmpty()) {
			con.setVolumeMounts(volumeMounts);
		}
		containers.add(con);
		return containers;
	}
	
	private static PodSpec getPodSpec(JSONObject json) {
		PodSpec podSpect = new PodSpec();
		JSONArray array = json.getJSONArray("tempnodeSelector");
		Map<String,String> map = new HashMap<>();
		for(int i = 0;i<array.length();i++) {
			JSONArray arrayselector = array.getJSONArray(i);
			map.put(arrayselector.getString(0), arrayselector.getString(1));
		}
		if(!map.isEmpty()) {
			podSpect.setNodeSelector(map);
		}
		List<Container> list = KubernetesUtils.getListContainer(json);
		if(!list.isEmpty()) {
			podSpect.setContainers(list);
		}
		podSpect.setHostname(json.getString("spec_template_spec_hostname"));
		podSpect.setSubdomain(json.getString("spec_template_spec_subdomain"));
		array = json.getJSONArray("volumes");
		List<Volume> volumes = new ArrayList<>();
		for(int i = 0;i<array.length();i++) {
			Volume volume = new Volume();
			JSONObject volumesobj = array.getJSONObject(i);
			volume.setName(volumesobj.getString("name"));
			JSONObject hostpathobj = volumesobj.getJSONObject("hostPath");
			HostPathVolumeSource source = new HostPathVolumeSource();
			source.setPath(hostpathobj.getString("path"));
			volume.setHostPath(source);
			volumes.add(volume);
		}
		if(!volumes.isEmpty()) {
			podSpect.setVolumes(volumes);
		}
		return podSpect;
	}
	
	private static ObjectMeta getTemplateObjectMeta(JSONObject json) {
		ObjectMeta meta = new ObjectMeta();
		Map<String,String> map = new HashMap<>();
		JSONArray array = json.getJSONArray("templabels");
		for(int i = 0;i<array.length();i++) {
			JSONArray arrayselector = array.getJSONArray(i);
			map.put(arrayselector.getString(0), arrayselector.getString(1));
		}
		if(!map.isEmpty()) {
			meta.setLabels(map);
		}
		return meta;
	}
	
	private static PodTemplateSpec getPodTemplateSpec(JSONObject json) {
		PodTemplateSpec podtemplatespec = new PodTemplateSpec();
		podtemplatespec.setSpec(KubernetesUtils.getPodSpec(json));
		podtemplatespec.setMetadata(KubernetesUtils.getTemplateObjectMeta(json));
		return podtemplatespec;
	}
	
	private static ReplicationControllerSpec getReplicationControllerSpec(JSONObject json) {
		ReplicationControllerSpec spec = new ReplicationControllerSpec();
		spec.setTemplate(KubernetesUtils.getPodTemplateSpec(json));
		spec.setReplicas(json.getInt("spec_replicas"));
		Map<String,String> map = new HashMap<>();
		JSONArray array = json.getJSONArray("tempselector");
		for(int i = 0;i<array.length();i++) {
			JSONArray arrayselector = array.getJSONArray(i);
			map.put(arrayselector.getString(0), arrayselector.getString(1));
		}
		if(!map.isEmpty()) {
			spec.setSelector(map);
		}
		return spec;
	}
	
	private static ReplicationController getReplicationController(JSONObject json) {
		ReplicationController rc = new ReplicationController();
		rc.setApiVersion(json.getString("apiVersion"));
		rc.setKind(json.getString("kind"));
		rc.setMetadata(KubernetesUtils.getObjectMeta(json));
		rc.setSpec(KubernetesUtils.getReplicationControllerSpec(json));
		return rc;
	}
	
	private static ObjectMeta getObjectMeta(JSONObject json) {
		ObjectMeta objectmeta = new ObjectMeta();
		objectmeta.setName(json.getString("metadata_name"));
		return objectmeta;
	}
	
	public static ReplicationController getReplicationControllerBySteam(KubernetesClient kubClient,String str) {
		List<HasMetadata> resources = kubClient.resourceList(str).get();
		HasMetadata resource = resources.get(0);
		if(resource instanceof ReplicationController) {
			return (ReplicationController) resource;
		}
		return null;
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
	
	public static KayakReplicationControllerList getReplicationControllerLists(int pageNumber,int pageSize,KubernetesClient kubClient) throws JsonProcessingException {
		return new KayakReplicationControllerList(pageNumber, pageSize, kubClient.replicationControllers().list());
	}

}
