package com.alura.Challenge.forumhub.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.alura.Challenge.forumhub.dto.ListagemTopicoDto;
import com.alura.Challenge.forumhub.dto.TopicoDetalhamentoDto;
import com.alura.Challenge.forumhub.dto.TopicoDto;
import com.alura.Challenge.forumhub.models.Resposta;
import com.alura.Challenge.forumhub.models.Usuario;
import com.alura.Challenge.forumhub.services.TopicoService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TopicoControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private TopicoService topicoService;
	
	@Autowired
	private JacksonTester<TopicoDto> topicoPayloadJson;
	
	private static final String TITULO = "estou com duvida";
    private static final String MENSAGEM = "Me ajuda nessa duvida";
    private static final String CURSO = "Java";
    private static final LocalDateTime DATA_CRIACAO = LocalDateTime.now().withNano(0);
    private static final String AUTOR = "Antonio";
    private static final String STATUS_TOPICO = "NÃO RESPONDIDO";
    private static final List<String> RESPOSTA = List.of("Não sei a resposta", "Java é bom");
    
    private static final String PATH = "topicos";
    private static final Long ID = 1L;
    
    private List<TopicoDetalhamentoDto> topicos;
	
	
	@Test
	@DisplayName("Espera o status 400")
	@WithMockUser
	void deveRetornarStatus400SemCorpoRequisicao() throws Exception {
		
		var response = mvc.perform(post("/topicos")
				.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		verificarStatus(response, HttpStatus.BAD_REQUEST);
	}
	
	@Test
	@WithMockUser
	void deveCadastrarTopicoRetornarStatus200() throws Exception {
		var payload = criarTopicoDto();
		prepararCenarioParaSimulacaoDeCadastrarRecurso(payload);
		
		var response = obterRespostaSimulacaoCadastrarRecurso(payload, topicoPayloadJson);
		
		verificarStatus(response, HttpStatus.CREATED);
		verificarConteudoJson(response, CURSO, TITULO, MENSAGEM);
		
	}

	
	@Test
    @DisplayName("Deve retorna o status 200 e um tópico detalhado")
	@WithMockUser
    void deveRetornarUmTopico() throws Exception {		
		prepararCenarioParaSimulacaoDeObterRecurso();

		var response = obterRespostaSimulacaoDeObterRecurso();

		verificarStatus(response, HttpStatus.OK);
		verificarConteudoJson(response, CURSO, TITULO, MENSAGEM, AUTOR, STATUS_TOPICO);
	}

	
	@Test
    @DisplayName("Deve retorna o status 200 e um tópico detalhado")
	@WithMockUser
    void deveRetornarTodosTopicos() throws Exception {
		prepararCenarioParaSimulacaoDeObterTodosRecursos();
		
		var response = obterRespostaSimulacaoDeObterRecursos();
		
		verificarStatus(response, HttpStatus.OK);
		verificarConteudoJson(response, CURSO, TITULO, MENSAGEM, AUTOR, STATUS_TOPICO);
	}
	
	
	@Test
    @DisplayName("Deve retorna o status 200 e um tópico detalhado")
	@WithMockUser
    void deveRetornarUmTopicoAtualizado() throws Exception {
		var payload = criarTopicoDto();
		prepararCenarioParaSimulacaoDeAtualizaraRecurso(payload);
		
		var response = obterRespostaSimulacaoDeAtualizarRecurso(payload, topicoPayloadJson);
		
		verificarStatus(response, HttpStatus.OK);
		var tituloAtualizado = "Deus está conosco";
		verificarConteudoJson(response, tituloAtualizado);
	}	
	
	@Test
    @DisplayName("Deve retorna o status 204 e remova um topico")
	@WithMockUser
	void removaTopicoComSuccesso() throws Exception {
		doNothing().when(topicoService).removePorId(ID);
		
		var response = obterRespostaSimulacaoDeRemoverRecurso();
		
		verificarStatus(response, HttpStatus.NO_CONTENT);
	}
	
	
	private void prepararCenarioParaSimulacaoDeAtualizaraRecurso(TopicoDto payload) {
		ListagemTopicoDto topicoAtualizado = new ListagemTopicoDto(
				ID, "Deus está conosco", MENSAGEM, DATA_CRIACAO);
		when(topicoService.atualizar(payload, ID)).thenReturn(topicoAtualizado);
		
	}

	private void prepararCenarioParaSimulacaoDeObterTodosRecursos() {
		var topicosListagem = obterTopicos();
		Pageable paginacao = PageRequest.of(0, 10);
		Page<ListagemTopicoDto> page = new PageImpl<>(topicosListagem, paginacao, topicosListagem.size());
	
		when(topicoService.obterTopicos(paginacao)).thenReturn(page);
	}
	
	
	private List<Resposta> criaListaDeRespostas() {
		return new ArrayList<>();
	}
	
	private List<ListagemTopicoDto> obterTopicos() {		 
		return List.of(
				criarTopicoListagemDto(1L),
				criarTopicoListagemDto(2L)
				);
	}

	private void prepararCenarioParaSimulacaoDeCadastrarRecurso(TopicoDto payload) {
		ListagemTopicoDto topicoDetalhado = criarListagemTopicoDto(ID);
		when(topicoService.cadastrar(payload)).thenReturn(topicoDetalhado);
	}
	
	private ListagemTopicoDto criarListagemTopicoDto(Long id) {
		return new ListagemTopicoDto(id, TITULO, MENSAGEM, DATA_CRIACAO);
	}
	
	private void prepararCenarioParaSimulacaoDeObterRecurso() {
	    var topicoDetalhado = criarTopicoDetalhadoDto(ID);
	    when(topicoService.obterPorId(ID)).thenReturn(topicoDetalhado);
	}
	
	private MockHttpServletResponse obterRespostaSimulacaoDeRemoverRecurso() throws Exception {
		return mvc.perform(delete("/" + "topicos" + "/{id}", ID)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
				.andReturn().getResponse();
	}
	
	
	
	private MockHttpServletResponse obterRespostaSimulacaoDeObterRecursos() throws Exception {
		return mvc.perform(get("/" + "topicos")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", "0")
                .param("size", "10")
				.characterEncoding("UTF-8"))
				.andReturn().getResponse();
	}
	
	private <T> MockHttpServletResponse obterRespostaSimulacaoDeAtualizarRecurso(T payload, JacksonTester<T> payloadJson) throws Exception {
		return mvc.perform(put("/" + PATH + "/{id}", ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(criarJsonEsperado(payload, payloadJson))
				.characterEncoding("UTF-8"))
				.andReturn().getResponse();
	}
	
	private <T> MockHttpServletResponse obterRespostaSimulacaoCadastrarRecurso(T payload, JacksonTester<T> payloadJson) throws Exception {
		return mvc.perform(post("/" + PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(criarJsonEsperado(payload, payloadJson))
				.characterEncoding("UTF-8"))
				.andReturn().getResponse();
	}
	
	private MockHttpServletResponse obterRespostaSimulacaoDeObterRecurso() throws Exception {
		return mvc.perform(get("/" + "topicos" + "/{id}", ID)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
				.andReturn().getResponse();
	}
	
	
	
	
	private void verificarConteudoJson(MockHttpServletResponse response, String... conteudosEsperados) throws UnsupportedEncodingException {
		var responseBody = response.getContentAsString(StandardCharsets.UTF_8);
		System.out.println(responseBody);
		for (String conteudo : conteudosEsperados) {
            assertThat(responseBody).contains(conteudo);
        }
	}

	private void verificarStatus(MockHttpServletResponse response, HttpStatus status) {
		 assertThat(response.getStatus()).isEqualTo(status.value());
	}
	
	private TopicoDto criarTopicoDto() {
        return new TopicoDto(TITULO, MENSAGEM, CURSO);
    }
	
	private TopicoDetalhamentoDto criarTopicoDetalhadoDto(Long id) {
        return new TopicoDetalhamentoDto(id, TITULO, MENSAGEM, 
        		DATA_CRIACAO, AUTOR, STATUS_TOPICO, 
            criaListaDeRespostas());
    }
	
	private ListagemTopicoDto criarTopicoListagemDto(Long id) {
        return new ListagemTopicoDto(id, TITULO, MENSAGEM, 
        		DATA_CRIACAO);
    }
	
	private <T> String criarJsonEsperado(T payload, JacksonTester<T> criadorJson) throws IOException {
        return criadorJson.write(payload).getJson();
    }
	


}
