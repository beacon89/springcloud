package com.kayak.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric8.kubernetes.api.model.AWSElasticBlockStoreVolumeSource;
import io.fabric8.kubernetes.api.model.Affinity;
import io.fabric8.kubernetes.api.model.AzureDiskVolumeSource;
import io.fabric8.kubernetes.api.model.AzureFileVolumeSource;
import io.fabric8.kubernetes.api.model.Capabilities;
import io.fabric8.kubernetes.api.model.CephFSVolumeSource;
import io.fabric8.kubernetes.api.model.CinderVolumeSource;
import io.fabric8.kubernetes.api.model.ConfigMapEnvSource;
import io.fabric8.kubernetes.api.model.ConfigMapKeySelector;
import io.fabric8.kubernetes.api.model.ConfigMapProjection;
import io.fabric8.kubernetes.api.model.ConfigMapVolumeSource;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.DownwardAPIProjection;
import io.fabric8.kubernetes.api.model.DownwardAPIVolumeFile;
import io.fabric8.kubernetes.api.model.DownwardAPIVolumeSource;
import io.fabric8.kubernetes.api.model.EmptyDirVolumeSource;
import io.fabric8.kubernetes.api.model.EnvFromSource;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarSource;
import io.fabric8.kubernetes.api.model.ExecAction;
import io.fabric8.kubernetes.api.model.FCVolumeSource;
import io.fabric8.kubernetes.api.model.FlexVolumeSource;
import io.fabric8.kubernetes.api.model.FlockerVolumeSource;
import io.fabric8.kubernetes.api.model.GCEPersistentDiskVolumeSource;
import io.fabric8.kubernetes.api.model.GitRepoVolumeSource;
import io.fabric8.kubernetes.api.model.GlusterfsVolumeSource;
import io.fabric8.kubernetes.api.model.HTTPGetAction;
import io.fabric8.kubernetes.api.model.HTTPHeader;
import io.fabric8.kubernetes.api.model.Handler;
import io.fabric8.kubernetes.api.model.HostAlias;
import io.fabric8.kubernetes.api.model.HostPathVolumeSource;
import io.fabric8.kubernetes.api.model.ISCSIVolumeSource;
import io.fabric8.kubernetes.api.model.Initializer;
import io.fabric8.kubernetes.api.model.Initializers;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.KeyToPath;
import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.LabelSelectorRequirement;
import io.fabric8.kubernetes.api.model.Lifecycle;
import io.fabric8.kubernetes.api.model.ListMeta;
import io.fabric8.kubernetes.api.model.LocalObjectReference;
import io.fabric8.kubernetes.api.model.NFSVolumeSource;
import io.fabric8.kubernetes.api.model.NodeAffinity;
import io.fabric8.kubernetes.api.model.NodeSelector;
import io.fabric8.kubernetes.api.model.NodeSelectorRequirement;
import io.fabric8.kubernetes.api.model.NodeSelectorTerm;
import io.fabric8.kubernetes.api.model.ObjectFieldSelector;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.OwnerReference;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimVolumeSource;
import io.fabric8.kubernetes.api.model.PhotonPersistentDiskVolumeSource;
import io.fabric8.kubernetes.api.model.PodAffinity;
import io.fabric8.kubernetes.api.model.PodAffinityTerm;
import io.fabric8.kubernetes.api.model.PodAntiAffinity;
import io.fabric8.kubernetes.api.model.PodSecurityContext;
import io.fabric8.kubernetes.api.model.PodSpec;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.PortworxVolumeSource;
import io.fabric8.kubernetes.api.model.PreferredSchedulingTerm;
import io.fabric8.kubernetes.api.model.Probe;
import io.fabric8.kubernetes.api.model.ProjectedVolumeSource;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.QuobyteVolumeSource;
import io.fabric8.kubernetes.api.model.RBDVolumeSource;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.ReplicationControllerCondition;
import io.fabric8.kubernetes.api.model.ReplicationControllerSpec;
import io.fabric8.kubernetes.api.model.ReplicationControllerStatus;
import io.fabric8.kubernetes.api.model.ResourceFieldSelector;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.SELinuxOptions;
import io.fabric8.kubernetes.api.model.ScaleIOVolumeSource;
import io.fabric8.kubernetes.api.model.SecretEnvSource;
import io.fabric8.kubernetes.api.model.SecretKeySelector;
import io.fabric8.kubernetes.api.model.SecretProjection;
import io.fabric8.kubernetes.api.model.SecretVolumeSource;
import io.fabric8.kubernetes.api.model.SecurityContext;
import io.fabric8.kubernetes.api.model.Status;
import io.fabric8.kubernetes.api.model.StatusCause;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.api.model.StorageOSVolumeSource;
import io.fabric8.kubernetes.api.model.TCPSocketAction;
import io.fabric8.kubernetes.api.model.Toleration;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.VolumeProjection;
import io.fabric8.kubernetes.api.model.VsphereVirtualDiskVolumeSource;
import io.fabric8.kubernetes.api.model.WeightedPodAffinityTerm;

public class TestModel {
	
	//过
		private List<Initializer> getInitializerList() {
			List<Initializer> pending = new ArrayList<>();
			Initializer initializer1 = new Initializer();
			initializer1.setName("initializer-name1");
			initializer1.setAdditionalProperty("initializer1-key1", "initializer1-val1");
			initializer1.setAdditionalProperty("initializer1-key2", "initializer1-val2");
			
			Initializer initializer2 = new Initializer();
			initializer2.setName("initializer-name2");
			initializer2.setAdditionalProperty("initializer2-key1", "initializer2-val1");
			initializer2.setAdditionalProperty("initializer2-key2", "initializer2-val2");
			
			pending.add(initializer1);
			pending.add(initializer2);
			return pending;
		}

		//过
		private StatusDetails getStatusDetails() {
			StatusDetails details = new StatusDetails();
			details.setCauses(this.getStatusCauseList());
			details.setGroup("StatusDetails group");
			details.setKind("StatusDetails kind ");
			details.setName("StatusDetails name");
			details.setRetryAfterSeconds(10);
			details.setUid("StatusDetails uuid");
			details.setAdditionalProperty("StatusDetails-key1", "StatusDetails-val1");
			details.setAdditionalProperty("StatusDetails-key2", "StatusDetails-val2");
			return details;
		}

		//过
		private ListMeta getListMeta() {
			ListMeta metadata = new ListMeta();
			metadata.setResourceVersion("metadata.resourceVersion");
			metadata.setSelfLink("metadata.selfLink");
			metadata.setAdditionalProperty("ListMeta-key1", "ListMeta-val1");
			metadata.setAdditionalProperty("ListMeta-key2", "ListMeta-val2");
			return metadata;
		}

		//过
		private List<StatusCause> getStatusCauseList() {
			List<StatusCause> list = new ArrayList<>();
			StatusCause cause1 = new StatusCause();
			cause1.setField("StatusCause field1");
			cause1.setMessage("StatusCause message1");
			cause1.setReason("StatusCause reason1");
			cause1.setAdditionalProperty("StatusCause-key1", "StatusCause-val1");
			list.add(cause1);
			StatusCause cause2 = new StatusCause();
			cause2.setField("StatusCause field2");
			cause2.setMessage("StatusCause message2");
			cause2.setReason("StatusCause reason2");
			cause2.setAdditionalProperty("StatusCause-key2", "StatusCause-val2");
			list.add(cause2);
			return list;
		}

		//过
		private Status getStatus() {
			Status result = new Status();
			result.setAdditionalProperty("Status-key1", "Status-val1");
			result.setAdditionalProperty("Status-key2", "Status-val2");
			result.setApiVersion("meta/v1");
			result.setCode(10);
			result.setDetails(this.getStatusDetails());
			result.setKind("Status");
			result.setMessage("Status message");
			result.setMetadata(this.getListMeta());
			result.setReason("Status reason");
			result.setStatus("Status status");
			return result;
		}

