package com.kayak.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.AddDomainRequest;
import com.aliyuncs.alidns.model.v20150109.AddDomainResponse;
import com.aliyuncs.alidns.model.v20150109.DeleteDomainRequest;
import com.aliyuncs.alidns.model.v20150109.DeleteDomainResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainInfoRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainInfoResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainLogsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainLogsResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainWhoisInfoRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainWhoisInfoResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainsResponse;
import com.aliyuncs.alidns.model.v20150109.GetMainDomainNameRequest;
import com.aliyuncs.alidns.model.v20150109.GetMainDomainNameResponse;
import com.aliyuncs.alidns.model.v20150109.ModifyHichinaDomainDNSRequest;
import com.aliyuncs.alidns.model.v20150109.ModifyHichinaDomainDNSResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.kayak.utils.AliyunUtils;


/**
 * 
 * @author beacon
 * 域名管理接口
 * @version 1.0
 */
@RestController
public class AliyunDNSDomainController {

	private final Logger log = LoggerFactory.getLogger(AliyunDNSDomainController.class);
	
	@Autowired
	private IAcsClient iacsClient;
	
	@Autowired
	private AddDomainRequest addDomain;
	
	@Autowired
	private DeleteDomainRequest deleteDomain;
	
	@Autowired
	private DescribeDomainsRequest describeDomains;
	
	@Autowired
	private DescribeDomainInfoRequest describeDomainInfo;
	
	@Autowired
	private DescribeDomainWhoisInfoRequest describeDomainWhoIsInfo;
	
	@Autowired
	private ModifyHichinaDomainDNSRequest modifyhichinaDomainDns;
	
	@Autowired
	private GetMainDomainNameRequest getMainDomainName;
	
	@Autowired
	private DescribeDomainLogsRequest describeDomainLogs;
	
