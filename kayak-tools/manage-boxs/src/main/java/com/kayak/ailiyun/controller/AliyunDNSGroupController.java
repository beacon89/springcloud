package com.kayak.ailiyun.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.AddDomainGroupRequest;
import com.aliyuncs.alidns.model.v20150109.AddDomainGroupResponse;
import com.aliyuncs.alidns.model.v20150109.ChangeDomainGroupRequest;
import com.aliyuncs.alidns.model.v20150109.ChangeDomainGroupResponse;
import com.aliyuncs.alidns.model.v20150109.DeleteDomainGroupRequest;
import com.aliyuncs.alidns.model.v20150109.DeleteDomainGroupResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainGroupsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainGroupsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainGroupRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainGroupResponse;
import com.aliyuncs.exceptions.ClientException;
import com.kayak.util.AliyunUtils;

/**
 * 
 * @author beacon
 *
 */
@RestController
public class AliyunDNSGroupController {

	private final Logger log = LoggerFactory.getLogger(AliyunDNSGroupController.class);

	@Autowired
	private IAcsClient iacsClient;

	@Autowired
	private AddDomainGroupRequest addDomainGroup;
	
	@Autowired
	private UpdateDomainGroupRequest updateDomainGroup;
	
	@Autowired
	private DeleteDomainGroupRequest deleteDomainGroup;
	
	@Autowired
	private ChangeDomainGroupRequest changeDomainGroup;
	
	@Autowired
	private DescribeDomainGroupsRequest describeDomainGroups;
	
	/**
	 * Action AddDomainGroup 添加域名分组
	 * @param groupName 域名分组名称(必输)
	 * @return RequestId 唯一请求识别码
	 * @return GroupId 域名分组ID
	 * @return GroupName 域名分组名称
	 * @throws ClientException 
	 */
	@RequestMapping(value = "addDomainGroup")
	public AddDomainGroupResponse addDomainGroup(String groupName) throws ClientException {
		AddDomainGroupResponse response = null;
		try {
			this.addDomainGroup.setGroupName(groupName);
			response = iacsClient.getAcsResponse(this.addDomainGroup);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action UpdateDomainGroup 修改域名分组
	 * @param groupId 域名分组ID(必输)
	 * @param groupName 域名分组新名称(必输)
	 * @return RequestId 唯一请求识别码
	 * @return GroupId 域名分组ID
	 * @return GroupName 域名分组名称
	 * @throws ClientException 
	 */
	@RequestMapping(value = "updateDomainGroup")
	public UpdateDomainGroupResponse updateDomainGroup(String groupId,String groupName) throws ClientException {
		UpdateDomainGroupResponse response = null;
		try {
			this.updateDomainGroup.setGroupId(groupId);
			this.updateDomainGroup.setGroupName(groupName);
			response = iacsClient.getAcsResponse(this.updateDomainGroup);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action DeleteDomainGroup 删除域名分组
	 * @param groupId 域名分组ID(必输)
	 * @return 
	 * @return RequestId 唯一请求识别码
	 * @return GroupName 域名分组名称
	 * @throws ClientException 
	 */
	@RequestMapping(value = "deleteDomainGroup")
	public DeleteDomainGroupResponse deleteDomainGroup(String groupId) throws ClientException {
		DeleteDomainGroupResponse response = null;
		try {
			this.deleteDomainGroup.setGroupId(groupId);
			response = iacsClient.getAcsResponse(this.deleteDomainGroup);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action changeDomainGroup 更换域名分组
	 * @param DomainName 域名名称 必输
	 * @param GroupId 目标域名分组ID 必输
	 * @return 
	 * @return RequestId 唯一请求识别码
	 * @return GroupId 目标域名分组ID
	 * @return GroupName 目标域名分组名称
	 * @throws ClientException 
	 */
	@RequestMapping(value = "changeDomainGroup")
	public ChangeDomainGroupResponse changeDomainGroup(String domainName,String groupId) throws ClientException {
		ChangeDomainGroupResponse response = null;
		try {
			this.changeDomainGroup.setDomainName(domainName);
			this.changeDomainGroup.setGroupId(groupId);
			response = iacsClient.getAcsResponse(this.changeDomainGroup);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action DescribeDomainGroups
	 * @param pageNumber 当前页数，起始值为1，默认为1
	 * @param pageSize 分页查询时设置的每页行数，最大值100，默认为20
	 * @param keyWord 组名关键字，按照”%KeyWord%”模式搜索，不区分大小写
	 * @return
	 * @throws ClientException 
	 */
	@RequestMapping(value = "describeDomainGroups")
	public DescribeDomainGroupsResponse describeDomainGroups(Long pageNumber,Long pageSize,String keyWord) throws ClientException {
		DescribeDomainGroupsResponse response = null;
		try {
			if(AliyunUtils.canSet(pageNumber)) {
				this.describeDomainGroups.setPageNumber(pageNumber);
			}
			if(AliyunUtils.canSet(pageSize)) {
				this.describeDomainGroups.setPageSize(pageSize);
			}
			if(AliyunUtils.canSet(keyWord)) {
				this.describeDomainGroups.setKeyWord(keyWord);
			}
			response = iacsClient.getAcsResponse(this.describeDomainGroups);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	} 

	
	
	
}
