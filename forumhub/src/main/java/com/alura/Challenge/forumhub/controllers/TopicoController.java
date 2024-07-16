package com.alura.Challenge.forumhub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alura.Challenge.forumhub.dto.ListagemTopicoDto;
import com.alura.Challenge.forumhub.dto.TopicoDetalhamentoDto;
import com.alura.Challenge.forumhub.dto.TopicoDto;
import com.alura.Challenge.forumhub.services.TopicoService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("topicos")
public class TopicoController {
	@Autowired
	private TopicoService topicoService;

	@PostMapping
	@Transactional
	public ResponseEntity<ListagemTopicoDto> cadastrar(@RequestBody @Valid TopicoDto dados) {
		ListagemTopicoDto topicoDetalhado = topicoService.cadastrar(dados);
		return ResponseEntity.created(null).body(topicoDetalhado);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TopicoDetalhamentoDto> obterPorId(@PathVariable Long id) {
		TopicoDetalhamentoDto topicoDetalhado = topicoService.obterPorId(id);
		return ResponseEntity.ok(topicoDetalhado);
	}
	
	@GetMapping
	public ResponseEntity<Page<ListagemTopicoDto>> obterTopicos(Pageable paginacao) {
		Page<ListagemTopicoDto> page = topicoService.obterTopicos(paginacao);
		return ResponseEntity.ok(page);
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<ListagemTopicoDto> atualizar(@RequestBody @Valid TopicoDto payload, @PathVariable Long id) {
		ListagemTopicoDto topicoAtualizado = topicoService.atualizar(payload, id);
		return ResponseEntity.ok(topicoAtualizado);
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> removerPorId(@PathVariable Long id) {
		topicoService.removePorId(id);
		return ResponseEntity.noContent().build();
	}
}
