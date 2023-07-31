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
				authorizeRequests().requestMatchers("/products").authenticated()
				.requestMatchers("/product").authenticated()
				.requestMatchers("/countProducts").authenticated()
				.requestMatchers("/deleteProduct").authenticated()
				.requestMatchers("/users").authenticated()
				.requestMatchers("/user").authenticated()
				.requestMatchers("/countUsers").authenticated()
				.requestMatchers("/deleteUser").authenticated()
				.requestMatchers("/units").authenticated()
				.requestMatchers("/unit").authenticated()
				.requestMatchers("/deleteUnit").authenticated()
				.requestMatchers("/store").authenticated()
				.requestMatchers("/deleteStore").authenticated()
				.requestMatchers("/purchases").authenticated()
				.requestMatchers("/purchase").authenticated()
				.requestMatchers("/countPurchase").authenticated()
				.requestMatchers("/deletePurchase").authenticated()
				.requestMatchers("/sales").authenticated()
				.requestMatchers("/countSales").authenticated()
				.requestMatchers("/deleteSales").authenticated()
				.requestMatchers("/auth/login").permitAll()
				.anyRequest().authenticated()
				.and()
				.exceptionHandling(ex -> ex.authenticationEntryPoint(point))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}


}
