package com.alura.Challenge.forumhub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alura.Challenge.forumhub.models.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

	boolean existsByTituloAndMensagem(String titulo, String mensagem);

}
