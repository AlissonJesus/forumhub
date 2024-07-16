package com.alura.Challenge.forumhub.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.alura.Challenge.forumhub.models.Resposta;
import com.alura.Challenge.forumhub.models.Topico;

public record TopicoDetalhamentoDto(
		Long id,
		String titulo,
		String mensagem,
		LocalDateTime dataCriacao,
		String nomeAutor,
		String status,
		List<Resposta> respostas 
		
		
		) {

	
	public TopicoDetalhamentoDto(Topico topico) {
		this(
				topico.getId(), 
				topico.getTitulo(), 
				topico.getMensagem(),  
				topico.getDataCriacao(),
				topico.getAutor().getNome(),
				topico.getStatus(),
				topico.getRespostas());
	}

}
