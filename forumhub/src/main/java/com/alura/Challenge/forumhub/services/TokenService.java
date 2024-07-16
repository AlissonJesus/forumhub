package com.alura.Challenge.forumhub.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alura.Challenge.forumhub.models.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Service
public class TokenService {
	@Value(value = "${api.security.token.secret}")
	private String secret;
	
	public String gerarToken(Usuario usuario) {
		System.out.println("Token dentro. Lá ele");
		
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    return JWT.create()
		        .withIssuer("API forum")
		        .withSubject(usuario.getEmail())
		        .withClaim("id", usuario.getId())
		        .withExpiresAt(dataExpiracao())
		        .sign(algorithm);
		} catch (JWTCreationException exception){
			throw new RuntimeException("Erro para gerar token JWT", exception);
		}
	}
	
	public String getSubject(String tokenJWT) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256(secret);
		    JWTVerifier verifier = JWT.require(algorithm)
		        // specify any specific claim validations
		        .withIssuer("API forum")
		        // reusable verifier instance
		        .build();
		      
		    DecodedJWT decodedJWT = verifier.verify(tokenJWT);
		    return decodedJWT.getSubject();
		    
		} catch (JWTVerificationException exception){
		    throw new RuntimeException("Token inválido");
		}
	}

	private Instant dataExpiracao() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}
