package com.kayak.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.kayak.utils.AliyunUtils;

@RestController
public class AliyunDomainRecordController extends BaseController{

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
	 * @throws ClientException 
	 * TODO 过
	 */
	@RequestMapping(value = "addDomainRecord",method = RequestMethod.POST)
	public Map<String, Object> addDomainRecord(String domainName,String rr,String type,String value,Long ttl,Long priority,String line) {
			AddDomainRecordResponse response;
			try {
				if(AliyunUtils.canSet(domainName)) {
					this.addDomainRecord.setDomainName(domainName);
				}
				this.addDomainRecord.setRR(rr);
				this.addDomainRecord.setType(type);
				this.addDomainRecord.setValue(value);
				this.addDomainRecord.setTTL(ttl);
				this.addDomainRecord.setPriority(priority);
				this.addDomainRecord.setLine(line);
				response = iacsClient.getAcsResponse(this.addDomainRecord);
				Map<String, Object> map = new HashMap<>();
				map.put("requestId", response.getRecordId());
				map.put("recordId", response.getRequestId());
				return this.responseMessage(map);
			} catch (ClientException e) {
				log.error(e.getMessage());
				return this.responseMessage(TransactionCode.ALIYUN9999.getCode(),e.getMessage());
			}
			
	}
	
	/**
	 * Action DeleteDomainRecord 删除解析记录
	 * @param recordId 解析记录的ID，此参数在添加解析时会返回，在获取域名解析列表时会返回(必输)
	 * @return RequestId 唯一请求识别码
	 * @return RecordId 解析记录的ID
	 * @throws ClientException 
	 * TODO 过
	 */
	@RequestMapping(value = "deleteDomainRecord",method = RequestMethod.POST)
	public Map<String, Object> deleteDomainRecord(String recordId) throws ClientException {
		this.deleteDomainRecord.setRecordId(recordId);
		DeleteDomainRecordResponse response = iacsClient.getAcsResponse(this.deleteDomainRecord);
		Map<String, Object> map = new HashMap<>();
		map.put("requestId", response.getRecordId());
		map.put("recordId", response.getRequestId());
		return this.responseMessage(map);
	}
	
	/**
	 * Action UpdateDomainRecord 修改解析记录
	 * @param recordId 解析记录的ID，此参数在添加解析时会返回，在获取域名解析列表时会返回(必输)
	 * @param rR 主机记录，如果要解析@.exmaple.com，主机记录要填写"@”，而不是空(必输)
	 * @param type 解析记录类型(必输)
	 * @param value 记录值(必输)
	 * @param ttl 生存时间，默认为600秒（10分钟）
	 * @param priority MX记录的优先级，取值范围[1,10]，记录类型为MX记录时，此参数必须
	 * @param line 解析线路，默认为default
	 * @return RequestId 唯一请求识别码
	 * @return RecordId 解析记录的ID
	 * @throws ClientException 
	 * TODO 过
	 */
	@RequestMapping(value = "updateDomainRecord",method = RequestMethod.POST)
	public Map<String, Object> updateDomainRecord(String recordId,String rr,String type,String value,Long ttl,Long priority,String line)  {
		UpdateDomainRecordResponse response = null;
		try {
			this.updateDomainRecord.setRecordId(recordId);
			this.updateDomainRecord.setRR(rr);
			this.updateDomainRecord.setType(type);
			this.updateDomainRecord.setValue(value);
			this.updateDomainRecord.setTTL(ttl);
			this.updateDomainRecord.setPriority(priority);
			this.updateDomainRecord.setLine(line);
			response = iacsClient.getAcsResponse(this.updateDomainRecord);
			Map<String, Object> map = new HashMap<>();
			map.put("requestId", response.getRecordId());
			map.put("recordId", response.getRequestId());
			return this.responseMessage(map);
		} catch (ClientException e) {
			log.error(e.getMessage());
			return this.responseMessage(TransactionCode.ALIYUN9999.getCode(),e.getMessage());
		}
	}
	
