package com.kayak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.kayak.properties.AliyunDnsPropertie;

@SpringBootApplication
@EnableConfigurationProperties({AliyunDnsPropertie.class})
public class AliyunApplication {
	
	public static void main(String[] args) {
		if(args.length == 0) {
			SpringApplication.run(AliyunApplication.class);
			return;
		}
		SpringApplication.run(AliyunApplication.class, args);
	}

}
