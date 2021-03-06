package com.infopulse.infomail.security.config;

import com.infopulse.infomail.security.filters.AppAuthenticationFilter;
import com.infopulse.infomail.security.filters.AppAuthorizationFilter;
import com.infopulse.infomail.security.jwt.JwtUtil;
import com.infopulse.infomail.services.security.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static com.infopulse.infomail.models.users.roles.AppUserRole.USER;
import static lombok.AccessLevel.PRIVATE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@NonFinal
	@Value("${application.security.frontUrl}")
	String frontURL;

	JwtUtil jwtUtil;
	PasswordEncoder passwordEncoder;
	AppUserDetailsService userDetailsService;
	AppAuthorizationFilter authorizationFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		final AppAuthenticationFilter authenticationFilter = new AppAuthenticationFilter(
				jwtUtil,
				authenticationManagerBean());

		http.cors().and().csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilter(authenticationFilter)
				.addFilterAfter(authorizationFilter, authenticationFilter.getClass())
				.authorizeRequests()
				.antMatchers(
						"/api/v*/users",
						"/api/v*/registration",
						"/api/v*/registration/**",
						"api/v*/authenticate",
						"/swagger-ui.html",
						"/swagger-ui/**",
						"/api/v*/api-docs/**"
				).permitAll()
				.antMatchers("/**").hasRole(USER.name())
				.anyRequest().authenticated()
				.and()
				.exceptionHandling()
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin(frontURL);
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.addExposedHeader("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
