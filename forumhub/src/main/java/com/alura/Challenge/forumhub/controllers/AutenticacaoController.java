package com.alura.Challenge.forumhub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alura.Challenge.forumhub.dto.AutenticacaoDto;
import com.alura.Challenge.forumhub.dto.TokenJwtDto;
import com.alura.Challenge.forumhub.models.Usuario;
import com.alura.Challenge.forumhub.services.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("login")
public class AutenticacaoController {
	
	@Autowired
	private AuthenticationManager manager;
	@Autowired
	private TokenService tokenService;
	
	
	@PostMapping
	public ResponseEntity logar(@RequestBody @Valid AutenticacaoDto payload) {
		
		var AuthenticationToken = new UsernamePasswordAuthenticationToken(payload.email(), payload.senha());
	
		var autentication = manager.authenticate(AuthenticationToken);

		var tokenJWT = tokenService.gerarToken((Usuario) autentication.getPrincipal());

		return ResponseEntity.ok(new TokenJwtDto(tokenJWT));
	}
}