		//过
		private Initializers getInitializers() {
			Initializers initializers = new Initializers();
			initializers.setPending(this.getInitializerList());
			initializers.setResult(this.getStatus());
			initializers.setAdditionalProperty("initializers-key1", "initializers-val1");
			initializers.setAdditionalProperty("initializers-key2", "initializers-val2");
			return initializers;
		}

		//过
		private List<OwnerReference> getOwnerReferenceList() {
			List<OwnerReference> list = new ArrayList<>();
			OwnerReference owner1 = new OwnerReference();
			owner1.setAdditionalProperty("OwnerReference-key1", "OwnerReference-val1");
			owner1.setAdditionalProperty("OwnerReference-key2", "OwnerReference-val2");
			owner1.setApiVersion("owner1-api");
			owner1.setBlockOwnerDeletion(true);
			owner1.setController(true);
			owner1.setKind("owner1-kind");
			owner1.setName("owner1-name");
			owner1.setUid("owner1-uuid");

			OwnerReference owner2 = new OwnerReference();
			owner2.setAdditionalProperty("OwnerReference-key1", "OwnerReference-val1");
			owner2.setAdditionalProperty("OwnerReference-key2", "OwnerReference-val2");
			owner2.setApiVersion("owner2-api");
			owner2.setBlockOwnerDeletion(true);
			owner2.setController(true);
			owner2.setKind("owner2-kind");
			owner2.setName("owner2-name");
			owner2.setUid("owner2-uuid");
			list.add(owner1);
			list.add(owner2);
			return list;
		}

		//过
		private ObjectMeta getObjectMeta() {
			ObjectMeta metadata = new ObjectMeta();
			Map<String, String> annotations = new HashMap<>();
			annotations.put("annotations1", "annotations1");
			annotations.put("annotations2", "annotations2");
			
			metadata.setAnnotations(annotations);
			metadata.setClusterName("clusterName");
			metadata.setCreationTimestamp("creationTimestamp");
			metadata.setDeletionGracePeriodSeconds(10L);
			metadata.setDeletionTimestamp("deletionTimestamp");
			
			List<String> finalizers = new ArrayList<>();
			finalizers.add("finalizers1");
			finalizers.add("finalizers2");
			metadata.setFinalizers(finalizers);
			metadata.setGenerateName("generateName");
			metadata.setGeneration(10L);
			metadata.setInitializers(this.getInitializers());
			Map<String, String> labels = new HashMap<>();
			labels.put("labels-key1", "labels-val1");
			labels.put("labels-key2", "labels-val2");
			metadata.setLabels(labels);
			metadata.setName("metadata-name");
			metadata.setNamespace("metadata-namespace");
			metadata.setOwnerReferences(this.getOwnerReferenceList());
			metadata.setResourceVersion("metadata-resourceVersion");
			metadata.setSelfLink("metadata-selfLink");
			metadata.setUid("metadata-uuid");
			metadata.setAdditionalProperty("metadata-property1", "metadata-val1");
			metadata.setAdditionalProperty("metadata-property2", "metadata-val2");
			return metadata;
		}

		public NodeAffinity getNodeAffinity() {
			NodeAffinity nodeAffinity = new NodeAffinity();
			nodeAffinity.setAdditionalProperty("nodeAffinity-key1", "nodeAffinity-val1");
			nodeAffinity.setAdditionalProperty("nodeAffinity-key2", "nodeAffinity-val2");
			nodeAffinity.setPreferredDuringSchedulingIgnoredDuringExecution(this.getPreferredSchedulingTermList());
			nodeAffinity.setRequiredDuringSchedulingIgnoredDuringExecution(this.getNodeSelector());
			return nodeAffinity;
		}

		public List<NodeSelectorTerm> getNodeSelectorTermList() {
			List<NodeSelectorTerm> list = new ArrayList<>();
			NodeSelectorTerm team1 = new NodeSelectorTerm();
			team1.setAdditionalProperty("NodeSelectorTerm1-key1", "NodeSelectorTerm1-val1");
			team1.setAdditionalProperty("NodeSelectorTerm1-key2", "NodeSelectorTerm1-val2");
			team1.setMatchExpressions(this.getNodeSelectorRequirementList1());
			
			list.add(team1);
			NodeSelectorTerm team2 = new NodeSelectorTerm();
			team2.setAdditionalProperty("NodeSelectorTerm2-key1", "NodeSelectorTerm2-val1");
			team2.setAdditionalProperty("NodeSelectorTerm2-key2", "NodeSelectorTerm2-val2");
			team2.setMatchExpressions(this.getNodeSelectorRequirementList2());
			list.add(team2);
			return list;
		}

		public List<NodeSelectorRequirement> getNodeSelectorRequirementList1() {
			List<NodeSelectorRequirement> list = new ArrayList<>();
			NodeSelectorRequirement node1 = new NodeSelectorRequirement();
			node1.setKey("key1");
			node1.setOperator("operator1");
			node1.setValues(this.getvaluesList("node1-kye1", 1));
			node1.setAdditionalProperty("NodeSelectorRequirement1-key1", "NodeSelectorRequirement1-val1");
			node1.setAdditionalProperty("NodeSelectorRequirement1-key2", "NodeSelectorRequirement1-val2");
			
			NodeSelectorRequirement node2 = new NodeSelectorRequirement();
			node2.setKey("key2");
			node2.setOperator("operator2");
			node2.setValues(this.getvaluesList("node2-kye2", 1));
			node2.setAdditionalProperty("NodeSelectorRequirement2-key1", "NodeSelectorRequirement2-val1");
			node2.setAdditionalProperty("NodeSelectorRequirement2-key2", "NodeSelectorRequirement2-val2");
			
			list.add(node1);
			list.add(node2);
			return list;
		}
		
		public List<String> getvaluesList(String key,int number){
			List<String> list = new ArrayList<>();
			list.add(key+number);
			list.add(key+(++number));
			return list;
		}

		public List<NodeSelectorRequirement> getNodeSelectorRequirementList2() {
			List<NodeSelectorRequirement> list = new ArrayList<>();
			NodeSelectorRequirement node1 = new NodeSelectorRequirement();
			node1.setKey("key3");
			node1.setOperator("operator3");
			node1.setValues(this.getvaluesList("node3-kye3", 1));
			NodeSelectorRequirement node2 = new NodeSelectorRequirement();
			node2.setKey("key4");
			node2.setOperator("operator4");
			node2.setValues(this.getvaluesList("node4-kye3", 1));
			list.add(node1);
			list.add(node2);
			return list;
		}

		public NodeSelector getNodeSelector() {
			NodeSelector nodeSelectorTerms = new NodeSelector();
			nodeSelectorTerms.setAdditionalProperty("NodeSelector-key1", "NodeSelector-val1");
			nodeSelectorTerms.setAdditionalProperty("NodeSelector-key2", "NodeSelector-val2");
			nodeSelectorTerms.setNodeSelectorTerms(this.getNodeSelectorTermList());
			return nodeSelectorTerms;
		}
		
		public NodeSelectorTerm getNodeSelectorTerm1() {
			NodeSelectorTerm team = new NodeSelectorTerm();
			team.setMatchExpressions(this.getNodeSelectorRequirementList1());
			team.setAdditionalProperty("team-key1", "team-val1");
			return team;
		}
		
		public NodeSelectorTerm getNodeSelectorTerm2() {
			NodeSelectorTerm team = new NodeSelectorTerm();
			team.setMatchExpressions(this.getNodeSelectorRequirementList1());
			team.setAdditionalProperty("team-key2", "team-val2");
			return team;
		}

