package com.gov.systemservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.gov")
@MapperScan("com.gov.systemservice.mapper")
@EnableDiscoveryClient
@EnableFeignClients
@EnableTransactionManagement
public class SystemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemServiceApplication.class, args);
	}

}
