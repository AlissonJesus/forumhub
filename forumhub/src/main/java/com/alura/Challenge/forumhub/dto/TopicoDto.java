package com.alura.Challenge.forumhub.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.NotBlank;

public record TopicoDto(
		@NotBlank
		String titulo,
		@NotBlank
		String mensagem,
		
		@JsonAlias("curso")
		@NotBlank
		String nomeCurso
		) {
	
}
