package com.kayak.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.MethodType;


@ConfigurationProperties(prefix = "alidns")
public class AliyunDnsPropertie {
	
	@Value("${alidns.regionid}")
	private String regionid;
	
	@Value("${alidns.product}")
	private String product;
	
	@Value("${alidns.domain}")
	private String domain;
	
	@Value("${alidns.accesskeyid}")
	private String accesskeyid;
	
	@Value("${alidns.accesskeysecret}")
	private String accesskeysecret;
	
	@Value("${alidns.protocoltype}")
	private String protocoltype;
	
	@Value("${alidns.formattype}")
	private String formattype;
	
	@Value("${alidns.methodtype}")
	private String methodtype;
	
	@Value("${alidns.connectTimeout}")
	private Integer connectTimeout;
	
	@Value("${alidns.readTimeout}")
	private Integer readTimeout;
	
	@Value("${alidns.encoding}")
	private String encoding;
	
	@Value("${alidns.pageNumber}")
	private Long pageNumber;
	
	@Value("${alidns.pageSize}")
	private Long pageSize;
	
	@Value("${alidns.defaultdomain}")
	private String defaultdomain;
	
	private MethodType methodTypeFormat(String methodtype) {
		if(MethodType.POST.toString().equals(methodtype)) {
			return MethodType.POST;
		}else if(MethodType.OPTIONS.toString().equals(methodtype)) {
			return MethodType.OPTIONS;
		}else if(MethodType.DELETE.toString().equals(methodtype)) {
			return MethodType.DELETE;
		}else if(MethodType.HEAD.toString().equals(methodtype)) {
			return MethodType.HEAD;
		}else if(MethodType.PUT.toString().equals(methodtype)) {
			return MethodType.PUT;
		}else {
			return MethodType.GET;
		}
	}
	
