package com.alura.Challenge.forumhub.models;


import java.time.LocalDateTime;
import java.util.List;

import com.alura.Challenge.forumhub.dto.TopicoDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Topico")
@Table(name = "topicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String titulo;
	private String mensagem;
	private LocalDateTime dataCriacao;
	
	@Column(name = "estado")
	private String status;
	
	
	@ManyToOne
	private Usuario autor;
	
	@ManyToOne
	private Curso curso;
	
	@OneToMany(mappedBy = "topico")
	private List<Resposta> respostas;
	
	
	public Topico(TopicoDto dadosCadastro) {
		titulo = dadosCadastro.titulo();
		mensagem = dadosCadastro.mensagem();
		status = "Não_Respondido";
		dataCriacao = LocalDateTime.now();
	}

	public void atualizar(TopicoDto dados) {
		if(dados.titulo() != null) titulo = dados.titulo();
		if(dados.mensagem() != null) mensagem = dados.mensagem();
		
	}
}
