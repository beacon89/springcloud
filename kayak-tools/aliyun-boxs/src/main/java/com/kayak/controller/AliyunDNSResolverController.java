package com.kayak.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.ChangeDomainOfDnsProductRequest;
import com.aliyuncs.alidns.model.v20150109.ChangeDomainOfDnsProductResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDnsProductInstancesRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDnsProductInstancesResponse;
import com.aliyuncs.exceptions.ClientException;
import com.kayak.utils.AliyunUtils;

/**
 * 
 * @author beacon
 *
 */
@RestController
public class AliyunDNSResolverController {

private final Logger log = LoggerFactory.getLogger(AliyunDNSResolverController.class);
	
	@Autowired
	private IAcsClient iacsClient;
	
	@Autowired
	private DescribeDnsProductInstancesRequest describeDnsProductInstancesRequest;
	
	@Autowired
	private ChangeDomainOfDnsProductRequest changeDomainOfDnsProduct;
	
	/**
	 * Action describeDnsProductInstances 获取云解析收费版本产品列表
	 * @param pageNumber 当前页数，起始值为1，默认为1(默认在文件中配置)
	 * @param pageSize 分页查询时设置的每页行数，最大值100，默认为20(默认在文件中配置)
	 * @return RequestId 唯一请求识别码
	 * @return TotalCount 域名列表总数
	 * @return PageNumber 当前页码
	 * @return PageSize 本次查询获取的域名数量
	 * @return DnsProducts 	本次获取的云解析产品列表<List object>
	 * -InstanceId 少中文注释
	 * -StartTime
	 * -EndTime
	 * -Domain
	 * -BindCount
	 * -BindUsedCount
	 * -TTLMinValue
	 * -SubDomainLevel
	 * -DnsSLBCount
	 * -URLForwardCount
	 * -DDosDefendFlow
	 * -DDosDefendQuery
	 * -OverseaDDosDefendFlow
	 * -SearchEngineLines
	 * -ISPLines
	 * -ISPRegionLines
	 * -OverseaLine
	 * TODO 过
	 * @throws ClientException 
	 */
	@RequestMapping(value = "describeDnsProductInstances")
	public DescribeDnsProductInstancesResponse describeDnsProductInstances(Long pageNumber,Long pageSize) throws ClientException {
		DescribeDnsProductInstancesResponse response = null;
		try {
			if(AliyunUtils.canSet(pageNumber)) {
				this.describeDnsProductInstancesRequest.setPageNumber(pageNumber);
			}
			if(AliyunUtils.canSet(pageSize)) {
				this.describeDnsProductInstancesRequest.setPageSize(pageSize);
			}
			response = iacsClient.getAcsResponse(this.describeDnsProductInstancesRequest);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action ChangeDomainOfDnsProduct 更换云解析产品绑定的域名
	 * @param instanceId 云解析产品ID
	 * @param newDomain 目标绑定域名，如不填写，则为解绑
	 * @return RequestId 唯一请求识别码
	 * @return OriginalDomain 原始绑定的域名，如果为空，则为第一次绑定
	 * @throws ClientException 
	 */
	@RequestMapping(value = "changeDomainOfDnsProduct")
	public ChangeDomainOfDnsProductResponse changeDomainOfDnsProduct(String instanceId,String newDomain) throws ClientException {
		ChangeDomainOfDnsProductResponse response = null;
		try {
			if(AliyunUtils.canSet(instanceId)) {
				this.changeDomainOfDnsProduct.setInstanceId(instanceId);
			}
			if(AliyunUtils.canSet(newDomain)) {
				this.changeDomainOfDnsProduct.setNewDomain(newDomain);
			}
			response = iacsClient.getAcsResponse(this.changeDomainOfDnsProduct);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	
	
	
}
