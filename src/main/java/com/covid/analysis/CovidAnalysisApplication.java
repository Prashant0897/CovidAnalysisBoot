package com.covid.analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.covid.analysis.controller.CoreInitiater;


@SpringBootApplication
public class CovidAnalysisApplication {
	
	public static void main(String[] args) {
		
		//SpringApplication.run(CovidAnalysisApplication.class, args);
		
		//Uncomment above line and comment everything below to run app as Rest WebService
		
		ConfigurableApplicationContext app = SpringApplication.run(CovidAnalysisApplication.class, args);
		
		CoreInitiater init = app.getBean(CoreInitiater.class);
		System.out.println(">>>>>>>>Covid Analysis Application - Console Preview");
		init.initiate();
		System.out.println(">>>>>>>>Exiting.......");
		SpringApplication.exit(app, () -> 0);
	}

}
