package com.internship.tmontica_admin;

import com.internship.tmontica_admin.security.JwtService;
import com.internship.tmontica_admin.security.JwtServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
@EnableScheduling
public class TmonticaAdminApplication {

	@Bean
	public ModelMapper modelMapper(){	return new ModelMapper(); }

	@Bean
	public JwtService jwtService() {
		return new JwtServiceImpl();
	}

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(5);
		return taskScheduler;
	}

	public static void main(String[] args) {
		SpringApplication.run(TmonticaAdminApplication.class, args);
	}

}
