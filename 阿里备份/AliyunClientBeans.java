package com.kayak.ailiyun.beans;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.AddBatchDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.AddDomainGroupRequest;
import com.aliyuncs.alidns.model.v20150109.AddDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.AddDomainRequest;
import com.aliyuncs.alidns.model.v20150109.ApplyForRetrievalDomainNameRequest;
import com.aliyuncs.alidns.model.v20150109.ChangeDomainGroupRequest;
import com.aliyuncs.alidns.model.v20150109.ChangeDomainOfDnsProductRequest;
import com.aliyuncs.alidns.model.v20150109.CheckDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.DeleteBatchDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DeleteBatchDomainsRequest;
import com.aliyuncs.alidns.model.v20150109.DeleteDomainGroupRequest;
import com.aliyuncs.alidns.model.v20150109.DeleteDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.DeleteDomainRequest;
import com.aliyuncs.alidns.model.v20150109.DeleteSubDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeBatchResultRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDNSSLBSubDomainsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDnsProductInstancesRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainGroupsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainInfoRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainLogsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordInfoRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainWhoisInfoRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeRecordLogsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeSubDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.GetMainDomainNameRequest;
import com.aliyuncs.alidns.model.v20150109.ModifyHichinaDomainDNSRequest;
import com.aliyuncs.alidns.model.v20150109.RetrievalDomainNameRequest;
import com.aliyuncs.alidns.model.v20150109.SetDNSSLBStatusRequest;
import com.aliyuncs.alidns.model.v20150109.SetDomainRecordStatusRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateBatchDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDNSSLBWeightRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainGroupRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.kayak.aliyun.properties.AliyunDnsPropertie;

@Configuration
public class AliyunClientBeans {

	@Resource
	private AliyunDnsPropertie aliyunDnsPropertie;

	@Bean
	public IAcsClient getIAcsClient() throws ClientException {
		IClientProfile profile = DefaultProfile.getProfile(aliyunDnsPropertie.getRegionid(),
				aliyunDnsPropertie.getAccesskeyid(), aliyunDnsPropertie.getAccesskeysecret());
		DefaultProfile.addEndpoint(aliyunDnsPropertie.getRegionid(), aliyunDnsPropertie.getRegionid(),
				aliyunDnsPropertie.getProduct(), aliyunDnsPropertie.getDomain());
		return new DefaultAcsClient(profile);
	}
	
	@Bean
	public AddDomainRequest getAddDomainRequest() {
		return aliyunDnsPropertie.getAddDomainRequest();
	}
	
	@Bean
	public DescribeDomainInfoRequest getDescribeDomainInfoRequest() {
		return aliyunDnsPropertie.getDescribeDomainInfoRequest();
	}
	
	
	@Bean
	public DeleteDomainRequest getDeleteDomainRequest() {
		return this.aliyunDnsPropertie.getDeleteDomainRequest();
	}
	

	@Bean
	public DescribeDomainsRequest getDescribeDomainsRequest() throws Exception {
		return this.aliyunDnsPropertie.getDescribeDomainsRequest();
	}
	
	@Bean
	public DescribeDomainWhoisInfoRequest getDescribeDomainWhoisInfoRequest() {
		return this.aliyunDnsPropertie.getDescribeDomainWhoisInfoRequest();
	}
	
	@Bean
	public ModifyHichinaDomainDNSRequest getModifyHichinaDomainDNSRequest() {
		return this.aliyunDnsPropertie.getModifyHichinaDomainDNSRequest();
	}
	
	@Bean
	public GetMainDomainNameRequest getGetMainDomainNameRequest() {
		return this.aliyunDnsPropertie.getGetMainDomainNameRequest();
	}
	
	@Bean
	public DescribeDomainLogsRequest getDescribeDomainLogsRequest() {
		return this.aliyunDnsPropertie.getDescribeDomainLogsRequest();
	}
	
	@Bean
	public DescribeDnsProductInstancesRequest getDescribeDnsProductInstancesRequest() {
		return this.aliyunDnsPropertie.getDescribeDnsProductInstancesRequest();
	}
	
	@Bean
	public ChangeDomainOfDnsProductRequest getChangeDomainOfDnsProductRequest() {
		return this.aliyunDnsPropertie.getChangeDomainOfDnsProductRequest();	
	}
	
	@Bean
	public AddDomainGroupRequest getAddDomainGroupRequest() {
		return this.aliyunDnsPropertie.getAddDomainGroupRequest();
	}
	
	@Bean
	public UpdateDomainGroupRequest getUpdateDomainGroupRequest() {
		return this.aliyunDnsPropertie.getUpdateDomainGroupRequest();
	}
	