	/**
	 * setDomainRecordStatus  设置解析记录状态
	 * @param recordId 解析记录的ID，此参数在添加解析时会返回，在获取域名解析列表时会返回
	 * @param status Enable: 启用解析 Disable: 暂停解析
	 * @return RequestId 唯一请求识别码
	 * @return RecordId 解析记录的ID
	 * @return Status 当前解析记录状态
	 * @throws ClientException 
	 * TODO 过
	 */
	@RequestMapping(value = "setDomainRecordStatus", method = RequestMethod.POST)
	public Map<String, Object> setDomainRecordStatus(String recordId,String status) throws ClientException {
			this.setDomainRecordStatus.setRecordId(recordId);
			this.setDomainRecordStatus.setStatus(status);
			SetDomainRecordStatusResponse response = iacsClient.getAcsResponse(this.setDomainRecordStatus);
			Map<String, Object> map = new HashMap<>();
			map.put("requestId", response.getRecordId());
			map.put("recordId", response.getRequestId());
			map.put("status", response.getStatus());
			return this.responseMessage(map);
	}
	
	/**
	 * Action DescribeDomainRecords 获取解析记录列表
	 * @param domainName 域名名称(必输)
	 * @param pageNumber 当前页数，起始值为1，默认为1
	 * @param pageSize 分页查询时设置的每页行数，最大值500，默认为20
	 * @param rrKeyWord 主机记录的关键字，按照”%RRKeyWord%”模式搜索，不区分大小写
	 * @param typeKeyWord 解析类型的关键字，按照全匹配搜索，不区分大小写
	 * @param valueKeyWord 记录值的关键字，按照”%ValueKeyWord%”模式搜索，不区分大小写
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
	 * TODO 过
	 */
	@RequestMapping(value="describeDomainRecords",method = RequestMethod.POST)
	public Map<String, Object> describeDomainRecords(String domainName,Long pageNumber,Long pageSize,String rRKeyWord,String typeKeyWord,String valueKeyWord) throws ClientException {
		if(AliyunUtils.canSet(domainName)) {
			this.describeDomainRecords.setDomainName(domainName);
		}
		this.describeDomainRecords.setPageNumber(pageNumber);
		this.describeDomainRecords.setPageSize(pageSize);
		this.describeDomainRecords.setRRKeyWord(rRKeyWord);
		this.describeDomainRecords.setTypeKeyWord(typeKeyWord);
		this.describeDomainRecords.setValueKeyWord(valueKeyWord);
		DescribeDomainRecordsResponse response = iacsClient.getAcsResponse(this.describeDomainRecords);
		Map<String, Object> map = new HashMap<>();
		map.put("requestId", response.getRequestId());
		return this.responseMessage(response.getDomainRecords(), response.getPageNumber(), response.getPageSize(), response.getTotalCount(), map);
	}
	
	/**
	 * Action DescribeDomainRecordInfo 获取解析记录信息
	 * @param recordId 解析记录的ID ，此参数在添加解析时会返回，在获取域名解析列表时会返回
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
	 * TODO 过
	 */
	@RequestMapping(value="describeDomainRecordInfo", method = RequestMethod.POST)
	public Map<String, Object> describeDomainRecordInfo(String recordId) throws ClientException {
			this.describeDomainRecordInfo.setRecordId(recordId);
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
			return this.responseMessage(map);
	}
	