	public AddDomainRequest getAddDomainRequest() {
		AddDomainRequest request = new AddDomainRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public DescribeDomainInfoRequest getDescribeDomainInfoRequest() {
		DescribeDomainInfoRequest request = new DescribeDomainInfoRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public DeleteDomainRequest getDeleteDomainRequest() {
		DeleteDomainRequest request = new DeleteDomainRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public DescribeDomainsRequest getDescribeDomainsRequest() {
		DescribeDomainsRequest request = new DescribeDomainsRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		if(null != pageNumber && !"".equals(pageNumber.toString())) {
			request.setPageNumber(pageNumber);
		}
		if(null != pageSize && !"".equals(pageSize.toString())) {
			request.setPageSize(pageSize);
		}
		return request;
	}
	
	public DescribeDomainWhoisInfoRequest getDescribeDomainWhoisInfoRequest() {
		DescribeDomainWhoisInfoRequest request = new DescribeDomainWhoisInfoRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public ModifyHichinaDomainDNSRequest getModifyHichinaDomainDNSRequest() {
		ModifyHichinaDomainDNSRequest request = new ModifyHichinaDomainDNSRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public GetMainDomainNameRequest getGetMainDomainNameRequest() {
		GetMainDomainNameRequest request = new GetMainDomainNameRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public DescribeDomainLogsRequest getDescribeDomainLogsRequest() {
		DescribeDomainLogsRequest request = new DescribeDomainLogsRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public DescribeDnsProductInstancesRequest getDescribeDnsProductInstancesRequest() {
		DescribeDnsProductInstancesRequest request = new DescribeDnsProductInstancesRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		if(null != pageNumber && !"".equals(pageNumber.toString())) {
			request.setPageNumber(pageNumber);
		}
		if(null != pageSize && !"".equals(pageSize.toString())) {
			request.setPageSize(pageSize);
		}
		return request;
	}
	
	public ChangeDomainOfDnsProductRequest getChangeDomainOfDnsProductRequest() {
		ChangeDomainOfDnsProductRequest request = new ChangeDomainOfDnsProductRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	
	public AddDomainGroupRequest getAddDomainGroupRequest() {
		AddDomainGroupRequest request = new AddDomainGroupRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public UpdateDomainGroupRequest getUpdateDomainGroupRequest() {
		UpdateDomainGroupRequest request = new UpdateDomainGroupRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public DeleteDomainGroupRequest getDeleteDomainGroupRequest() {
		DeleteDomainGroupRequest request = new DeleteDomainGroupRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public ChangeDomainGroupRequest getChangeDomainGroupRequest() {
		ChangeDomainGroupRequest request = new ChangeDomainGroupRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public DescribeDomainGroupsRequest getDescribeDomainGroupsRequest() {
		DescribeDomainGroupsRequest request = new DescribeDomainGroupsRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public RetrievalDomainNameRequest getRetrievalDomainNameRequest() {
		RetrievalDomainNameRequest request = new RetrievalDomainNameRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public ApplyForRetrievalDomainNameRequest getApplyForRetrievalDomainNameRequest() {
		ApplyForRetrievalDomainNameRequest request = new ApplyForRetrievalDomainNameRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public CheckDomainRecordRequest getCheckDomainRecordRequest() {
		CheckDomainRecordRequest request = new CheckDomainRecordRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public AddDomainRecordRequest getAddDomainRecordRequest() {
		AddDomainRecordRequest request = new AddDomainRecordRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		if(null != this.defaultdomain && !"".equals(this.defaultdomain)) {
			request.setDomainName(this.defaultdomain);
		}
		return request;
	}
	
	public DeleteDomainRecordRequest getDeleteDomainRecordRequest() {
		DeleteDomainRecordRequest request = new DeleteDomainRecordRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public UpdateDomainRecordRequest getUpdateDomainRecordRequest() {
		UpdateDomainRecordRequest request = new UpdateDomainRecordRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	
	public SetDomainRecordStatusRequest getSetDomainRecordStatusRequest() {
		SetDomainRecordStatusRequest request = new SetDomainRecordStatusRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public DescribeDomainRecordsRequest getDescribeDomainRecordsRequest() {
		DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		if(null != this.defaultdomain && !"".equals(this.defaultdomain)) {
			request.setDomainName(this.defaultdomain);
		}
		return request;
	}
	
	public DescribeDomainRecordInfoRequest getDescribeDomainRecordInfoRequest() {
		DescribeDomainRecordInfoRequest request = new DescribeDomainRecordInfoRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public DescribeSubDomainRecordsRequest getDescribeSubDomainRecordsRequest() {
		DescribeSubDomainRecordsRequest request = new DescribeSubDomainRecordsRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public DeleteSubDomainRecordsRequest getDeleteSubDomainRecordsRequest() {
		DeleteSubDomainRecordsRequest request = new DeleteSubDomainRecordsRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public DescribeRecordLogsRequest getDescribeRecordLogsRequest() {
		DescribeRecordLogsRequest request = new DescribeRecordLogsRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		if(null != this.defaultdomain && !"".equals(this.defaultdomain)) {
			request.setDomainName(this.defaultdomain);
		}
		return request;
	}
	
	public DeleteBatchDomainsRequest getDeleteBatchDomainsRequest() {
		DeleteBatchDomainsRequest request = new DeleteBatchDomainsRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public AddBatchDomainRecordsRequest getAddBatchDomainRecordsRequest() {
		AddBatchDomainRecordsRequest request = new AddBatchDomainRecordsRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public UpdateBatchDomainRecordsRequest getUpdateBatchDomainRecordsRequest() {
		UpdateBatchDomainRecordsRequest request = new UpdateBatchDomainRecordsRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public DeleteBatchDomainRecordsRequest getDeleteBatchDomainRecordsRequest() {
		DeleteBatchDomainRecordsRequest request = new DeleteBatchDomainRecordsRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		if (null != this.methodtype && !"".equals(this.methodtype)) {
			request.setMethod(this.methodTypeFormat(this.methodtype));
		}
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public DescribeBatchResultRequest getDescribeBatchResultRequest() {
		DescribeBatchResultRequest request = new DescribeBatchResultRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		request.setMethod(MethodType.GET);
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public SetDNSSLBStatusRequest getSetDNSSLBStatusRequest() {
		SetDNSSLBStatusRequest request = new SetDNSSLBStatusRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		request.setMethod(MethodType.GET);
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public DescribeDNSSLBSubDomainsRequest getDescribeDNSSLBSubDomainsRequest() {
		DescribeDNSSLBSubDomainsRequest request = new DescribeDNSSLBSubDomainsRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		request.setMethod(MethodType.GET);
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	public UpdateDNSSLBWeightRequest getUpdateDNSSLBWeightRequest() {
		UpdateDNSSLBWeightRequest request = new UpdateDNSSLBWeightRequest();
		if (null != this.formattype && !"".equals(this.formattype)) {
			request.setAcceptFormat(FormatType.mapAcceptToFormat(this.formattype));
		}
		request.setMethod(MethodType.GET);
		if (null != connectTimeout && !"".equals(connectTimeout.toString())) {
			request.setConnectTimeout(connectTimeout);
		}
		if (null != readTimeout && !"".equals(readTimeout.toString())) {
			request.setReadTimeout(readTimeout);
		}
		if (null != encoding && !"".equals(encoding)) {
			request.setEncoding(encoding);
		}
		return request;
	}
	
	
	
	
	public String getRegionid() {
		return regionid;
	}

	public void setRegionid(String regionid) {
		this.regionid = regionid;
	}

	public String getAccesskeysecret() {
		return accesskeysecret;
	}

	public void setAccesskeysecret(String accesskeysecret) {
		this.accesskeysecret = accesskeysecret;
	}

	public String getProtocoltype() {
		return protocoltype;
	}

	public void setProtocoltype(String protocoltype) {
		this.protocoltype = protocoltype;
	}

	public String getFormattype() {
		return formattype;
	}

	public void setFormattype(String formattype) {
		this.formattype = formattype;
	}

	public String getMethodtype() {
		return methodtype;
	}

	public void setMethodtype(String methodtype) {
		this.methodtype = methodtype;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}


	public String getAccesskeyid() {
		return accesskeyid;
	}

	public void setAccesskeyid(String accesskeyid) {
		this.accesskeyid = accesskeyid;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public Integer getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Long getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Long pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Long getPageSize() {
		return pageSize;
	}

	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	public String getDefaultdomain() {
		return defaultdomain;
	}

	public void setDefaultdomain(String defaultdomain) {
		this.defaultdomain = defaultdomain;
	}

}
