package com.alura.Challenge.forumhub.dto;

import java.time.LocalDateTime;

import com.alura.Challenge.forumhub.models.Topico;

public record ListagemTopicoDto(Long id, String titulo, String mensagem, LocalDateTime dataCriacao) {
	public ListagemTopicoDto(Topico topico) {
		this(topico.getId(), topico.getTitulo(), topico.getMensagem(), topico.getDataCriacao());
	}

}
