package com.kayak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.kayak.model.KubModel;

@SpringBootApplication
@EnableConfigurationProperties({ KubModel.class })
public class KUBApplication {

	public static void main(String[] args) {
		if (args.length == 0) {
			SpringApplication.run(KUBApplication.class);
			return;
		}
		SpringApplication.run(KUBApplication.class, args);
	}

}
