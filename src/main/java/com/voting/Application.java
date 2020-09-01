package com.voting;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class Application {
	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7:00"));
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}