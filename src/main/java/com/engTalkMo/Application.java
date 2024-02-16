package com.engTalkMo;

import com.engTalkMo.redis.RefreshTokenRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.engTalkMo", excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {RefreshTokenRepository.class}))
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// exclude 옵션을 주어 RefreshTokenRepository는 적용하지 않도록 한다.
}
