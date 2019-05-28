package com.zlj.forum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = "com.zlj.forum.web.mapper")
public class CreationForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreationForumApplication.class, args);
	}

}

