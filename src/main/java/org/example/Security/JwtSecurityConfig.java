package org.example.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class JwtSecurityConfig {

	@Autowired
	private JwtAuthenticationEntryPoint point;
	@Autowired
	private JwtAuthenticationFilter filter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(AbstractHttpConfigurer :: disable).
				authorizeRequests().requestMatchers("/products").hasRole("Admin")
				.requestMatchers("/product").hasRole("Admin")
				.requestMatchers("/countProducts").hasRole("Admin")
				.requestMatchers("/deleteProduct").hasRole("Admin")
				.requestMatchers("/users").hasRole("Admin")
				.requestMatchers("/user").hasRole("Admin")
				.requestMatchers("/countUsers").hasRole("Admin")
				.requestMatchers("/deleteUser").hasRole("Admin")
				.requestMatchers("/units").hasRole("Admin")
				.requestMatchers("/unit").hasRole("Admin")
				.requestMatchers("/deleteUnit").hasRole("Admin")
				.requestMatchers("/store").hasRole("Admin")
				.requestMatchers("/deleteStore").hasRole("Admin")
				.requestMatchers("/purchases").hasAnyRole("Purchase", "Admin")
				.requestMatchers("/purchase").hasAnyRole("Purchase", "Admin")
				.requestMatchers("/countPurchase").hasAnyRole("Purchase", "Admin")
				.requestMatchers("/deletePurchase").hasAnyRole("Purchase", "Admin")
				.requestMatchers("/sales").hasAnyRole("Sales", "Admin")
				.requestMatchers("/countSales").hasAnyRole("Sales", "Admin")
				.requestMatchers("/deleteSales").hasAnyRole("Sales", "Admin")
				.requestMatchers("/auth/login").permitAll()
				.requestMatchers("/auth/logout").permitAll()
				.anyRequest().authenticated()
				.and()
				.exceptionHandling(ex -> ex.authenticationEntryPoint(point))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}


}
