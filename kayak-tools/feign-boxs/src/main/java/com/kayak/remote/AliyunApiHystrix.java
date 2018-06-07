package com.kayak.remote;

import java.util.Map;

import org.springframework.context.annotation.Configuration;

import com.kayak.client.AliyunFeignClient;
import com.kayak.controller.BeseHystrix;

@Configuration
public class AliyunApiHystrix extends BeseHystrix implements AliyunFeignClient{

	@Override
	public Map<String, Object> describeDomainRecords(String domainName, Long pageNumber, Long pageSize,
			String rRKeyWord, String typeKeyWord, String valueKeyWord) {
		return this.returnSystemfail("kayak-aliyun");
	}

	@Override
	public Map<String, Object> addDomainRecord(String domainName, String rr, String type, String value, Long ttl,
			Long priority, String line) {
		return this.returnSystemfail("kayak-aliyun");
	}

	@Override
	public Map<String, Object> deleteDomainRecord(String recordId) {
		return this.returnSystemfail("kayak-aliyun");
	}

	@Override
	public Map<String, Object> updateDomainRecord(String recordId, String rr, String type, String value, Long ttl,
			Long priority, String line) {
		return this.returnSystemfail("kayak-aliyun");
	}

	@Override
	public Map<String, Object> setDomainRecordStatus(String recordId, String status) {
		return this.returnSystemfail("kayak-aliyun");
	}

	@Override
	public Map<String, Object> describeDomainRecordInfo(String recordId) {
		return this.returnSystemfail("kayak-aliyun");
	}

}