	@Bean
	public DeleteDomainGroupRequest getDeleteDomainGroupRequest() {
		return this.aliyunDnsPropertie.getDeleteDomainGroupRequest();
	}
	
	@Bean
	public ChangeDomainGroupRequest getChangeDomainGroupRequest() {
		return this.aliyunDnsPropertie.getChangeDomainGroupRequest();
	}
	
	@Bean
	public DescribeDomainGroupsRequest getDescribeDomainGroupsRequest() {
		return this.aliyunDnsPropertie.getDescribeDomainGroupsRequest();
	}
	
	@Bean
	public RetrievalDomainNameRequest getRetrievalDomainNameRequest() {
		return this.aliyunDnsPropertie.getRetrievalDomainNameRequest();
	}
	
	@Bean
	public ApplyForRetrievalDomainNameRequest getApplyForRetrievalDomainNameRequest() {
		return this.aliyunDnsPropertie.getApplyForRetrievalDomainNameRequest();
	}
	
	@Bean
	public CheckDomainRecordRequest getCheckDomainRecordRequest() {
		return this.aliyunDnsPropertie.getCheckDomainRecordRequest();
	}
	
	@Bean
	public AddDomainRecordRequest getAddDomainRecordRequest() {
		return this.aliyunDnsPropertie.getAddDomainRecordRequest();
	}
	
	@Bean
	public DeleteDomainRecordRequest getDeleteDomainRecordRequest() {
		return this.aliyunDnsPropertie.getDeleteDomainRecordRequest();
	}
	
	@Bean
	public UpdateDomainRecordRequest getUpdateDomainRecordRequest() {
		return this.aliyunDnsPropertie.getUpdateDomainRecordRequest();
	}
	
	@Bean
	public SetDomainRecordStatusRequest getSetDomainRecordStatusRequest() {
		return this.aliyunDnsPropertie.getSetDomainRecordStatusRequest();
	}
	
	@Bean
	public DescribeDomainRecordsRequest getDescribeDomainRecordsRequest() {
		return this.aliyunDnsPropertie.getDescribeDomainRecordsRequest();
	}
	
	@Bean
	public DescribeDomainRecordInfoRequest getDescribeDomainRecordInfoRequest() {
		return this.aliyunDnsPropertie.getDescribeDomainRecordInfoRequest();
	}
	
	@Bean
	public DescribeSubDomainRecordsRequest getDescribeSubDomainRecordsRequest() {
		return this.aliyunDnsPropertie.getDescribeSubDomainRecordsRequest();
	}
	
	@Bean
	public DeleteSubDomainRecordsRequest getDeleteSubDomainRecordsRequest() {
		return this.aliyunDnsPropertie.getDeleteSubDomainRecordsRequest();
	}
	
	@Bean
	public DescribeRecordLogsRequest getDescribeRecordLogsRequest() {
		return this.aliyunDnsPropertie.getDescribeRecordLogsRequest();
	}
	
	@Bean
	public DeleteBatchDomainsRequest getDeleteBatchDomainsRequest() {
		return this.aliyunDnsPropertie.getDeleteBatchDomainsRequest();
	}
	
	@Bean
	public AddBatchDomainRecordsRequest getAddBatchDomainRecordsRequest() {
		return this.aliyunDnsPropertie.getAddBatchDomainRecordsRequest();
	}
	
	@Bean
	public UpdateBatchDomainRecordsRequest getUpdateBatchDomainRecordsRequest() {
		return this.aliyunDnsPropertie.getUpdateBatchDomainRecordsRequest();	
	}
	
	@Bean
	public DeleteBatchDomainRecordsRequest getDeleteBatchDomainRecordsRequest() {
		return this.aliyunDnsPropertie.getDeleteBatchDomainRecordsRequest();
	}
	
	@Bean
	public DescribeBatchResultRequest getDescribeBatchResultRequest() {
		return this.aliyunDnsPropertie.getDescribeBatchResultRequest();
	}
	
	@Bean
	public SetDNSSLBStatusRequest getSetDNSSLBStatusRequest() {
		return this.aliyunDnsPropertie.getSetDNSSLBStatusRequest();
	}
	
	@Bean
	public DescribeDNSSLBSubDomainsRequest getDescribeDNSSLBSubDomainsRequest() {
		return this.aliyunDnsPropertie.getDescribeDNSSLBSubDomainsRequest();
	}
	
	@Bean
	public UpdateDNSSLBWeightRequest getUpdateDNSSLBWeightRequest() {
		return this.aliyunDnsPropertie.getUpdateDNSSLBWeightRequest();
	}
	
	
	
}