		public List<PreferredSchedulingTerm> getPreferredSchedulingTermList() {
			List<PreferredSchedulingTerm> preferredDuringSchedulingIgnoredDuringExecution = new ArrayList<>();
			PreferredSchedulingTerm term1 = new PreferredSchedulingTerm();
			term1.setPreference(this.getNodeSelectorTerm1());
			term1.setAdditionalProperty("PreferredSchedulingTerm1-key1", "PreferredSchedulingTerm1-val1");
			term1.setAdditionalProperty("PreferredSchedulingTerm1-key2", "PreferredSchedulingTerm1-val2");
			term1.setWeight(10);
			preferredDuringSchedulingIgnoredDuringExecution.add(term1);
			PreferredSchedulingTerm term2 = new PreferredSchedulingTerm();
			term2.setPreference(this.getNodeSelectorTerm2());
			term2.setAdditionalProperty("PreferredSchedulingTerm2-key1", "PreferredSchedulingTerm2-val1");
			term2.setAdditionalProperty("PreferredSchedulingTerm2-key2", "PreferredSchedulingTerm2-val2");
			term2.setWeight(10);
			preferredDuringSchedulingIgnoredDuringExecution.add(term2);
			return preferredDuringSchedulingIgnoredDuringExecution;
		}
		
		public List<LabelSelectorRequirement> getLabelSelectorRequirementList(){
			List<LabelSelectorRequirement> list = new ArrayList<>();
			LabelSelectorRequirement irement = new LabelSelectorRequirement();
			irement.setKey("key");
			irement.setOperator("operator");
			irement.setValues(this.getStringlist());
			irement.setAdditionalProperty("irement-key1", "irement-val1");
			irement.setAdditionalProperty("irement-key2", "irement-val2");
			list.add(irement);
			
			LabelSelectorRequirement irement1 = new LabelSelectorRequirement();
			irement1.setKey("key");
			irement1.setOperator("operator");
			irement1.setValues(this.getStringlist());
			irement1.setAdditionalProperty("irement-key1", "irement-val1");
			irement1.setAdditionalProperty("irement-key2", "irement-val2");
			list.add(irement1);
			
			return list;
		}
		
		public LabelSelector getLabelSelector() {
			LabelSelector selector = new LabelSelector();
			selector.setMatchLabels(this.getMap());
			selector.setAdditionalProperty("selector-key1", "selector-val1");
			selector.setAdditionalProperty("selector-key2", "selector-val2");
			selector.setMatchExpressions(this.getLabelSelectorRequirementList());
			return selector;
		}
		
		public PodAffinityTerm getPodAffinityTerm() {
			PodAffinityTerm term = new PodAffinityTerm();
			term.setAdditionalProperty("PodAffinityTerm-key1", "PodAffinityTerm-val1");
			term.setAdditionalProperty("PodAffinityTerm-key2", "PodAffinityTerm-val2");
			term.setLabelSelector(this.getLabelSelector());
			term.setTopologyKey("topologyKey");
			term.setNamespaces(this.getStringlist());
			return term;
		}
		
		public List<WeightedPodAffinityTerm> getWeightedPodAffinityTermList(){
			List<WeightedPodAffinityTerm> list = new ArrayList<>();
			WeightedPodAffinityTerm weightedpod = new WeightedPodAffinityTerm();
			weightedpod.setAdditionalProperty("weightedpod-key1", "weightedpod-val1");
			weightedpod.setAdditionalProperty("weightedpod-key2", "weightedpod-val2");
			weightedpod.setPodAffinityTerm(this.getPodAffinityTerm());
			weightedpod.setWeight(10);
			list.add(weightedpod);
			
			WeightedPodAffinityTerm weightedpod1 = new WeightedPodAffinityTerm();
			weightedpod1.setAdditionalProperty("weightedpod-key1", "weightedpod-val1");
			weightedpod1.setAdditionalProperty("weightedpod-key2", "weightedpod-val2");
			weightedpod1.setPodAffinityTerm(this.getPodAffinityTerm());
			weightedpod1.setWeight(10);
			list.add(weightedpod1);
			return list;
		}
		
		public List<PodAffinityTerm> getPodAffinityTermList(){
			List<PodAffinityTerm> list = new ArrayList<>();
			PodAffinityTerm term = new PodAffinityTerm();
			term.setNamespaces(this.getStringlist());
			term.setTopologyKey("topologyKey1");
			term.setAdditionalProperty("PodAffinityTerm-key1", "PodAffinityTerm-val1");
			term.setAdditionalProperty("PodAffinityTerm-key2", "PodAffinityTerm-val2");
			term.setLabelSelector(this.getLabelSelector());
			list.add(term);
			
			PodAffinityTerm term1 = new PodAffinityTerm();
			term1.setNamespaces(this.getStringlist());
			term1.setTopologyKey("topologyKey2");
			term1.setAdditionalProperty("PodAffinityTerm-key1", "PodAffinityTerm-val1");
			term1.setAdditionalProperty("PodAffinityTerm-key2", "PodAffinityTerm-val2");
			term1.setLabelSelector(this.getLabelSelector());
			list.add(term1);
			
			return list;
		}

		public PodAffinity getPodAffinity() {
			PodAffinity podAffinity = new PodAffinity();
			podAffinity.setPreferredDuringSchedulingIgnoredDuringExecution(this.getWeightedPodAffinityTermList());
			podAffinity.setAdditionalProperty("podAffinity-key1", "podAffinity-val1");
			podAffinity.setAdditionalProperty("podAffinity-key2", "podAffinity-val2");
			podAffinity.setRequiredDuringSchedulingIgnoredDuringExecution(this.getPodAffinityTermList());
			return podAffinity;
		}
		

		public PodAntiAffinity getPodAntiAffinity() {
			PodAntiAffinity podAntiAffinity = new PodAntiAffinity();
			podAntiAffinity.setPreferredDuringSchedulingIgnoredDuringExecution(this.getWeightedPodAffinityTermList());
			podAntiAffinity.setRequiredDuringSchedulingIgnoredDuringExecution(this.getPodAffinityTermList());
			podAntiAffinity.setAdditionalProperty("podAntiAffinity-key1", "podAntiAffinity-val1");
			podAntiAffinity.setAdditionalProperty("podAntiAffinity-key1", "podAntiAffinity-val1");
			return podAntiAffinity;
		}

		public Affinity getAffinity() {
			Affinity affinity = new Affinity();
			affinity.setNodeAffinity(this.getNodeAffinity());
			affinity.setPodAffinity(this.getPodAffinity());
			affinity.setPodAntiAffinity(this.getPodAntiAffinity());
			affinity.setAdditionalProperty("affinity-key1", "affinity-val1");
			affinity.setAdditionalProperty("affinity-key2", "affinity-val2");
			return affinity;
		}


		public List<HostAlias> getHostAliasList() {
			List<HostAlias> hostAliases = new ArrayList<>();
			HostAlias host = new HostAlias();
			host.setHostnames(this.getStringlist());
			host.setIp("ip1");
			host.setAdditionalProperty("host-key1", "host-val1");
			host.setAdditionalProperty("host-key2", "host-val2");
			hostAliases.add(host);
			
			HostAlias host1 = new HostAlias();
			host1.setHostnames(this.getStringlist());
			host1.setIp("ip2");
			host1.setAdditionalProperty("host-key1", "host-val1");
			host1.setAdditionalProperty("host-key2", "host-val2");
			hostAliases.add(host1);
			return hostAliases;
		}

		public List<LocalObjectReference> getLocalObjectReferenceList() {
			List<LocalObjectReference> imagePullSecrets = new ArrayList<>();
			LocalObjectReference reference1 = new LocalObjectReference();
			reference1.setName("name1");
			reference1.setAdditionalProperty("reference1-key1", "reference1-value1");
			reference1.setAdditionalProperty("reference1-key2", "reference1-value2");
			imagePullSecrets.add(reference1);
			LocalObjectReference reference2 = new LocalObjectReference();
			reference2.setName("name2");
			reference2.setAdditionalProperty("reference2-key1", "reference2-value1");
			reference2.setAdditionalProperty("reference2-key2", "reference2-value2");
			imagePullSecrets.add(reference2);
			return imagePullSecrets;
		}
		
