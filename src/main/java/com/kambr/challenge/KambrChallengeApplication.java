package com.kambr.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class KambrChallengeApplication {
	public static void main(String[] args) {
		SpringApplication.run(KambrChallengeApplication.class, args);
	}
}
