package com.kayak.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDNSSLBSubDomainsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDNSSLBSubDomainsResponse;
import com.aliyuncs.alidns.model.v20150109.SetDNSSLBStatusRequest;
import com.aliyuncs.alidns.model.v20150109.SetDNSSLBStatusResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDNSSLBWeightRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDNSSLBWeightResponse;
import com.aliyuncs.exceptions.ClientException;
import com.kayak.utils.AliyunUtils;

/**
 * 
 * @author beacon
 *
 */

@RestController
public class AliyunDNSSLBStatusController {
	
	private final Logger log = LoggerFactory.getLogger(AliyunDNSSLBStatusController.class);
	@Autowired
	private IAcsClient iacsClient;
	@Autowired
	private SetDNSSLBStatusRequest setDNSSLBStatus;
	@Autowired
	private DescribeDNSSLBSubDomainsRequest describeDNSSLBSubDomains;
	@Autowired
	private UpdateDNSSLBWeightRequest updateDNSSLBWeight;
	
	/**
	 * Action SetDNSSLBStatus 开启/关闭解析负载均衡
	 * @param subDomain 需要负载均衡的子域名，其中aliyun.com为错误参数，请使用@.aliyun.com(必输)
	 * @param open true=开启，false=关闭，默认为ture
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return RecordCount 符合规则的A记录数量	
	 * @return Open 解析负载均衡的最新状态
	 * @throws ClientException 
	 */
	@CrossOrigin
	@RequestMapping(value = "setDNSSLBStatus")
	public SetDNSSLBStatusResponse setDNSSLBStatus(String subDomain,Boolean open) throws ClientException {
		SetDNSSLBStatusResponse response = null;
		try {
			this.setDNSSLBStatus.setSubDomain(subDomain);
			if(AliyunUtils.canSet(open)) {
				this.setDNSSLBStatus.setOpen(open);
			}
			response = iacsClient.getAcsResponse(this.setDNSSLBStatus);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action DescribeDNSSLBSubDomains 获取解析负载均衡的子域名列表
	 * @param DomainName 域名名称	
	 * @param pageNumber 当前页数，起始值为1，默认为1
	 * @param pageSize 分页查询时设置的每页行数，最大值100，默认为20
	 * @return RequestId 唯一请求识别码
	 * @return TotalCount 域名分组数量
	 * @return PageNumber 当前页码
	 * @return PageSize 本次查询获取的子域名数量
	 * @return SlbSubDomains 子域名负载均衡结构列表(object)
	 *  -SubDomain 子域名名称
	 *  -RecordCount 解析记录数量
	 *  -Open 解析负载均衡状态
	 */
	@CrossOrigin
	@RequestMapping(value = "describeDNSSLBSubDomains")
	public DescribeDNSSLBSubDomainsResponse describeDNSSLBSubDomains(String domainName,Long pageNumber,Long pageSize) {
		DescribeDNSSLBSubDomainsResponse response = null;
		try {
			this.describeDNSSLBSubDomains.setDomainName(domainName);
			if(AliyunUtils.canSet(pageNumber)) {
				this.describeDNSSLBSubDomains.setPageNumber(pageNumber);
			}
			if(AliyunUtils.canSet(pageSize)) {
				this.describeDNSSLBSubDomains.setPageSize(pageSize);
			}
			response = iacsClient.getAcsResponse(this.describeDNSSLBSubDomains);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
		}
		return response;
	}
	
	/**
	 * Action 操作接口名，系统规定参数，取值：UpdateDNSSLBWeight
	 * @param recordId 解析记录ID（必输）
	 * @param weight 要修改的权重值[1-100]（必输）
	 * @return RequestId 唯一请求识别码
	 * @return RecordId 解析记录ID
	 * @return Weight 修改后的权重值
	 */
	@CrossOrigin
	@RequestMapping(value = "updateDNSSLBWeight")
	public UpdateDNSSLBWeightResponse updateDNSSLBWeight(String recordId,Integer weight) {
		UpdateDNSSLBWeightResponse response = null;
		try {
			this.updateDNSSLBWeight.setRecordId(recordId);
			this.updateDNSSLBWeight.setWeight(weight);
			response = iacsClient.getAcsResponse(this.updateDNSSLBWeight);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
		}
		return response;
	}

}
