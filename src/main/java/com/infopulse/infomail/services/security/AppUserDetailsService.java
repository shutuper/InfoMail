package com.infopulse.infomail.services.security;

import com.infopulse.infomail.repositories.security.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

	private AppUserRepository appUserRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return appUserRepository
				.findAppUserByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("User %s doesn't exist", email)));
	}
}
