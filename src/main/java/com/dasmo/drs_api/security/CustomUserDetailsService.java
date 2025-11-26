package com.dasmo.drs_api.security;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dasmo.drs_api.model.User;
import com.dasmo.drs_api.model.UserPrincipal;
import com.dasmo.drs_api.repo.UserRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepo userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//find the current user to the db record
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

		// collect and set the authorities for the principal
		// Sample output for this iteration
		//		[
		//		  ROLE_ADMIN,
		//		  READ_USER,
		//		  WRITE_USER,
		//		  ROLE_AUDITOR,
		//		  READ_REPORT
		//		]
		Set<GrantedAuthority> authorities = user.getRoles().stream()
				.flatMap(role -> {
					Stream<GrantedAuthority> roleStream = Stream.of(
							new SimpleGrantedAuthority("ROLE_" + role.getName())
							);
					Stream<GrantedAuthority> permissionStream = role.getPermissions().stream()
							.map(permission -> new SimpleGrantedAuthority(permission.getName()));

					return Stream.concat(roleStream, permissionStream);

				}).collect(Collectors.toSet());


		return new UserPrincipal(user, authorities);
	}
}