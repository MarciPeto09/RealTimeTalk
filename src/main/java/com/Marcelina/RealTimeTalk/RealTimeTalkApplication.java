package com.Marcelina.RealTimeTalk;

import com.Marcelina.RealTimeTalk.model.Users;
import com.Marcelina.RealTimeTalk.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class RealTimeTalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(RealTimeTalkApplication.class, args);
	}

}
