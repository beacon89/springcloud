package com.kayak.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.AddBatchDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.AddBatchDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.DeleteBatchDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DeleteBatchDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.DeleteBatchDomainsRequest;
import com.aliyuncs.alidns.model.v20150109.DeleteBatchDomainsResponse;
import com.aliyuncs.alidns.model.v20150109.DescribeBatchResultRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeBatchResultResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateBatchDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateBatchDomainRecordsResponse;
import com.aliyuncs.exceptions.ClientException;

@RestController
public class AliyunBatchDNSController {

	private final Logger log = LoggerFactory.getLogger(AliyunBatchDNSController.class);
	@Autowired
	private IAcsClient iacsClient;
	@Autowired
	private DeleteBatchDomainsRequest deleteBatchDomains;
	@Autowired
	private AddBatchDomainRecordsRequest addBatchDomainRecords;
	@Autowired
	private UpdateBatchDomainRecordsRequest updateBatchDomainRecords;
	@Autowired
	private DeleteBatchDomainRecordsRequest deleteBatchDomainRecords;
	@Autowired
	private DescribeBatchResultRequest describeBatchResult;
	
	/**
	 * Action deleteBatchDomains 批量删除域名
	 * @param domains 多个域名名称，以JSON字符串的形式提交(不包含回车换行符)，最多支持5,000个域名(必输)
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return TraceId 批量任务ID
	 * @throws ClientException 
	 */
	@RequestMapping(value = "deleteBatchDomains")
	public DeleteBatchDomainsResponse deleteBatchDomains(String domains) throws ClientException {
		DeleteBatchDomainsResponse response = null;
		try {
			String domainarry[] = domains.split(",");
			List<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>();
			for(int i = 0;i<domainarry.length;i++) {
				Map<Integer,String> map = new HashMap<Integer,String>();
				map.put(i,domainarry[i]);
				list.add(map);
			}
			this.deleteBatchDomains.setDomains(JSON.toJSONString(list));
			response = iacsClient.getAcsResponse(this.deleteBatchDomains);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action AddBatchDomainRecords 批量添加域名解析记录
	 * @param records 要添加的域名和解析, 以JSON字符串的形式提交(不包含回车换行符)，最多支持5,000个解析记录。(必输)
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return TraceId 批量任务ID
	 * @throws ClientException 
	 */
	@RequestMapping(value = "addBatchDomainRecords")
	public AddBatchDomainRecordsResponse addBatchDomainRecords(String records) throws ClientException {
		AddBatchDomainRecordsResponse response = null;
		try {
			String domainarry[] = records.split(",");
			List<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>();
			for(int i = 0;i<domainarry.length;i++) {
				Map<Integer,String> map = new HashMap<Integer,String>();
				map.put(i,domainarry[i]);
				list.add(map);
			}
			this.addBatchDomainRecords.setRecords(JSON.toJSONString(list));
			response = iacsClient.getAcsResponse(this.addBatchDomainRecords);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action UpdateBatchDomainRecords 批量修改解析记录
	 * @param records 要修改的解析, 以JSON字符串的形式提交(不包含回车换行符)，最多支持5,000个解析记录。(必输)
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return TraceId 批量任务ID
	 * @throws ClientException 
	 */
	@RequestMapping(value = "updateBatchDomainRecords")
	public UpdateBatchDomainRecordsResponse updateBatchDomainRecords(String records) throws ClientException {
		UpdateBatchDomainRecordsResponse response = null;
		try {
			String domainarry[] = records.split(",");
			List<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>();
			for(int i = 0;i<domainarry.length;i++) {
				Map<Integer,String> map = new HashMap<Integer,String>();
				map.put(i,domainarry[i]);
				list.add(map);
			}
			this.updateBatchDomainRecords.setRecords(JSON.toJSONString(list));
			response = iacsClient.getAcsResponse(this.updateBatchDomainRecords);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	} 
	
	/**
	 * Action DeleteBatchDomainRecords 操作接口名，系统规定参数，取值
	 * @param records 要删除的解析, 以JSON字符串的形式提交(不包含回车换行符)，最多支持5,000个解析记录。(必输)
	 * 
	 * @return RequestId 唯一请求识别码
	 * @return TraceId 批量任务ID
	 * @throws ClientException 
	 */
	@RequestMapping(value = "deleteBatchDomainRecords")
	public DeleteBatchDomainRecordsResponse deleteBatchDomainRecords(String records) throws ClientException {
		DeleteBatchDomainRecordsResponse response = null;
		try {
			String domainarry[] = records.split(",");
			List<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>();
			for(int i = 0;i<domainarry.length;i++) {
				Map<Integer,String> map = new HashMap<Integer,String>();
				map.put(i,domainarry[i]);
				list.add(map);
			}
			this.deleteBatchDomainRecords.setRecords(JSON.toJSONString(list));
			response = iacsClient.getAcsResponse(this.deleteBatchDomainRecords);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	/**
	 * Action DescribeBatchResult 查询批量操作结果
	 * @param traceId 批量任务ID
	 * @return RequestId 唯一请求识别码
	 * @return TraceId 批量任务ID
	 * @return Status 0：操作进行中，1：操作已成功完成，2：操作中断
	 * @return BatchCount 批量操作总数量
	 * @return SuccessNumber 批量操作成功的数量
	 * @return FailResults 批量任务ID(object)
	 * @throws ClientException 
	 */
	@RequestMapping(value = "describeBatchResult")
	public DescribeBatchResultResponse describeBatchResult(String traceId) throws ClientException {
		DescribeBatchResultResponse response = null;
		try {
			String domainarry[] = traceId.split(",");
			List<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>();
			for(int i = 0;i<domainarry.length;i++) {
				Map<Integer,String> map = new HashMap<Integer,String>();
				map.put(i,domainarry[i]);
				list.add(map);
			}
			this.describeBatchResult.setTraceId(traceId);;
			response = iacsClient.getAcsResponse(this.describeBatchResult);
		} catch (ClientException e) {
			log.error(e.getErrCode(),e.getErrMsg());
			throw e;
		}
		return response;
	}
	
	
	
	
	
	
	
}
