package org.bitbucket.shevchenkod.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration
@PropertySource({"classpath:application.properties"})
//@ComponentScan("org.bitbucket.shevchenkod.restaurant")
@WebAppConfiguration
public class RestaurantApplication {//extends SpringBootServletInitializer {

	public static void main(String[] args) {

		SpringApplication.run(RestaurantApplication.class, args);
	}
}
