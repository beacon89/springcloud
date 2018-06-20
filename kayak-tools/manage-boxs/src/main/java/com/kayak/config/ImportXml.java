package com.kayak.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:conf/spring/spring-*.xml")
public class ImportXml {
}
