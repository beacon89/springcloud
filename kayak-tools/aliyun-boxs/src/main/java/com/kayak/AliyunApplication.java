package com.kayak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.kayak.properties.AliyunDnsPropertie;

@SpringBootApplication
@EnableConfigurationProperties({AliyunDnsPropertie.class})
@EnableEurekaClient
public class AliyunApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AliyunApplication.class, args);
	}

}
