package com.alura.Challenge.forumhub.services;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.alura.Challenge.forumhub.dto.ListagemTopicoDto;
import com.alura.Challenge.forumhub.dto.TopicoDetalhamentoDto;
import com.alura.Challenge.forumhub.dto.TopicoDto;
import com.alura.Challenge.forumhub.exception.ValidacaoException;
import com.alura.Challenge.forumhub.models.Curso;
import com.alura.Challenge.forumhub.models.Resposta;
import com.alura.Challenge.forumhub.models.Topico;
import com.alura.Challenge.forumhub.models.Usuario;
import com.alura.Challenge.forumhub.repositories.TopicoRepository;

import jakarta.validation.Valid;

@Service
public class TopicoService {
	
	@Autowired
	private TopicoRepository topicoRepository;

	public ListagemTopicoDto cadastrar(@Valid TopicoDto dados) {
		valideTopicoUnico(dados);
		
		var topico = new Topico(dados);
		topico.setAutor(obterUsuario());
		topico.setCurso(obterCurso());
		List<Resposta> listaVazia = new ArrayList<>();
		topico.setRespostas(listaVazia);
		
		var topicoCadastrado = topicoRepository.save(topico);
		return new ListagemTopicoDto(topicoCadastrado);
	}


	private Curso obterCurso() {
		var curso = new Curso(1L, "Java", "programacao", null);
		return curso;
	}


	private void valideTopicoUnico(TopicoDto topico) {
		var topicoExiste = topicoRepository.existsByTituloAndMensagem(topico.titulo(), topico.mensagem());
		if(topicoExiste) {
			throw new ValidacaoException("Topico já existe"); 
		}
	}


	public TopicoDetalhamentoDto obterPorId(Long id) {
		var topicoEncontrado = topicoRepository.getReferenceById(id);
		topicoEncontrado.setAutor(obterUsuario());
		topicoEncontrado.setCurso(obterCurso());
		List<Resposta> listaVazia = new ArrayList<>();
		topicoEncontrado.setRespostas(listaVazia);
		return new TopicoDetalhamentoDto(topicoEncontrado);
	}

	
	public Page<ListagemTopicoDto> obterTopicos(Pageable paginacao) {
		var page = topicoRepository.findAll(paginacao);
		return page.map(ListagemTopicoDto::new);
	}
	

	public ListagemTopicoDto atualizar(TopicoDto payload, Long id) {
		valideTopicoUnico(payload);
		Topico topico = topicoRepository.getReferenceById(id);
		topico.atualizar(payload);
		return new ListagemTopicoDto(topico);
	}
	
	public void removePorId(Long id) {
		topicoRepository.deleteById(id);
	}
	
	public Usuario obterUsuario() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario) authentication.getPrincipal();
	}

}
