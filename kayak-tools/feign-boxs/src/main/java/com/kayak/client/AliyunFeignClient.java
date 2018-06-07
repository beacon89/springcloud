package com.kayak.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kayak.remote.AliyunApiHystrix;

@FeignClient(name = "kayak-aliyun",fallback=AliyunApiHystrix.class)
public interface AliyunFeignClient {

	@RequestMapping(value = "describeDomainRecords", method = RequestMethod.POST)
	public Map<String, Object> describeDomainRecords(@RequestParam("domainName") final String domainName,
			@RequestParam("pageNumber") final Long pageNumber, @RequestParam("pageSize") final Long pageSize,
			@RequestParam("rRKeyWord") final String rRKeyWord, @RequestParam("typeKeyWord") final String typeKeyWord,
			@RequestParam("valueKeyWord") final String valueKeyWord);

	@RequestMapping(value = "addDomainRecord", method = RequestMethod.POST)
	public Map<String, Object> addDomainRecord(@RequestParam("domainName") final String domainName,
			@RequestParam("rr") final String rr, @RequestParam("type") final String type,
			@RequestParam("value") final String value, @RequestParam("ttl") final Long ttl,
			@RequestParam("priority") final Long priority, @RequestParam("line") final String line);

	@RequestMapping(value = "deleteDomainRecord", method = RequestMethod.POST)
	public Map<String, Object> deleteDomainRecord(@RequestParam("recordId") final String recordId);

	@RequestMapping(value = "updateDomainRecord", method = RequestMethod.POST)
	public Map<String, Object> updateDomainRecord(@RequestParam("recordId") final String recordId,
			@RequestParam("rr") final String rr, @RequestParam("type") final String type,
			@RequestParam("value") final String value, @RequestParam("ttl") final Long ttl,
			@RequestParam("priority") final Long priority, @RequestParam("line") final String line);
	
	@RequestMapping(value = "setDomainRecordStatus", method = RequestMethod.POST)
	public Map<String, Object> setDomainRecordStatus(@RequestParam("recordId") final String recordId,@RequestParam("status") final String status);
	
	
	@RequestMapping(value="describeDomainRecordInfo", method = RequestMethod.POST)
	public Map<String, Object> describeDomainRecordInfo(@RequestParam("recordId") final String recordId);
}