		public ConfigMapKeySelector getConfigMapKeySelector() {
			ConfigMapKeySelector source = new ConfigMapKeySelector();
			source.setAdditionalProperty("source-key1", "source-val1");
			source.setAdditionalProperty("source-key2", "source-val1");
			source.setKey("key");
			source.setName("name");
			source.setOptional(true);
			return source;
		}
		
		public SecretKeySelector getSecretKeySelector() {
			SecretKeySelector selctor = new SecretKeySelector();
			selctor.setAdditionalProperty("source-key1", "source-val1");
			selctor.setAdditionalProperty("source-key2", "source-val1");
			selctor.setKey("key");
			selctor.setName("name");
			selctor.setOptional(true);
			return selctor;
		}
		
		public EnvVarSource getEnvVarSource() {
			EnvVarSource source = new EnvVarSource();
			source.setAdditionalProperty("source-key1", "source-val1");
			source.setAdditionalProperty("source-key2", "source-val1");
			source.setConfigMapKeyRef(this.getConfigMapKeySelector());
			source.setFieldRef(this.getObjectFieldSelector());
			source.setResourceFieldRef(this.getResourceFieldSelector());
			source.setSecretKeyRef(this.getSecretKeySelector());
			return source;
		}
		
		public List<EnvVar> getEnvVarList(){
			List<EnvVar> list = new ArrayList<>();
			EnvVar var1 = new EnvVar();
			var1.setName("name1");
			var1.setValue("value1");
			var1.setAdditionalProperty("var-key1", "var-val1");
			var1.setAdditionalProperty("var-key2", "var-val1");
			var1.setValueFrom(this.getEnvVarSource());
			list.add(var1);
			EnvVar var2 = new EnvVar();
			var2.setName("name2");
			var2.setValue("value2");
			var2.setAdditionalProperty("var-key1", "var-val1");
			var2.setAdditionalProperty("var-key2", "var-val1");
			var2.setValueFrom(this.getEnvVarSource());
			list.add(var2);
			return list;
		}
		
		public ConfigMapEnvSource getConfigMapEnvSource() {
			ConfigMapEnvSource source = new ConfigMapEnvSource();
			source.setName("name");
			source.setOptional(true);
			source.setAdditionalProperty("ConfigMapEnvSource-key1", "ConfigMapEnvSource-val1");
			source.setAdditionalProperty("ConfigMapEnvSource-key2", "ConfigMapEnvSource-val2");
			return source;
		}
		
		public SecretEnvSource getSecretEnvSource() {
			SecretEnvSource source = new SecretEnvSource();
			source.setName("name");
			source.setOptional(true);
			source.setAdditionalProperty("SecretEnvSource-key1", "SecretEnvSource-val1");
			source.setAdditionalProperty("SecretEnvSource-key2", "SecretEnvSource-val2");
			return source;
		}
		
		public List<EnvFromSource> getEnvFromSourceList(){
			List<EnvFromSource> source = new ArrayList<>();
			EnvFromSource from = new EnvFromSource();
			from.setConfigMapRef(this.getConfigMapEnvSource());
			from.setPrefix("prefix1");
			from.setSecretRef(this.getSecretEnvSource());
			from.setAdditionalProperty("EnvFromSource1-key1", "EnvFromSource1-val1");
			from.setAdditionalProperty("EnvFromSource1-key2", "EnvFromSource1-val2");
			source.add(from);
			from = new EnvFromSource();
			from.setConfigMapRef(this.getConfigMapEnvSource());
			from.setPrefix("prefix2");
			from.setSecretRef(this.getSecretEnvSource());
			from.setAdditionalProperty("EnvFromSource2-key1", "EnvFromSource2-val1");
			from.setAdditionalProperty("EnvFromSource2-key2", "EnvFromSource2-val2");
			source.add(from);
			return source;
		}
		
		public ExecAction getExecAction() {
			ExecAction action = new ExecAction();
			action.setAdditionalProperty("ExecAction-key1", "ExecAction-val1");
			action.setAdditionalProperty("ExecAction-key2", "ExecAction-val2");
			action.setCommand(this.getStringlist());
			return action;
		}
		
		public List<HTTPHeader> getHTTPHeaderList(){
			List<HTTPHeader> list = new ArrayList<>();
			HTTPHeader header = new HTTPHeader();
			header.setName("name1");
			header.setValue("value1");
			header.setAdditionalProperty("HTTPHeader1-key1", "HTTPHeader1-val1");
			header.setAdditionalProperty("HTTPHeader1-key2", "HTTPHeader1-val2");
			list.add(header);
			header = new HTTPHeader();
			header.setName("name2");
			header.setValue("value2");
			header.setAdditionalProperty("HTTPHeader2-key1", "HTTPHeader2-val1");
			header.setAdditionalProperty("HTTPHeader2-key2", "HTTPHeader2-val2");
			list.add(header);
			return list;
		}
		
		public IntOrString getIntOrString() {
			IntOrString str = new IntOrString();
			str.setIntVal(10);
			str.setKind(10);
			str.setStrVal("StrVal");
			str.setAdditionalProperty("IntOrString-key1", "IntOrString-val1");
			str.setAdditionalProperty("IntOrString-key2", "IntOrString-val2");
			return str;
		}
		
		public HTTPGetAction getHTTPGetAction() {
			HTTPGetAction action = new HTTPGetAction();
			action.setAdditionalProperty("HTTPGetAction-key1", "HTTPGetAction-val1");
			action.setAdditionalProperty("HTTPGetAction-key2", "HTTPGetAction-val2");
			action.setHost("host");
			action.setHttpHeaders(this.getHTTPHeaderList());
			action.setPath("path");
			action.setPort(this.getIntOrString());
			action.setScheme("scheme");
			return action;
		}
		
		public TCPSocketAction getTCPSocketAction() {
			TCPSocketAction action = new TCPSocketAction();
			action.setHost("host");
			action.setAdditionalProperty("TCPSocketAction-key1", "TCPSocketAction-val1");
			action.setAdditionalProperty("TCPSocketAction-key2", "TCPSocketAction-val2");
			action.setPort(getIntOrString());
			return action;
		}
		
		public Handler getHandler() {
			Handler hadler = new Handler();
			hadler.setAdditionalProperty("Handler-key1", "Handler-val1");
			hadler.setAdditionalProperty("Handler-key2", "Handler-val2");
			hadler.setExec(this.getExecAction());
			hadler.setHttpGet(this.getHTTPGetAction());
			hadler.setTcpSocket(this.getTCPSocketAction());
			return hadler;
		}
		
		public Lifecycle getLifecycle() {
			Lifecycle cycle = new Lifecycle();
			cycle.setAdditionalProperty("Lifecycle-key1", "Lifecycle-val1");
			cycle.setAdditionalProperty("Lifecycle-key2", "Lifecycle-val2");
			cycle.setPostStart(this.getHandler());
			cycle.setPreStop(this.getHandler());
			return cycle;
		}
		
		public Probe getProbe() {
			Probe probe = new Probe();
			probe.setAdditionalProperty("Probe-key1", "Probe-val1");
			probe.setAdditionalProperty("Probe-key2", "Probe-val2");
			probe.setExec(this.getExecAction());
			probe.setFailureThreshold(10);
			probe.setHttpGet(this.getHTTPGetAction());
			probe.setInitialDelaySeconds(10);
			probe.setPeriodSeconds(10);
			probe.setSuccessThreshold(10);
			probe.setTcpSocket(this.getTCPSocketAction());
			probe.setTimeoutSeconds(10);
			return probe;
		}

		public List<ContainerPort> getContainerPortList(){
			List<ContainerPort> list = new ArrayList<>();
			ContainerPort port = new ContainerPort();
			port.setAdditionalProperty("ContainerPort1-key1", "ContainerPort1-val1");
			port.setAdditionalProperty("ContainerPort1-key2", "ContainerPort1-val1");
			port.setContainerPort(10);
			port.setHostIP("hostIP1");
			port.setHostPort(80);
			port.setName("name1");
			port.setProtocol("protocol1");
			list.add(port);
			port = new ContainerPort();
			port.setAdditionalProperty("ContainerPort2-key1", "ContainerPort2-val1");
			port.setAdditionalProperty("ContainerPort2-key2", "ContainerPort2-val1");
			port.setContainerPort(10);
			port.setHostIP("hostIP2");
			port.setHostPort(80);
			port.setName("name2");
			port.setProtocol("protocol2");
			list.add(port);
			return list;
		}
		
