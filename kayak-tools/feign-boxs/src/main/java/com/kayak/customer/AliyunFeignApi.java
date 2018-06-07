package com.kayak.customer;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kayak.client.AliyunFeignClient;


@RestController
public class AliyunFeignApi {

	@Autowired
	private AliyunFeignClient aliyunFeignClient;
	
	@RequestMapping(value="describeDomainRecords",method = RequestMethod.POST)
	public Map<String, Object> describeDomainRecords(String domainName,Long pageNumber,Long pageSize,String rRKeyWord,String typeKeyWord,String valueKeyWord){
		return aliyunFeignClient.describeDomainRecords(domainName, pageNumber, pageSize, rRKeyWord, typeKeyWord, valueKeyWord);
	}
	
}