	/**
	 * Action DescribeSubDomainRecords 获取子域名的解析记录列表
	 * @param subDomain 域名名称，如www.abc.com，如果输入的是abc.com，则认为是@.abc.com；(必输)
	 * @param pageNumber 当前页数，起始值为1，默认为1
	 * @param pageSize 分页查询时设置的每页行数，最大值500，默认为20
	 * @param type 如果不填写，则返回子域名对应的全部解析记录类型。解析类型包括(不区分大小写)：A、MX、CNAME、TXT、REDIRECT_URL、FORWORD_URL、NS、AAAA、SRV
	 * @return RequestId 唯一请求识别码
	 * @return TotalCount 解析记录总数
	 * @return PageNumber 当前页码
	 * @return PageSize 本次查询获取的解析数量
	 * @return DomainRecords 解析记录列表
	 * -DomainName 域名名称
	 * -RecordId 解析记录ID
	 * -RR 主机记录
	 * -Type 记录类型
	 * -Value 记录值
	 * -TTL	生存时间
	 * -Priority MX记录的优先级
	 * -Line 解析线路
	 * -Status 解析记录状态，Enable/Disable
	 * -Locked 解析记录锁定状态，true/false
	 * -Weight 负载均衡权重
	 * @throws ClientException 
	 * TODO 过
	 */
	@RequestMapping(value="describeSubDomainRecords")
	public DescribeSubDomainRecordsResponse describeSubDomainRecords(String subDomain,Long pageNumber,Long pageSize,String type) throws ClientException {
		DescribeSubDomainRecordsResponse response = null;
		try {
			this.describeSubDomainRecords.setSubDomain(subDomain);
			if(AliyunUtils.canSet(pageNumber)) {
				this.describeSubDomainRecords.setPageNumber(pageNumber);
			}
			if(AliyunUtils.canSet(pageSize)) {
				this.describeSubDomainRecords.setPageSize(pageSize);
			}
			if(AliyunUtils.canSet(type)) {
				this.describeSubDomainRecords.setType(type);
			}
			response = iacsClient.getAcsResponse(this.describeSubDomainRecords);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action DeleteSubDomainRecords 删除主机记录对应的解析记录(批量删除,老子没做checkbox,NND)
	 * @param domainName 域名名称
	 * @param rR 主机记录，如果要解析@.exmaple.com，主机记录要填写"@”，而不是空
	 * @param type 如果不填写，则返回子域名对应的全部解析记录类型。解析类型包括(不区分大小写)：A、MX、CNAME、TXT、REDIRECT_URL、FORWORD_URL、NS、AAAA、SRV
	 * @return RequestId 唯一请求识别码
	 * @return RR 主机记录
	 * @return TotalCount 被删除的解析记录总数
	 * @throws ClientException 
	 */
	@RequestMapping(value="deleteSubDomainRecords")
	public DeleteSubDomainRecordsResponse deleteSubDomainRecords(String domainName,String rr,String type) throws ClientException {
		DeleteSubDomainRecordsResponse response = null;
		try {
			this.deleteSubDomainRecords.setDomainName(domainName);
			this.deleteSubDomainRecords.setRR(rr);
			if(AliyunUtils.canSet(type)) {
				this.deleteSubDomainRecords.setType(type);
			}
			response = iacsClient.getAcsResponse(this.deleteSubDomainRecords);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	
	/**
	 * Action DescribeRecordLogs 获取域名的解析操作日志
	 * @param domainName 域名名称
	 * @param pageNumber 当前页数，起始值为1，默认为1	
	 * @param pageSize 分页查询时设置的每页行数，最大值100，默认为20
	 * @param KeyWord 关键字，按照”%KeyWord%”模式搜索，不区分大小写
	 * @return 
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return TotalCount 日志列表总数
	 * @return PageNumber 当前页码
	 * @return PageSize 本次查询获取的日志数量
	 * @return RecordLogs 域名操作日志列表
	 * - ActionTime操作时间
	 * - Action 操作行为
	 * - Message 操作消息
	 * - ClientIp 操作者IP
	 * @throws ClientException 
	 * TODO 过
	 */
	@RequestMapping(value="describeRecordLogs")
	public DescribeRecordLogsResponse describeRecordLogs(String domainName,Long pageNumber,Long pageSize,String keyWord) throws ClientException {
		DescribeRecordLogsResponse response = null;
		try {
			if(AliyunUtils.canSet(domainName)) {
				this.describeRecordLogs.setDomainName(domainName);
			}
			if(AliyunUtils.canSet(pageNumber)) {
				this.describeRecordLogs.setPageNumber(pageNumber);
			}
			if(AliyunUtils.canSet(pageSize)) {
				this.describeRecordLogs.setPageSize(pageSize);
			}
			this.describeRecordLogs.setKeyWord(keyWord);
			response = iacsClient.getAcsResponse(this.describeRecordLogs);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	
	
	
}
