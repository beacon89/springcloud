package com.kayak.customer;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.client.AliyunFeignClient;

@RestController
public class AliyunFeignApi {

	@Resource
	private AliyunFeignClient aliyunFeignClient;

	@RequestMapping(value = "describeDomainRecords", method = RequestMethod.POST)
	public Map<String, Object> describeDomainRecords(String domainName, Long pageNumber, Long pageSize,
			String rRKeyWord, String typeKeyWord, String valueKeyWord) {
		return aliyunFeignClient.describeDomainRecords(domainName, pageNumber, pageSize, rRKeyWord, typeKeyWord,
				valueKeyWord);
	}

	@RequestMapping(value = "addDomainRecord", method = RequestMethod.POST)
	public Map<String, Object> addDomainRecord(String domainName, String rr, String type, String value, Long ttl,
			Long priority, String line) {
		return aliyunFeignClient.addDomainRecord(domainName, rr, type, value, ttl, priority, line);
	}

	@RequestMapping(value = "deleteDomainRecord", method = RequestMethod.POST)
	public Map<String, Object> deleteDomainRecord(String recordId) {
		return aliyunFeignClient.deleteDomainRecord(recordId);
	}

	@RequestMapping(value = "updateDomainRecord", method = RequestMethod.POST)
	public Map<String, Object> updateDomainRecord(String recordId, String rr, String type, String value, Long ttl,
			Long priority, String line) {
		return aliyunFeignClient.updateDomainRecord(recordId,rr,type,value,ttl,priority,line);
	}

	@RequestMapping(value = "setDomainRecordStatus", method = RequestMethod.POST)
	public Map<String, Object> setDomainRecordStatus(String recordId, String status) {
		return aliyunFeignClient.setDomainRecordStatus(recordId,status);
	}

	@RequestMapping(value = "describeDomainRecordInfo", method = RequestMethod.POST)
	public Map<String, Object> describeDomainRecordInfo(@RequestParam("recordId") final String recordId){
		return aliyunFeignClient.describeDomainRecordInfo(recordId);
	}

}
