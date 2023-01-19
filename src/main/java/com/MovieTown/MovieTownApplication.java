package com.MovieTown;

import com.MovieTown.beans.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
public class MovieTownApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(MovieTownApplication.class, args);
		MovieTownServer movieTownServer = ctx.getBean(MovieTownServer.class);
		movieTownServer.runApplication();
	}

	@Bean
	public HashMap<String, MySession> sessions(){
		return new HashMap<String, MySession>();
	}
}
