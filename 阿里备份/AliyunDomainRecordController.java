package com.kayak.ailiyun.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.AddDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.AddDomainRecordResponse;
import com.aliyuncs.alidns.model.v20150109.DeleteDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.DeleteDomainRecordResponse;
import com.aliyuncs.alidns.model.v20150109.DeleteSubDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DeleteSubDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordInfoRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordInfoResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeRecordLogsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeRecordLogsResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeSubDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeSubDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.SetDomainRecordStatusRequest;
import com.aliyuncs.alidns.model.v20150109.SetDomainRecordStatusResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.exceptions.ClientException;
import com.kayak.base.action.BaseController;
import com.kayak.base.system.RequestSupport;
import com.kayak.util.AliyunUtils;

@RestController
public class AliyunDomainRecordController extends BaseController {

	private final Logger log = LoggerFactory.getLogger(AliyunDomainRecordController.class);

	@Autowired
	private IAcsClient iacsClient;

	@Autowired
	private AddDomainRecordRequest addDomainRecord;

	@Autowired
	private DeleteDomainRecordRequest deleteDomainRecord;

	@Autowired
	private UpdateDomainRecordRequest updateDomainRecord;

	@Autowired
	private SetDomainRecordStatusRequest setDomainRecordStatus;

	@Autowired
	private DescribeDomainRecordsRequest describeDomainRecords;

	@Autowired
	private DescribeDomainRecordInfoRequest describeDomainRecordInfo;

	@Autowired
	private DescribeSubDomainRecordsRequest describeSubDomainRecords;

	@Autowired
	private DeleteSubDomainRecordsRequest deleteSubDomainRecords;

	@Autowired
	private DescribeRecordLogsRequest describeRecordLogs;

	/**
	 * Action AddDomainRecord 添加解析记录
	 * @param domainName 域名名称(必输)
	 * @param rr 主机记录，如果要解析@.exmaple.com，主机记录要填写"@”，而不是空 (必输)
	 * @param type 解析记录类型 (必输)
	 * @param value 记录值(必输)
	 * @param ttl 生存时间，默认为600秒(非必输)
	 * @param priority MX记录的优先级，取值范围[1,10]，记录类型为MX记录时，此参数必须(非必输)
	 * @param line 解析线路，默认为default(非必输)
	 * @return
	 */