		public Map<String, io.fabric8.kubernetes.api.model.Quantity> getQuantityMap(){
			Map<String, io.fabric8.kubernetes.api.model.Quantity> map = new HashMap<>();
			Quantity tity = new Quantity();
			tity.setAmount("amount1");
			tity.setFormat("format1");
			tity.setAdditionalProperty("Quantity1-key1", "Quantity1-val1");
			tity.setAdditionalProperty("Quantity1-key2", "Quantity1-val1");
			map.put("tity1", tity);
			tity = new Quantity();
			tity.setAmount("amount2");
			tity.setFormat("format2");
			tity.setAdditionalProperty("Quantity1-key2", "Quantity2-val1");
			tity.setAdditionalProperty("Quantity1-key2", "Quantity2-val1");
			map.put("tity2", tity);
			return map;
		}
		
		public ResourceRequirements getResourceRequirements() {
			ResourceRequirements irements = new ResourceRequirements();
			irements.setLimits(this.getQuantityMap());
			irements.setRequests(this.getQuantityMap());
			irements.setAdditionalProperty("ResourceRequirements-key1", "ResourceRequirements-val1");
			irements.setAdditionalProperty("ResourceRequirements-key2", "ResourceRequirements-val1");
			return irements;
		}
		
		public Capabilities getCapabilities() {
			Capabilities cap = new Capabilities();
			cap.setAdd(this.getStringlist());
			cap.setDrop(this.getStringlist());
			cap.setAdditionalProperty("Capabilities-key1", "Capabilities-val1");
			cap.setAdditionalProperty("Capabilities-key2", "Capabilities-val1");
			return cap;
		}
		
		public SecurityContext getSecurityContext() {
			SecurityContext context = new SecurityContext();
			context.setAdditionalProperty("SecurityContext-key1", "SecurityContext-val1");
			context.setAdditionalProperty("SecurityContext-key2", "SecurityContext-val1");
			context.setCapabilities(this.getCapabilities());
			context.setPrivileged(true);
			context.setReadOnlyRootFilesystem(true);
			context.setRunAsNonRoot(true);
			context.setRunAsUser(10l);
			context.setSeLinuxOptions(this.getSELinuxOptions());
			return context;
		}
		
		public List<VolumeMount> getVolumeMountList(){
			List<VolumeMount> list = new ArrayList<>();
			VolumeMount mount = new VolumeMount();
			mount.setMountPath("mountPath1");
			mount.setName("name1");
			mount.setReadOnly(true);
			mount.setSubPath("subPath1");
			mount.setAdditionalProperty("VolumeMount1-key1", "VolumeMount1-val1");
			mount.setAdditionalProperty("VolumeMount1-key2", "VolumeMount1-val1");
			list.add(mount);
			
			mount = new VolumeMount();
			mount.setMountPath("mountPath2");
			mount.setName("name2");
			mount.setReadOnly(true);
			mount.setSubPath("subPath2");
			mount.setAdditionalProperty("VolumeMount2-key1", "VolumeMount2-val1");
			mount.setAdditionalProperty("VolumeMount2-key2", "VolumeMount2-val1");
			list.add(mount);
			return list;
		}
		
		public List<Container> getContainerList() {
			List<Container> initContainers = new ArrayList<>();
			Container container1 = new Container();
			container1.setAdditionalProperty("initContainers1-key1", "initContainers1-val1");
			container1.setAdditionalProperty("initContainers1-key2", "initContainers1-val1");
			container1.setArgs(this.getStringlist());
			container1.setCommand(this.getStringlist());
			container1.setEnv(this.getEnvVarList());
			container1.setEnvFrom(this.getEnvFromSourceList());
			container1.setImage("image1");
			container1.setImagePullPolicy("imagePullPolicy1");
			container1.setLifecycle(this.getLifecycle());
			container1.setLivenessProbe(this.getProbe());
			container1.setName("name1");
			container1.setPorts(this.getContainerPortList());
			container1.setReadinessProbe(this.getProbe());
			container1.setResources(this.getResourceRequirements());
			container1.setSecurityContext(this.getSecurityContext());
			container1.setStdin(true);
			container1.setStdinOnce(true);
			container1.setTerminationMessagePath("terminationMessagePath1");
			container1.setTerminationMessagePolicy("terminationMessagePolicy1");
			container1.setTty(true);
			container1.setVolumeMounts(this.getVolumeMountList());
			container1.setWorkingDir("workingDir1");
			initContainers.add(container1);
			
			
			Container container2 = new Container();
			container2.setAdditionalProperty("initContainers2-key1", "initContainers2-val1");
			container2.setAdditionalProperty("initContainers2-key2", "initContainers2-val1");
			container2.setArgs(this.getStringlist());
			container2.setCommand(this.getStringlist());
			container2.setEnv(this.getEnvVarList());
			container2.setEnvFrom(this.getEnvFromSourceList());
			container2.setImage("image2");
			container2.setImagePullPolicy("imagePullPolicy2");
			container2.setLifecycle(this.getLifecycle());
			container2.setLivenessProbe(this.getProbe());
			container2.setName("name2");
			container2.setPorts(this.getContainerPortList());
			container2.setReadinessProbe(this.getProbe());
			container2.setResources(this.getResourceRequirements());
			container2.setSecurityContext(this.getSecurityContext());
			container2.setStdin(true);
			container2.setStdinOnce(true);
			container2.setTerminationMessagePath("terminationMessagePath2");
			container2.setTerminationMessagePolicy("terminationMessagePolicy2");
			container2.setTty(true);
			container2.setVolumeMounts(this.getVolumeMountList());
			container2.setWorkingDir("workingDir2");
			initContainers.add(container2);
			
			return initContainers;
		}
		
		public List<Long> getLongList(){
			List<Long> list = new ArrayList<>();
			list.add(1l);
			list.add(2l);
			return list;
		}
		
		public SELinuxOptions getSELinuxOptions() {
			SELinuxOptions options = new SELinuxOptions();
			options.setAdditionalProperty("options-key1", "options-val1");
			options.setAdditionalProperty("options-key2", "options-val2");
			options.setLevel("level");
			options.setRole("role");
			options.setType("type");
			options.setUser("user");
			return options;
		}

		public PodSecurityContext getPodSecurityContext() {
			PodSecurityContext securityContext = new PodSecurityContext();
			securityContext.setAdditionalProperty("securityContext-key1", "securityContext-val1");
			securityContext.setAdditionalProperty("securityContext-key2", "securityContext-val2");
			securityContext.setFsGroup(10l);
			securityContext.setRunAsNonRoot(true);
			securityContext.setRunAsUser(10l);
			securityContext.setSeLinuxOptions(this.getSELinuxOptions());
			securityContext.setSupplementalGroups(this.getLongList());
			return securityContext;
		}

		public List<Toleration> getTolerationList() {
			List<Toleration> tolerations = new ArrayList<>();
			Toleration toleration1 = new Toleration();
			toleration1.setAdditionalProperty("property_key1", "property_val1");
			toleration1.setAdditionalProperty("property_key2", "property_val2");
			toleration1.setEffect("effect1");
			toleration1.setKey("key1");
			toleration1.setOperator("operator1");
			toleration1.setTolerationSeconds(1l);
			toleration1.setValue("value1");
			tolerations.add(toleration1);
			Toleration toleration2 = new Toleration();
			toleration2.setAdditionalProperty("property_key1", "property_val1");
			toleration2.setAdditionalProperty("property_key2", "property_val2");
			toleration2.setEffect("effect2");
			toleration2.setKey("key2");
			toleration2.setOperator("operator2");
			toleration2.setTolerationSeconds(2l);
			toleration2.setValue("value2");
			tolerations.add(toleration2);
			return tolerations;
		}
		
