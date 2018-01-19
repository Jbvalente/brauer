package com.tjgo.brauer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BrauerApplication {
	//Será usado apenas com profile
	/*private static ApplicationContext APPLICATION_CONTEXT;*/

	public static void main(String[] args) {
		SpringApplication.run(BrauerApplication.class, args);
		/*APPLICATION_CONTEXT = SpringApplication.run(BrauerApplication.class, args);*/
	}
	
	/*public static <T> T getBean(Class<T> requiredType){
		return APPLICATION_CONTEXT.getBean(requiredType);
	}*/
	
}
