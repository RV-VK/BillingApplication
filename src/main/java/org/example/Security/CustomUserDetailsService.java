package org.example.Security;

import org.example.Entity.User;
import org.example.Mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserMapper userMapper;
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		User user = userMapper.findByUsername(username);
		if(user == null)
			throw new UsernameNotFoundException("User Not Found!");
		return org.springframework.security.core.userdetails.User.builder()
				.username(user.getUserName())
				.password(user.getPassWord())
				.roles(user.getUserType())
				.build();
	}
}
