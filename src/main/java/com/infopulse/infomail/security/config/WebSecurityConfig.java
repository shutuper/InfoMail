package com.infopulse.infomail.security.config;

import com.infopulse.infomail.security.filters.AppAuthenticationFilter;
import com.infopulse.infomail.security.filters.AppAuthorizationFilter;
import com.infopulse.infomail.security.jwt.JwtUtil;
import com.infopulse.infomail.services.security.AppUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;
	private final AppUserDetailsService userDetailsService;
	private final AppAuthorizationFilter authorizationFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		final AppAuthenticationFilter authenticationFilter = new AppAuthenticationFilter(jwtUtil, authenticationManagerBean());
		http.cors().and().csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilter(authenticationFilter)
				.addFilterAfter(authorizationFilter, authenticationFilter.getClass())
				.authorizeRequests()
//				.antMatchers("/api/v*/registration/sayHi").hasRole(USER.name())
//				.antMatchers("/", "/api/v*/registration", "/api/v*/registration/**", "api/v*/authenticate").permitAll()
//				.antMatchers("/**").hasRole(USER.name())
				.antMatchers("/*").permitAll()
				.anyRequest().authenticated();
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
		config.addAllowedOrigin("http://localhost:4200");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
