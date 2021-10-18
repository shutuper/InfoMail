package com.infopulse.infomail.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfoMailConfiguration {

	@Bean("sentFrom")
	public String sentFrom() {
		return "infomail.infopulse@gmail.com";
	} // infomail@infopulse.com

}
