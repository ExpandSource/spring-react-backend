package com.busanit.springreactbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SpringReactBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringReactBackendApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfig() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")  // 모든 경로 허가
						.allowedMethods("*")				// 모든 메소드 허가
						.allowedOrigins("http://localhost:3000");	// 출처 허가
			}
		};
	}
}
