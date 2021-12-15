package com.infopulse.infomail;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Infomail API",
				version = "1.0",
				description = "This is an email service, that allows to schedule emails sending and share email templates"))
@SecurityScheme(
		name = "Authorization",
		type = SecuritySchemeType.APIKEY,
		in = SecuritySchemeIn.HEADER)
public class InfoMailApplication {

	@Value("${application.timezone.default}")
	private String defaultTimeZone;

	public static void main(String[] args) {
		SpringApplication.run(InfoMailApplication.class, args);
	}

	@PostConstruct
	private void init() {
		TimeZone.setDefault(TimeZone.getTimeZone(defaultTimeZone));
	}
}