	/**
	 * Action AddDomain 添加域名
	 * @param domainName 域名名称(必输)
	 * @param groupId 域名分组，默认为“默认分组”的GroupId(非必输)
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return DomainId	域名ID
	 * @return DomainName 域名名称
	 * @return GroupId 域名分组ID
	 * @return GroupName 域名分组名称
	 * @throws ClientException 
	 * @PunyCode 只针对中文域名返回punycode码
	 * @DnsServers	域名在解析系统中的DNS列表
	 *  - DnsServer DNS服务器名称，如dns1.hichina.com
	 * TODO 过
	 */
	@RequestMapping(value = "addDomain")
	public AddDomainResponse addDomain(String domainName,String groupId) throws ClientException {
		AddDomainResponse response = null;
		try {
			this.addDomain.setDomainName(domainName);
			if(AliyunUtils.canSet(groupId)) {
				this.addDomain.setGroupId(groupId);
			}
			response = iacsClient.getAcsResponse(this.addDomain);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action DeleteDomain 删除域名
	 * @param domainName 域名名称(必输)
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return DomainName 域名名称
	 * TDOO 过
	 * @throws ClientException 
	 */
	@RequestMapping(value = "deleteDomain")
	public DeleteDomainResponse delDomain(String domainName) throws ClientException {
		DeleteDomainResponse response = null;
		try {
			this.deleteDomain.setDomainName(domainName);
			response = this.iacsClient.getAcsResponse(this.deleteDomain);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	

	/**
	 * Action DescribeDomains 获取域名列表
	 * @param PageNumber 当前页数，起始值为1，默认为1(系统中设置，看参数文件)
	 * @param PageSize 分页查询时设置的每页行数，最大值100，默认为20(系统中设置，看参数文件)
	 * @param KeyWord 关键字，按照”%KeyWord%”模式搜索，不区分大小写(非必输)
	 * @param GroupId 域名分组ID，如果不填写则默认为全部分组(非必输)
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return TotalCount 域名列表总数
	 * @return PageNumber 当前页码
	 * @return Domains 本次获取的域名列表(Object)
	 * -DomainId 域名ID	
	 * -DomainName  域名名称
	 * -AliDomain 是否为阿里云万网域名
	 * -GroupId 域名分组ID
	 * -InstanceId 云解析产品ID
	 * -VersionCode 云解析版本Code
	 * -PunyCode 中文域名的punycode码，英文域名返回为空
	 * -DnsServers 域名在解析系统中的DNS列表(Object)
	 * 	--DnsServer DNS服务器名称，如dns1.hichina.com
	 * TODO 过
	 */
	@RequestMapping(value = "describeDomains")
	public DescribeDomainsResponse describeDomains(Long pageNumber,Long pageSize,String keyWord,String groupId) {
		DescribeDomainsResponse response = null;
		try {
			if(AliyunUtils.canSet(pageNumber)) {
				this.describeDomains.setPageNumber(pageNumber);
			}
			if(AliyunUtils.canSet(pageSize)) {
				this.describeDomains.setPageSize(pageSize);
			}
			if(AliyunUtils.canSet(keyWord)) {
				this.describeDomains.setKeyWord(keyWord);
			}
			if(AliyunUtils.canSet(groupId)) {
				this.describeDomains.setGroupId(groupId);
			}
			response = iacsClient.getAcsResponse(this.describeDomains);
		} catch (ServerException e) {
			log.error(e.getErrCode(),e.getErrMsg());
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
		}
		return response;
	}
	
	/**
	 * Action DescribeDomainInfo 获取域名信息
	 * @param DomainName 域名名称(必输)
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return DomainId 域名ID
	 * @return DomainName 域名名称
	 * @return AliDomain 是否为阿里云万网域名
	 * @return GroupId 域名分组ID
	 * @return GroupName 域名分组名称
	 * @return InstanceId 云解析产品ID，如果是免费版则为空
	 * @return VersionCode 云解析版本ID
	 * @return PunyCode 只针对中文域名返回punycode码
	 * @return DnsServers 域名在解析系统中的DNS列表
	 *  -DnsServer DNS服务器名称，如dns1.hichina.com
	 * @throws ClientException 
	 * 
	 * TODO 过
	 */
	@RequestMapping(value = "describeDomainInfo")
	public DescribeDomainInfoResponse describeDomainInfo(String domainName) throws ClientException {
		DescribeDomainInfoResponse response = null;
		try {
			this.describeDomainInfo.setDomainName(domainName);
			response  = this.iacsClient.getAcsResponse(this.describeDomainInfo);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action DescribeDomainWhoisInfo 获取域名 Whois 信息
	 * @param DomainName 域名名称(必输)
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return StatusList 域名状态列表(object)
	 * -Status 状态名称
	 * @return DnsServers 域名当前使用的DNS列表(object)
	 * -DnsServer DNS服务器名称，如dns1.hichina.com
	 * @return RegistrantName 所有者
	 * @return RegistrantEmail 所有者联系邮箱
	 * @return Registrar 注册商
	 * @return RegistrationDate 注册日期
	 * @return ExpirationDate 到期日期
	 * @throws ClientException 
	 * 
	 * TODO 过 
	 */
	@RequestMapping(value = "describeDomainWhoIsInfo")
	public DescribeDomainWhoisInfoResponse describeDomainWhoIsInfo(String domainName) throws ClientException {
		DescribeDomainWhoisInfoResponse response = null;
		try {
			this.describeDomainWhoIsInfo.setDomainName(domainName);
			response = this.iacsClient.getAcsResponse(this.describeDomainWhoIsInfo);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action ModifyHichinaDomainDNS 修改万网域名 DNS
	 * @param domainName 域名名称(必输)
	 * @return 
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return OriginalDnsServers 域名原来的DNS列表
	 * @return NewDnsServers 域名修改后的DNS列表
	 * @throws ClientException 
	 * TODO 过
	 */
	@RequestMapping(value = "modifyhichinaDomainDns")
	public ModifyHichinaDomainDNSResponse modifyhichinaDomainDns(String domainName) throws ClientException {
		ModifyHichinaDomainDNSResponse response = null;
		try {
			this.modifyhichinaDomainDns.setDomainName(domainName);
			response  = this.iacsClient.getAcsResponse(this.modifyhichinaDomainDns);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action GetMainDomainName 获取主域名名称
	 * @param inputString 字符串，最长不超过128个字符(必输)
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return DomainName 域名名称
	 * @return RR 主机记录信息
	 * @return DomainLevel 输入域名的级别
	 * @throws ClientException 
	 * TODO 过
	 */
	@RequestMapping(value="getMainDomainName")
	public GetMainDomainNameResponse getMainDomainName(String inputString) throws ClientException {
		GetMainDomainNameResponse response = null;
		try {
			this.getMainDomainName.setInputString(inputString);
			response = this.iacsClient.getAcsResponse(this.getMainDomainName);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action DescribeDomainLogs 获取域名操作日志
	 * @param pageNumber 当前页数，起始值为1，默认为1
	 * @param pageSize 分页查询时设置的每页行数，最大值100，默认为20
	 * @param keyWord 关键字，按照”%KeyWord%”模式搜索，不区分大小写
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return TotalCount 日志列表总数
	 * @return PageNumber 当前页码
	 * @return PageSize 本次查询获取的日志数量
	 * @return DomainLogs 域名操作日志列表(object)
	 * -ActionTime 操作时间
	 * -DomainName 域名名称
	 * -Action 操作行为
	 * -Message 操作消息
	 * -ClientIp 操作者IP
	 */
	@RequestMapping(value="describeDomainLogs")
	public DescribeDomainLogsResponse describeDomainLogs(Long pageNumber,Long pageSize,String keyWord) {
		DescribeDomainLogsResponse response = null;
		try {
			if(AliyunUtils.canSet(pageNumber)) {
				this.describeDomainLogs.setPageNumber(pageNumber);
			}
			if(AliyunUtils.canSet(pageSize)) {
				this.describeDomainLogs.setPageSize(pageSize);
			}
			if(AliyunUtils.canSet(keyWord)) {
				this.describeDomainLogs.setKeyWord(keyWord);
			}
			response  = this.iacsClient.getAcsResponse(this.describeDomainLogs);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
		}
		return response;
	}
	
	
	
}
