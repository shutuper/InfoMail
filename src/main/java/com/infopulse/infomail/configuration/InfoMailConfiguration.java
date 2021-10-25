package com.infopulse.infomail.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfoMailConfiguration {

	@Bean("ConfirmationTemplate")
	public String getConfirmationTemplate() {
		return "";
	} // infomail@infopulse.com

}
