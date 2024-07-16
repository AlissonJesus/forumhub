package com.alura.Challenge.forumhub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alura.Challenge.forumhub.repositories.UsuarioRepository;

@Service
public class AutenticationService implements UserDetailsService {
	@Autowired
	private UsuarioRepository repositorio;
	 
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { 
		return repositorio.findByEmail(email);
	}

}