		public AWSElasticBlockStoreVolumeSource getAWSElasticBlockStoreVolumeSource(String key,int value) {
			AWSElasticBlockStoreVolumeSource source = new AWSElasticBlockStoreVolumeSource();
			source.setFsType(key+"fstype");
			source.setPartition(10);
			source.setAdditionalProperty(key+"key"+value, key+"val");
			source.setAdditionalProperty(key+"key"+(++value), key+"val");
			source.setReadOnly(true);
			source.setVolumeID(key+value);
			return source;
		}
		
		public AzureDiskVolumeSource getAzureDiskVolumeSource() {
			AzureDiskVolumeSource source = new AzureDiskVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setCachingMode("cachingMode");
			source.setDiskName("disname");
			source.setDiskURI("diskURI");
			source.setFsType("fsType");
			source.setKind("kind");
			source.setReadOnly(true);
			return source;
		}
		
		public AzureFileVolumeSource getAzureFileVolumeSource() {
			AzureFileVolumeSource source = new AzureFileVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setReadOnly(true);
			source.setSecretName("secretName");
			source.setShareName("shareName");
			return source;
		}
		
		public CephFSVolumeSource getCephFSVolumeSource() {
			CephFSVolumeSource source = new CephFSVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setPath("path");
			source.setReadOnly(true);
			source.setSecretFile("secretFile");
			source.setMonitors(this.getStringlist());
			source.setSecretRef(this.getLocalObjectReference());
			source.setUser("user");
			return source;
		}
		
		
		public List<String> getStringlist(){
			List<String> list = new ArrayList<>();
			list.add("key1");
			list.add("key2");
			list.add("key3");
			return list;
		}
		
		public CinderVolumeSource getCinderVolumeSource() {
			CinderVolumeSource source = new CinderVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setFsType("fsType");
			source.setReadOnly(true);
			source.setVolumeID("volumeID");
			return source;
		}
		
		public List<KeyToPath> getKeyToPath(){
			List<KeyToPath> list = new ArrayList<>();
			KeyToPath path = new KeyToPath();
			path.setAdditionalProperty("property_key1", "property_val1");
			path.setAdditionalProperty("property_key2", "property_val2");
			path.setKey("key1");
			path.setMode(1);
			path.setPath("path1");
			list.add(path);
			path = new KeyToPath();
			path.setAdditionalProperty("property_key3", "property_val3");
			path.setAdditionalProperty("property_key4", "property_val4");
			path.setKey("key2");
			path.setMode(2);
			path.setPath("path2");
			list.add(path);
			return list;
		}
		
		public ConfigMapVolumeSource getConfigMapVolumeSource() {
			ConfigMapVolumeSource source = new ConfigMapVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setDefaultMode(10);
			source.setItems(this.getKeyToPath());
			source.setName("name");
			source.setOptional(true);
			return source;
		}
		
		
		public Quantity getQuantity() {
			Quantity tity = new Quantity();
			tity.setAdditionalProperty("property_key1", "property_val1");
			tity.setAdditionalProperty("property_key2", "property_val2");
			tity.setAmount("amount");
			tity.setFormat("format");
			return tity;
		}
		
		public ResourceFieldSelector getResourceFieldSelector() {
			ResourceFieldSelector selector = new ResourceFieldSelector();
			selector.setAdditionalProperty("property_key1", "property_val1");
			selector.setAdditionalProperty("property_key2", "property_val2");
			selector.setContainerName("containerName");
			selector.setDivisor(this.getQuantity());
			selector.setResource("resource");
			return selector;
		}
		
		public List<DownwardAPIVolumeFile> getDownwardAPIVolumeFile(){
			List<DownwardAPIVolumeFile> list = new ArrayList<>();
			DownwardAPIVolumeFile source = new DownwardAPIVolumeFile();
			source.setAdditionalProperty("DownwardAPIVolumeFile_key1", "DownwardAPIVolumeFile_val1");
			source.setAdditionalProperty("DownwardAPIVolumeFile_key2", "DownwardAPIVolumeFile_val2");
			source.setFieldRef(this.getObjectFieldSelector());
			source.setMode(1);
			source.setPath("path1");
			source.setResourceFieldRef(this.getResourceFieldSelector());
			list.add(source);
			source = new DownwardAPIVolumeFile();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setFieldRef(this.getObjectFieldSelector());
			source.setMode(2);
			source.setPath("path2");
			source.setResourceFieldRef(this.getResourceFieldSelector());
			list.add(source);
			return list;
		}
		
		
		public DownwardAPIVolumeSource getDownwardAPIVolumeSource() {
			DownwardAPIVolumeSource source = new DownwardAPIVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setDefaultMode(10);
			source.setItems(this.getDownwardAPIVolumeFile());
			return source;
		}
		
		public FCVolumeSource getFCVolumeSource() {
			FCVolumeSource source = new FCVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setFsType("fsType");
			source.setLun(10);
			source.setReadOnly(true);
			source.setTargetWWNs(this.getStringlist());
			return source;
		}
		
		
		public EmptyDirVolumeSource getEmptyDirVolumeSource() {
			EmptyDirVolumeSource source = new EmptyDirVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setMedium("medium");
			source.setSizeLimit(this.getQuantity());
			return source;
		}
		
		public Map<String,String> getMap(){
			Map<String,String> map = new HashMap<>();
			map.put("key1", "value1");
			map.put("key2", "value2");
			map.put("key3", "value3");
			return map;
		}
		
		public LocalObjectReference getLocalObjectReference() {
			LocalObjectReference reference = new LocalObjectReference();
			reference.setAdditionalProperty("property_key1", "property_val1");
			reference.setAdditionalProperty("property_key2", "property_val2");
			reference.setName("name");
			return reference;
		}
		
		public FlexVolumeSource getFlexVolumeSource() {
			FlexVolumeSource source = new FlexVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setDriver("driver");
			source.setFsType("fsType");
			source.setReadOnly(true);
			source.setOptions(this.getMap());
			source.setSecretRef(this.getLocalObjectReference());
			return source;
		}
		
		public FlockerVolumeSource getFlockerVolumeSource() {
			FlockerVolumeSource source = new FlockerVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setDatasetName("datasetName");
			source.setDatasetUUID("datasetUUID");
			return source;
		}
		
		public GCEPersistentDiskVolumeSource getGCEPersistentDiskVolumeSource() {
			GCEPersistentDiskVolumeSource source = new GCEPersistentDiskVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setFsType("fsType");
			source.setPartition(10);
			source.setPdName("pdName");
			source.setReadOnly(true);
			return source;
		}
		
		public GitRepoVolumeSource getGitRepoVolumeSource() {
			GitRepoVolumeSource source = new GitRepoVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setDirectory("directory");
			source.setRepository("repository");
			source.setRevision("revision");
			return source;
		}
		
		public GlusterfsVolumeSource getGlusterfsVolumeSource() {
			GlusterfsVolumeSource source = new GlusterfsVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setEndpoints("endpoints");
			source.setPath("path");
			source.setReadOnly(true);
			return source;
		}
		
		public HostPathVolumeSource getHostPathVolumeSource() {
			HostPathVolumeSource source = new HostPathVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setPath("path");
			return source;
		}

		public ISCSIVolumeSource getISCSIVolumeSource() {
			ISCSIVolumeSource source = new ISCSIVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setChapAuthDiscovery(true);
			source.setChapAuthSession(true);
			source.setFsType("fsType");
			source.setIqn("iqn");
			source.setIscsiInterface("iscsiInterface");
			source.setLun(10);
			source.setPortals(this.getStringlist());
			source.setReadOnly(true);
			source.setSecretRef(this.getLocalObjectReference());
			source.setTargetPortal("targetPortal");
			return source;
		}
		
