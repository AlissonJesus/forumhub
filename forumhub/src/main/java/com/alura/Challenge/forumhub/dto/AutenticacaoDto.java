package com.alura.Challenge.forumhub.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AutenticacaoDto(
		@NotBlank
		@Email
		@JsonAlias("login")
		String email,
		@NotBlank
		String senha) {
}
