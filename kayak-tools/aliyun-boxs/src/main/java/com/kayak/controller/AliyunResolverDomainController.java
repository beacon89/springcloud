package com.kayak.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.ApplyForRetrievalDomainNameRequest;
import com.aliyuncs.alidns.model.v20150109.ApplyForRetrievalDomainNameResponse;
import com.aliyuncs.alidns.model.v20150109.CheckDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.CheckDomainRecordResponse;
import com.aliyuncs.alidns.model.v20150109.RetrievalDomainNameRequest;
import com.aliyuncs.alidns.model.v20150109.RetrievalDomainNameResponse;
import com.aliyuncs.exceptions.ClientException;

/**
 * 
 * @author beacon
 *
 */
@RestController
public class AliyunResolverDomainController {
	
	private final Logger log = LoggerFactory.getLogger(AliyunDNSDomainController.class);
	@Autowired
	private IAcsClient iacsClient;
	@Autowired
	private RetrievalDomainNameRequest retrievalDomainName;
	@Autowired
	private ApplyForRetrievalDomainNameRequest applyForRetrievalDomainName;
	@Autowired
	private CheckDomainRecordRequest checkDomainRecord;
	
	/**
	 * Action RetrievalDomainName 发起找回域名
	 * @param domainName 域名名称
	 * @return RequestId 唯一请求识别码
	 * @return DomainName 域名名称
	 * @return WhoisEmail 接收验证码的whois邮箱
	 */
	@RequestMapping(value = "retrievalDomainName")
	public RetrievalDomainNameResponse retrievalDomainName(String domainName) {
		RetrievalDomainNameResponse response = null;
		try {
			this.retrievalDomainName.setDomainName(domainName);
			response = this.iacsClient.getAcsResponse(this.retrievalDomainName);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
		}
		return response;
	}
	
	/**
	 * Action ApplyForRetrievalDomainName 申请由管理员找回
	 * @param domainName 域名名称
	 * @return RequestId 唯一请求识别码
	 * @return DomainName 域名名称
	 */
	@RequestMapping(value="applyForRetrievalDomainName")
	public ApplyForRetrievalDomainNameResponse applyForRetrievalDomainName(String domainName) {
		ApplyForRetrievalDomainNameResponse response = null;
		try {
			this.applyForRetrievalDomainName.setDomainName(domainName);
			response = this.iacsClient.getAcsResponse(this.applyForRetrievalDomainName);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
		}
		return response;
	}
	
	/**
	 * 
	 * @param domainName 域名名称
	 * @param type 记录类型
	 * @param rr 主机记录	
	 * @param value 记录值
	 * @return RequestId 唯一请求识别码
	 * @return IsExist 是否存在解析记录。true为存在，false为不存在。	
	 */
	@RequestMapping(value="checkDomainRecord")
	public CheckDomainRecordResponse checkDomainRecord(String domainName,String type,String rr,String value) {
		CheckDomainRecordResponse response = null;
		try {
			this.checkDomainRecord.setDomainName(domainName);
			this.checkDomainRecord.setType(type);
			this.checkDomainRecord.setRR(rr);
			this.checkDomainRecord.setValue(value);
			response = this.iacsClient.getAcsResponse(this.checkDomainRecord);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
		}
		return response;
	}
	
	
}