		public NFSVolumeSource getNFSVolumeSource() {
			NFSVolumeSource source = new NFSVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setPath("path");
			source.setReadOnly(true);
			source.setServer("server");
			return source;
		}
		
		public PersistentVolumeClaimVolumeSource getPersistentVolumeClaimVolumeSource() {
			PersistentVolumeClaimVolumeSource source = new PersistentVolumeClaimVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setClaimName("claimName");
			source.setReadOnly(true);
			return source;
		}
		
		public PhotonPersistentDiskVolumeSource getPhotonPersistentDiskVolumeSource() {
			PhotonPersistentDiskVolumeSource source = new PhotonPersistentDiskVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setFsType("fsType");
			source.setPdID("pdID");
			return source;
		}
		
		public PortworxVolumeSource getPortworxVolumeSource() {
			PortworxVolumeSource source = new PortworxVolumeSource();
			source.setFsType("fsType");
			source.setVolumeID("volumeID");
			source.setReadOnly(true);
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			return source;
		}
		
		public ConfigMapProjection getConfigMapProjection() {
			ConfigMapProjection config = new ConfigMapProjection();
			config.setAdditionalProperty("property_key1", "property_val1");
			config.setAdditionalProperty("property_key2", "property_val2");
			config.setName("name");
			config.setOptional(true);
			config.setItems(this.getKeyToPath());
			return config;
		}
		
		public SecretProjection getSecretProjection() {
			SecretProjection projection = new SecretProjection();
			projection.setAdditionalProperty("property_key1", "property_val1");
			projection.setAdditionalProperty("property_key2", "property_val2");
			projection.setItems(this.getKeyToPath());
			projection.setName("name");
			projection.setOptional(true);
			return projection;
		}
		
		public ObjectFieldSelector getObjectFieldSelector() {
			ObjectFieldSelector obj = new ObjectFieldSelector();
			obj.setApiVersion("apiVersion");
			obj.setFieldPath("fieldPath");
			obj.setAdditionalProperty("ObjectFieldSelector_key1", "ObjectFieldSelector_val1");
			obj.setAdditionalProperty("ObjectFieldSelector_key2", "ObjectFieldSelector_val2");
			return obj;
		}
		
		
		public DownwardAPIProjection getDownwardAPIProjection() {
			DownwardAPIProjection api = new DownwardAPIProjection();
			api.setAdditionalProperty("property_key1", "property_val1");
			api.setAdditionalProperty("property_key2", "property_val2");
			api.setItems(this.getDownwardAPIVolumeFile());
			return api;
		}
		
		public List<VolumeProjection> getVolumeProjectionList(){
			List<VolumeProjection> list = new ArrayList<>();
			VolumeProjection projection1 = new VolumeProjection();
			projection1.setAdditionalProperty("property_key1", "property_val1");
			projection1.setAdditionalProperty("property_key2", "property_val2");
			projection1.setConfigMap(this.getConfigMapProjection());
			projection1.setSecret(this.getSecretProjection());
			projection1.setDownwardAPI(this.getDownwardAPIProjection());
			
			list.add(projection1);
			VolumeProjection projection2 = new VolumeProjection();
			projection2.setAdditionalProperty("property_key1", "property_val1");
			projection2.setAdditionalProperty("property_key2", "property_val2");
			projection2.setConfigMap(this.getConfigMapProjection());
			projection2.setSecret(this.getSecretProjection());
			projection2.setDownwardAPI(this.getDownwardAPIProjection());
			
			list.add(projection2);
			return list;
		}
		
		public ProjectedVolumeSource getProjectedVolumeSource() {
			ProjectedVolumeSource source = new ProjectedVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setDefaultMode(10);
			source.setSources(this.getVolumeProjectionList());
			return source;
		}
		
		
		public List<Volume> getVolumeList() {
			List<Volume> volumes = new ArrayList<>();
			Volume volue1 = new Volume();
			volue1.setAwsElasticBlockStore(this.getAWSElasticBlockStoreVolumeSource("volue1", 1));
			volue1.setAdditionalProperty("property_key1", "property_val1");
			volue1.setAdditionalProperty("property_key2", "property_val2");
			volue1.setAzureDisk(this.getAzureDiskVolumeSource());
			volue1.setAzureFile(this.getAzureFileVolumeSource());
			volue1.setCephfs(this.getCephFSVolumeSource());
			volue1.setCinder(this.getCinderVolumeSource());
			volue1.setConfigMap(this.getConfigMapVolumeSource());
			volue1.setDownwardAPI(this.getDownwardAPIVolumeSource());
			volue1.setEmptyDir(this.getEmptyDirVolumeSource());
			volue1.setFc(this.getFCVolumeSource());
			volue1.setFlexVolume(this.getFlexVolumeSource());
			volue1.setFlocker(this.getFlockerVolumeSource());
			volue1.setGcePersistentDisk(this.getGCEPersistentDiskVolumeSource());
			volue1.setGitRepo(this.getGitRepoVolumeSource());
			volue1.setGlusterfs(this.getGlusterfsVolumeSource());
			volue1.setHostPath(this.getHostPathVolumeSource());
			volue1.setIscsi(this.getISCSIVolumeSource());
			volue1.setName("name");
			volue1.setNfs(this.getNFSVolumeSource());
			volue1.setPersistentVolumeClaim(this.getPersistentVolumeClaimVolumeSource());
			volue1.setPhotonPersistentDisk(this.getPhotonPersistentDiskVolumeSource());
			volue1.setPortworxVolume(this.getPortworxVolumeSource());
			volue1.setProjected(this.getProjectedVolumeSource());
			volue1.setQuobyte(this.getQuobyteVolumeSource());
			volue1.setRbd(this.getRBDVolumeSource());
			volue1.setScaleIO(this.getScaleIOVolumeSource());
			volue1.setSecret(this.getSecretVolumeSource());
			volue1.setStorageos(this.getStorageOSVolumeSource());
			volue1.setVsphereVolume(this.getVsphereVirtualDiskVolumeSource());
			volumes.add(volue1);
			Volume volue2 = new Volume();
			volue2.setAwsElasticBlockStore(this.getAWSElasticBlockStoreVolumeSource("volue2", 1));
			volue2.setAdditionalProperty("property_key1", "property_val1");
			volue2.setAdditionalProperty("property_key2", "property_val2");
			volue2.setAzureDisk(this.getAzureDiskVolumeSource());
			volue2.setAzureFile(this.getAzureFileVolumeSource());
			volue2.setCephfs(this.getCephFSVolumeSource());
			volue2.setCinder(this.getCinderVolumeSource());
			volue2.setConfigMap(this.getConfigMapVolumeSource());
			volue2.setDownwardAPI(this.getDownwardAPIVolumeSource());
			volue2.setEmptyDir(this.getEmptyDirVolumeSource());
			volue2.setFc(this.getFCVolumeSource());
			volue2.setFlexVolume(this.getFlexVolumeSource());
			volue2.setFlocker(this.getFlockerVolumeSource());
			volue2.setGcePersistentDisk(this.getGCEPersistentDiskVolumeSource());
			volue2.setGitRepo(this.getGitRepoVolumeSource());
			volue2.setGlusterfs(this.getGlusterfsVolumeSource());
			volue2.setHostPath(this.getHostPathVolumeSource());
			volue2.setIscsi(this.getISCSIVolumeSource());
			volue2.setName("name");
			volue2.setNfs(this.getNFSVolumeSource());
			volue2.setPersistentVolumeClaim(this.getPersistentVolumeClaimVolumeSource());
			volue2.setPhotonPersistentDisk(this.getPhotonPersistentDiskVolumeSource());
			volue2.setPortworxVolume(this.getPortworxVolumeSource());
			volue2.setProjected(this.getProjectedVolumeSource());
			volue2.setQuobyte(this.getQuobyteVolumeSource());
			volue2.setRbd(this.getRBDVolumeSource());
			volue2.setScaleIO(this.getScaleIOVolumeSource());
			volue2.setSecret(this.getSecretVolumeSource());
			volue2.setStorageos(this.getStorageOSVolumeSource());
			volue2.setVsphereVolume(this.getVsphereVirtualDiskVolumeSource());
			volumes.add(volue2);
			return volumes;
		}
		
