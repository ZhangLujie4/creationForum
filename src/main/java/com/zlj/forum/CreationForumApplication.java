package com.zlj.forum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.zlj.forum.web.mapper")
public class CreationForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreationForumApplication.class, args);
	}

}