	@RequestMapping(value = "/aliyun/addDomainRecord.json")
	public String addDomainRecord() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			if (AliyunUtils.canSet(parmas.get("domainName"))) {
				this.addDomainRecord.setDomainName(AliyunUtils.canReadStr(parmas.get("domainName")));
			}
			this.addDomainRecord.setRR(AliyunUtils.canReadStr(parmas.get("rr")));
			this.addDomainRecord.setType(AliyunUtils.canReadStr(parmas.get("type")));
			this.addDomainRecord.setValue(AliyunUtils.canReadStr(parmas.get("value")));
			this.addDomainRecord.setTTL(AliyunUtils.canReadLong(parmas.get("ttl")));
			this.addDomainRecord.setPriority(AliyunUtils.canReadLong(parmas.get("priority")));
			this.addDomainRecord.setLine(AliyunUtils.canReadStr(parmas.get("line")));
			AddDomainRecordResponse response = iacsClient.getAcsResponse(this.addDomainRecord);
			Map<String, Object> map = new HashMap<>();
			map.put("requestId", response.getRecordId());
			map.put("recordId", response.getRequestId());
			return super.updateSuccess(map);
		} catch (ClientException e) {
			log.error(e.getMessage());
			return super.updateFailure(e.getMessage());
		}

	}

	/**
	 * Action DeleteDomainRecord 删除解析记录
	 * 
	 * @param recordId
	 *            解析记录的ID，此参数在添加解析时会返回，在获取域名解析列表时会返回(必输)
	 * @return RequestId 唯一请求识别码
	 * @return RecordId 解析记录的ID
	 * @throws ClientException
	 */
	@RequestMapping(value = "/aliyun/deleteDomainRecord.json")
	public String deleteDomainRecord() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			this.deleteDomainRecord.setRecordId(AliyunUtils.canReadStr(parmas.get("recordId")));
			DeleteDomainRecordResponse response = iacsClient.getAcsResponse(this.deleteDomainRecord);
			Map<String, Object> map = new HashMap<>();
			map.put("requestId", response.getRecordId());
			map.put("recordId", response.getRequestId());
			return super.updateSuccess(map);
		} catch (ClientException e) {
			log.error(e.getMessage());
			return super.updateFailure(e.getMessage());
		}
	}

	/**
	 * Action UpdateDomainRecord 修改解析记录
	 * 
	 * @param recordId
	 *            解析记录的ID，此参数在添加解析时会返回，在获取域名解析列表时会返回(必输)
	 * @param rR
	 *            主机记录，如果要解析@.exmaple.com，主机记录要填写"@”，而不是空(必输)
	 * @param type
	 *            解析记录类型(必输)
	 * @param value
	 *            记录值(必输)
	 * @param ttl
	 *            生存时间，默认为600秒（10分钟）
	 * @param priority
	 *            MX记录的优先级，取值范围[1,10]，记录类型为MX记录时，此参数必须
	 * @param line
	 *            解析线路，默认为default
	 * @return RequestId 唯一请求识别码
	 * @return RecordId 解析记录的ID
	 * @throws ClientException
	 */
	@RequestMapping(value = "/aliyun/updateDomainRecord.json")
	public String updateDomainRecord() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			this.updateDomainRecord.setRecordId(AliyunUtils.canReadStr(parmas.get("recordId")));
			this.updateDomainRecord.setRR(AliyunUtils.canReadStr(parmas.get("rr")));
			this.updateDomainRecord.setType(AliyunUtils.canReadStr(parmas.get("type")));
			this.updateDomainRecord.setValue(AliyunUtils.canReadStr(parmas.get("value")));
			this.updateDomainRecord.setTTL(AliyunUtils.canReadLong(parmas.get("ttl")));
			this.updateDomainRecord.setPriority(AliyunUtils.canReadLong(parmas.get("priority")));
			this.updateDomainRecord.setLine(AliyunUtils.canReadStr(parmas.get("line")));
			UpdateDomainRecordResponse response = iacsClient.getAcsResponse(this.updateDomainRecord);
			Map<String, Object> map = new HashMap<>();
			map.put("requestId", response.getRecordId());
			map.put("recordId", response.getRequestId());
			return super.updateSuccess(map);
		} catch (ClientException e) {
			log.error(e.getMessage());
			return super.updateFailure(e.getMessage());
		}
	}

	/**
	 * setDomainRecordStatus 设置解析记录状态
	 * 
	 * @param recordId
	 *            解析记录的ID，此参数在添加解析时会返回，在获取域名解析列表时会返回
	 * @param status
	 *            Enable: 启用解析 Disable: 暂停解析
	 * @return RequestId 唯一请求识别码
	 * @return RecordId 解析记录的ID
	 * @return Status 当前解析记录状态
	 * @throws ClientException
	 */
	@RequestMapping(value = "/aliyun/setDomainRecordStatus.json")
	public String setDomainRecordStatus() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			this.setDomainRecordStatus.setRecordId(AliyunUtils.canReadStr(parmas.get("recordId")));
			this.setDomainRecordStatus.setStatus(AliyunUtils.canReadStr(parmas.get("status")));
			SetDomainRecordStatusResponse response = iacsClient.getAcsResponse(this.setDomainRecordStatus);
			Map<String, Object> map = new HashMap<>();
			map.put("requestId", response.getRecordId());
			map.put("recordId", response.getRequestId());
			map.put("status", response.getStatus());
			return super.updateSuccess(map);
		} catch (ClientException e) {
			log.error(e.getMessage());
			return super.updateFailure(e.getMessage());
		}
	}

	/**
	 * Action DescribeDomainRecords 获取解析记录列表
	 * 
	 * @param domainName
	 *            域名名称(必输)
	 * @param pageNumber
	 *            当前页数，起始值为1，默认为1
	 * @param pageSize
	 *            分页查询时设置的每页行数，最大值500，默认为20
	 * @param rrKeyWord
	 *            主机记录的关键字，按照”%RRKeyWord%”模式搜索，不区分大小写
	 * @param typeKeyWord
	 *            解析类型的关键字，按照全匹配搜索，不区分大小写
	 * @param valueKeyWord
	 *            记录值的关键字，按照”%ValueKeyWord%”模式搜索，不区分大小写
	 * 
	 * @return domainName 域名名称
	 * @return recordId 解析记录ID
	 * @return rR 主机记录
	 * @return type 记录类型
	 * @return value 记录值
	 * @return tTL 生存时间
	 * @return priority MX记录的优先级
	 * @return line 解析线路
	 * @return status 解析记录状态，Enable/Disable
	 * @return locked 解析记录锁定状态，true/false
	 * @return weight 负载均衡权重
	 * @throws ClientException
	 */
	@RequestMapping(value = "/aliyun/describeDomainRecords.json")
	public String describeDomainRecords() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			if (AliyunUtils.canSet(AliyunUtils.canReadStr(parmas.get("domainName")))) {
				this.describeDomainRecords.setDomainName(AliyunUtils.canReadStr(parmas.get("domainName")));
			}
			this.describeDomainRecords.setPageNumber(AliyunUtils.canReadLong(parmas.get("pageNumber")));
			this.describeDomainRecords.setPageSize(AliyunUtils.canReadLong(parmas.get("pageSize")));
			this.describeDomainRecords.setRRKeyWord(AliyunUtils.canReadStr(parmas.get("rRKeyWord")));
			this.describeDomainRecords.setTypeKeyWord(AliyunUtils.canReadStr(parmas.get("typeKeyWord")));
			this.describeDomainRecords.setValueKeyWord(AliyunUtils.canReadStr(parmas.get("valueKeyWord")));
			DescribeDomainRecordsResponse response = iacsClient.getAcsResponse(this.describeDomainRecords);
			Map<String, Object> map = new HashMap<>();
			map.put("requestId", response.getRequestId());
			map.put("rows", response.getDomainRecords());
			map.put("pageNumber", response.getPageNumber());
			map.put("pageSize", response.getPageSize());
			map.put("totalCount", response.getTotalCount());
			return super.updateSuccess(map);
		} catch (ClientException e) {
			log.error(e.getMessage());
			return super.updateFailure(e.getMessage());
		}
	}

	/**
	 * Action DescribeDomainRecordInfo 获取解析记录信息
	 * 
	 * @param recordId
	 *            解析记录的ID ，此参数在添加解析时会返回，在获取域名解析列表时会返回
	 * @return RequestId 唯一请求识别码
	 * @return RecordId 解析记录的ID
	 * @return RR 解析类型
	 * @return Type 记录类型
	 * @return Value 记录值
	 * @return TTL 生存时间
	 * @return Priority MX记录的优先级
	 * @return Line 解析线路
	 * @return Status 解析记录状态，Enable/Disable
	 * @return Boolean 解析记录锁定状态，true/false
	 * @throws ClientException
	 */
	@RequestMapping(value = "/aliyun/describeDomainRecordInfo.json")
	public String describeDomainRecordInfo() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			this.describeDomainRecordInfo.setRecordId(AliyunUtils.canReadStr(parmas.get("recordId")));
			DescribeDomainRecordInfoResponse response = iacsClient.getAcsResponse(this.describeDomainRecordInfo);
			Map<String, Object> map = new HashMap<>();
			map.put("requestId", response.getRequestId());
			map.put("domainId", response.getDomainId());
			map.put("domainName", response.getDomainName());
			map.put("punyCode", response.getPunyCode());
			map.put("punyCode", response.getGroupId());
			map.put("groupName", response.getGroupName());
			map.put("recordId", response.getRecordId());
			map.put("rR", response.getRR());
			map.put("type", response.getType());
			map.put("value", response.getValue());
			map.put("tTL", response.getTTL());
			map.put("priority", response.getPriority());
			map.put("line", response.getLine());
			map.put("status", response.getStatus());
			map.put("locked", response.getLocked());
			return super.updateSuccess(map);
		} catch (ClientException e) {
			return super.updateFailure(e.getMessage());
		}
	}

	/**
	 * Action DescribeSubDomainRecords 获取子域名的解析记录列表
	 * 
	 * @param subDomain
	 *            域名名称，如www.abc.com，如果输入的是abc.com，则认为是@.abc.com；(必输)
	 * @param pageNumber
	 *            当前页数，起始值为1，默认为1
	 * @param pageSize
	 *            分页查询时设置的每页行数，最大值500，默认为20
	 * @param type
	 *            如果不填写，则返回子域名对应的全部解析记录类型。解析类型包括(不区分大小写)：A、MX、CNAME、TXT、REDIRECT_URL、FORWORD_URL、NS、AAAA、SRV
	 * @return RequestId 唯一请求识别码
	 * @return TotalCount 解析记录总数
	 * @return PageNumber 当前页码
	 * @return PageSize 本次查询获取的解析数量
	 * @return DomainRecords 解析记录列表 -DomainName 域名名称 -RecordId 解析记录ID -RR 主机记录 -Type
	 *         记录类型 -Value 记录值 -TTL 生存时间 -Priority MX记录的优先级 -Line 解析线路 -Status
	 *         解析记录状态，Enable/Disable -Locked 解析记录锁定状态，true/false -Weight 负载均衡权重
	 * @throws ClientException
	 */
	@RequestMapping(value = "/aliyun/describeSubDomainRecords.json")
	public String describeSubDomainRecords() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			this.describeSubDomainRecords.setSubDomain(AliyunUtils.canReadStr(parmas.get("subDomain")));
			this.describeSubDomainRecords.setPageNumber(AliyunUtils.canReadLong(parmas.get("pageNumber")));
			this.describeSubDomainRecords.setPageSize(AliyunUtils.canReadLong(parmas.get("pageSize")));
			this.describeSubDomainRecords.setType(AliyunUtils.canReadStr(parmas.get("type")));
			DescribeSubDomainRecordsResponse response = iacsClient.getAcsResponse(this.describeSubDomainRecords);
			Map<String, Object> map = new HashMap<>();
			map.put("requestId", response.getRequestId());
			map.put("rows", response.getDomainRecords());
			map.put("pageNumber", response.getPageNumber());
			map.put("pageSize", response.getPageSize());
			map.put("totalCount", response.getTotalCount());
			return super.updateSuccess(map);
		} catch (ClientException e) {
			return super.updateFailure(e.getMessage());
		}
	}

	/**
	 * Action DeleteSubDomainRecords 删除主机记录对应的解析记录(批量删除,老子没做checkbox,NND)
	 * 
	 * @param domainName
	 *            域名名称
	 * @param rR
	 *            主机记录，如果要解析@.exmaple.com，主机记录要填写"@”，而不是空
	 * @param type
	 *            如果不填写，则返回子域名对应的全部解析记录类型。解析类型包括(不区分大小写)：A、MX、CNAME、TXT、REDIRECT_URL、FORWORD_URL、NS、AAAA、SRV
	 * @return RequestId 唯一请求识别码
	 * @return RR 主机记录
	 * @return TotalCount 被删除的解析记录总数
	 * @throws ClientException
	 */
	@RequestMapping(value = "/aliyun/deleteSubDomainRecords.json")
	public String deleteSubDomainRecords(){
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			this.deleteSubDomainRecords.setDomainName(AliyunUtils.canReadStr(parmas.get("domainName")));
			this.deleteSubDomainRecords.setRR(AliyunUtils.canReadStr(parmas.get("rr")));
			this.deleteSubDomainRecords.setType(AliyunUtils.canReadStr(parmas.get("type")));
			DeleteSubDomainRecordsResponse response = iacsClient.getAcsResponse(this.deleteSubDomainRecords);
			Map<String, Object> map = new HashMap<>();
			map.put("requestId", response.getRequestId());
			map.put("rr", response.getRR());
			map.put("totalCount", response.getTotalCount());
			return super.updateSuccess(map);
		} catch (ClientException e) {
			return super.updateFailure(e.getMessage());
		}
	
	}

	/**
	 * Action DescribeRecordLogs 获取域名的解析操作日志
	 * 
	 * @param domainName
	 *            域名名称
	 * @param pageNumber
	 *            当前页数，起始值为1，默认为1
	 * @param pageSize
	 *            分页查询时设置的每页行数，最大值100，默认为20
	 * @param KeyWord
	 *            关键字，按照”%KeyWord%”模式搜索，不区分大小写
	 * @return
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return TotalCount 日志列表总数
	 * @return PageNumber 当前页码
	 * @return PageSize 本次查询获取的日志数量
	 * @return RecordLogs 域名操作日志列表 - ActionTime操作时间 - Action 操作行为 - Message 操作消息 -
	 *         ClientIp 操作者IP
	 * @throws ClientException
	 */
	@RequestMapping(value = "describeRecordLogs")
	public String describeRecordLogs() {
		try {
			Map<String, Object> parmas = RequestSupport.getBodyParameters();
			if (AliyunUtils.canSet(AliyunUtils.canReadLong(parmas.get("domainName")))) {
				this.describeRecordLogs.setDomainName(AliyunUtils.canReadStr("domainName"));
			}
			if (AliyunUtils.canSet(AliyunUtils.canReadLong(parmas.get("pageNumber")))) {
				this.describeRecordLogs.setPageNumber(AliyunUtils.canReadLong(parmas.get("pageNumber")));
			}
			if (AliyunUtils.canSet(AliyunUtils.canReadLong(parmas.get("pageSize")))) {
				this.describeRecordLogs.setPageSize(AliyunUtils.canReadLong(parmas.get("pageSize")));
			}
			this.describeRecordLogs.setKeyWord(AliyunUtils.canReadStr(parmas.get("keyWord")));
			DescribeRecordLogsResponse response = iacsClient.getAcsResponse(this.describeRecordLogs);
			Map<String, Object> map = new HashMap<>();
			map.put("pageNumber", response.getPageNumber());
			map.put("pageSize", response.getPageSize());
			map.put("totalCount", response.getTotalCount());
			map.put("requestId", response.getRequestId());
			map.put("rows", response.getRecordLogs());
			return super.updateSuccess(map);
		} catch (ClientException e) {
			return super.updateFailure(e.getMessage());
		}
	}

}