		public VsphereVirtualDiskVolumeSource getVsphereVirtualDiskVolumeSource() {
			VsphereVirtualDiskVolumeSource source = new VsphereVirtualDiskVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setFsType("fsType");
			source.setStoragePolicyID("storagePolicyID");
			source.setStoragePolicyName("storagePolicyName");
			source.setVolumePath("volumePath");
			return source;
		}
		
		public StorageOSVolumeSource getStorageOSVolumeSource() {
			StorageOSVolumeSource source = new StorageOSVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setFsType("fsType");
			source.setReadOnly(true);
			source.setSecretRef(this.getLocalObjectReference());
			source.setVolumeName("volumeName");
			source.setVolumeNamespace("volumeNamespace");
			return source;
		}
		
		
		public SecretVolumeSource getSecretVolumeSource() {
			SecretVolumeSource source = new SecretVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setDefaultMode(10);
			source.setItems(this.getKeyToPath());
			source.setOptional(true);
			source.setSecretName("secretName");
			return source;
		}
		
		public ScaleIOVolumeSource getScaleIOVolumeSource() {
			ScaleIOVolumeSource source = new ScaleIOVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setFsType("fsType");
			source.setGateway("gateway");
			source.setProtectionDomain("protectionDomain");
			source.setReadOnly(true);
			source.setSecretRef(this.getLocalObjectReference());
			source.setSslEnabled(true);
			source.setStorageMode("storageMode");
			source.setStoragePool("storagePool");
			source.setSystem("system");
			source.setVolumeName("volumeName");
			return source;
		}
		
		public RBDVolumeSource getRBDVolumeSource() {
			RBDVolumeSource source = new RBDVolumeSource();
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setFsType("fsType");
			source.setImage("image");
			source.setMonitors(this.getStringlist());
			source.setKeyring("keyring");
			source.setPool("pool");
			source.setReadOnly(true);
			source.setSecretRef(this.getLocalObjectReference());
			source.setUser("user");
			return source;
		}
		
		public QuobyteVolumeSource getQuobyteVolumeSource() {
			QuobyteVolumeSource source = new QuobyteVolumeSource();
			source.setGroup("group");
			source.setAdditionalProperty("property_key1", "property_val1");
			source.setAdditionalProperty("property_key2", "property_val2");
			source.setReadOnly(true);
			source.setRegistry("registry");
			source.setUser("user");
			source.setVolume("volume");
			return source;
		}

		public PodSpec getPodSpec() {
			PodSpec spec = new PodSpec();
			spec.setActiveDeadlineSeconds(10l);
			spec.setAffinity(this.getAffinity());
			spec.setAutomountServiceAccountToken(true);
			spec.setContainers(this.getContainerList());
			spec.setDnsPolicy("dnsPolicy");
			spec.setHostAliases(this.getHostAliasList());
			spec.setHostIPC(true);
			spec.setHostNetwork(true);
			spec.setHostPID(true);
			spec.setHostname("hostname");
			spec.setImagePullSecrets(this.getLocalObjectReferenceList());
			spec.setInitContainers(this.getContainerList());
			spec.setNodeName("nodeName");
			Map<String, String> nodeSelector = new HashMap<>();
			nodeSelector.put("nodeSelector-key1", "nodeSelector-val1");
			nodeSelector.put("nodeSelector-key2", "nodeSelector-val2");
			spec.setNodeSelector(nodeSelector);
			spec.setRestartPolicy("restartPolicy");
			spec.setSchedulerName("schedulerName");
			spec.setSecurityContext(this.getPodSecurityContext());
			spec.setServiceAccount("serviceAccount");
			spec.setServiceAccountName("serviceAccountName");
			spec.setSubdomain("subdomain");
			spec.setTerminationGracePeriodSeconds(10L);
			spec.setTolerations(this.getTolerationList());
			spec.setVolumes(this.getVolumeList());
			spec.setAdditionalProperty("PodSpec-key1", "PodSpec-val1");
			spec.setAdditionalProperty("PodSpec-key2", "PodSpec-val2");
			return spec;
		}

		//过
		private PodTemplateSpec getPodTemplateSpec() {
			PodTemplateSpec template = new PodTemplateSpec();
			template.setMetadata(this.getObjectMeta());
			template.setSpec(this.getPodSpec());
			template.setAdditionalProperty("PodTemplateSpec-key1", "PodTemplateSpec-val1");
			template.setAdditionalProperty("PodTemplateSpec-key2", "PodTemplateSpec-val2");
			return template;
		}

		//过
		private ReplicationControllerSpec getReplicationControllerSpec() {
			ReplicationControllerSpec spec = new ReplicationControllerSpec();
			spec.setMinReadySeconds(10);
			spec.setReplicas(10);
			Map<String, String> selector = new HashMap<>();
			selector.put("ReplicationControllerSpecMAP-key1", "ReplicationControllerSpecMAP-val1");
			selector.put("ReplicationControllerSpecMAP-key2", "ReplicationControllerSpecMAP-val2");
			spec.setSelector(selector);
			spec.setAdditionalProperty("ReplicationControllerSpec-key1", "ReplicationControllerSpec-val1");
			spec.setAdditionalProperty("ReplicationControllerSpec-key2", "ReplicationControllerSpec-val2");
			spec.setTemplate(this.getPodTemplateSpec());
			return spec;
		}

		private List<ReplicationControllerCondition> getReplicationControllerConditionList(){
			List<ReplicationControllerCondition> list = new ArrayList<>();
			ReplicationControllerCondition condition = new ReplicationControllerCondition();
			condition.setAdditionalProperty("ReplicationControllerConditionlist1-key1", "ReplicationControllerConditionlist1-val1");
			condition.setAdditionalProperty("ReplicationControllerConditionlist1-key2", "ReplicationControllerConditionlist1-val1");
			condition.setLastTransitionTime("lastTransitionTime1");
			condition.setMessage("message1");
			condition.setReason("reason1");
			condition.setStatus("status1");
			condition.setType("type1");
			list.add(condition);
			condition = new ReplicationControllerCondition();
			condition.setAdditionalProperty("ReplicationControllerConditionlist2-key1", "ReplicationControllerConditionlist2-val1");
			condition.setAdditionalProperty("ReplicationControllerConditionlist2-key2", "ReplicationControllerConditionlist2-val1");
			condition.setLastTransitionTime("lastTransitionTime2");
			condition.setMessage("message2");
			condition.setReason("reason2");
			condition.setStatus("status2");
			condition.setType("type2");
			list.add(condition);
			return list;
		}
		
		private ReplicationControllerStatus getReplicationControllerStatus() {
			ReplicationControllerStatus status = new ReplicationControllerStatus();
			status.setAdditionalProperty("ReplicationControllerStatus-key1", "ReplicationControllerStatus-val1");
			status.setAdditionalProperty("ReplicationControllerStatus-key2", "ReplicationControllerStatus-val2");
			status.setAvailableReplicas(10);
			status.setConditions(this.getReplicationControllerConditionList());
			status.setFullyLabeledReplicas(10);
			status.setObservedGeneration(10l);
			status.setReadyReplicas(10);
			status.setReplicas(10);
			return status;
		}

		/**
		 * TODO ReplicationController
		 * @return
		 */
		public ReplicationController getReplicationController() {
			ReplicationController rc = new ReplicationController();
			rc.setMetadata(this.getObjectMeta());
			rc.setSpec(this.getReplicationControllerSpec());
			rc.setStatus(this.getReplicationControllerStatus());
			rc.setAdditionalProperty("ReplicationController-key1", "ReplicationController-val1");
			rc.setAdditionalProperty("ReplicationController-key2", "ReplicationController-val2");
			return rc;
		}

}
