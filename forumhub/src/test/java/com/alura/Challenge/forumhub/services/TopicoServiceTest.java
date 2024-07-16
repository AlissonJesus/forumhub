package com.alura.Challenge.forumhub.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.alura.Challenge.forumhub.dto.ListagemTopicoDto;
import com.alura.Challenge.forumhub.dto.TopicoDetalhamentoDto;
import com.alura.Challenge.forumhub.dto.TopicoDto;
import com.alura.Challenge.forumhub.models.Curso;
import com.alura.Challenge.forumhub.models.Resposta;
import com.alura.Challenge.forumhub.models.Topico;
import com.alura.Challenge.forumhub.models.Usuario;
import com.alura.Challenge.forumhub.repositories.TopicoRepository;

@ExtendWith(MockitoExtension.class)
class TopicoServiceTest {
	
	@Mock
	private TopicoRepository topicoRepository;
	
	@InjectMocks
	private TopicoService topicoService;
	
	private static final String TITULO = "estou com duvida";
    private static final String MENSAGEM = "Me ajuda nessa duvida";
    private static final String CURSO = "Java";
    private static final LocalDateTime DATA_CRIACAO = LocalDateTime.now().withNano(0);
    private static final String AUTOR = "Antonio";
    private static final String STATUS_TOPICO = "Não_Respondido";
    
    private static final Long ID = 1L;

	@Test
	void deveCadastrarComSucesso() {
		var dto = criarTopicoDto();
		var topicoCadastrado = criarTopicoDetalhado(dto);
		
		when(topicoRepository.existsByTituloAndMensagem(dto.titulo(), dto.mensagem())).thenReturn(false);
		when(topicoRepository.save(any(Topico.class))).thenReturn(topicoCadastrado);
		
		var topicoDetalhadoDto = topicoService.cadastrar(dto); 
		
		//verificaTopicoDetalhado(dto, topicoDetalhadoDto);
		verificarComportamentoMockCadastrar(dto);
		
	}
	
	@Test
	void deveObterTopicoComSucesso() {
		var dto = criarTopicoDto();
		var topicoEncontrado = criarTopicoDetalhado(dto);
		
		when(topicoRepository.getReferenceById(ID)).thenReturn(topicoEncontrado);
		
		var topicoEncontradoDto = topicoService.obterPorId(ID);
		
		verificaTopicoDetalhado(dto, topicoEncontradoDto);
		verificarComportamentoMockObterTopico();
		
	}
	
	@Test
	void deveObterListaDeTopicosComSucesso() {
		var topicos = obterTopicos();
		Pageable paginacao = PageRequest.of(0, 10);
		Page<Topico> page = new PageImpl<Topico>(topicos, paginacao, topicos.size());
		
		
		when(topicoRepository.findAll(paginacao)).thenReturn(page);
		
		var pagina = topicoService.obterTopicos(paginacao);
		
		verificaListaDeTopicos(topicos, pagina);
		verificarComportamentoMockObterTopicos(paginacao);
		
	}
	
	@Test
	void deveAtualizarTopicoComSucesso() {
		var dto = criarTopicoDto("Novo titulo", "Nova mensagem");
		var topicoDesatualizado = criarTopicoModel(ID, TITULO, MENSAGEM);
		
		when(topicoRepository.existsByTituloAndMensagem(dto.titulo(), dto.mensagem())).thenReturn(false);
		when(topicoRepository.getReferenceById(ID)).thenReturn(topicoDesatualizado);
		
		ListagemTopicoDto topicoAtualizado = topicoService.atualizar(dto, ID);
		
		verificarCamposEsperadosAtualizados(dto, topicoAtualizado);
		verificarComportamentoMockAtualizar(dto);
	}
	
	@Test()
	void deveRemoverTopico() {
		doNothing().when(topicoRepository).deleteById(ID);
		topicoService.removePorId(ID);
		verify(topicoRepository, times(1)).deleteById(ID);;
	}
	
	private void verificarComportamentoMockAtualizar(TopicoDto dto) {
		verify(topicoRepository, times(1)).existsByTituloAndMensagem(dto.titulo(), dto.mensagem());
		verify(topicoRepository, times(1)).getReferenceById(ID);
	}

	private void verificarCamposEsperadosAtualizados(TopicoDto dto, ListagemTopicoDto topicoAtualizado) {

		assertNotNull(topicoAtualizado.titulo());
		assertEquals(dto.titulo(), topicoAtualizado.titulo());
		assertEquals(dto.mensagem(), topicoAtualizado.mensagem());
	}

	private void verificarComportamentoMockObterTopicos(Pageable paginacao) {
		verify(topicoRepository, times(1)).findAll(paginacao);
	}

	private void verificaListaDeTopicos(List<Topico> topicos, Page<ListagemTopicoDto> pagina) {
		
		ListagemTopicoDto primeiroTopico = pagina.getContent().get(0);
		ListagemTopicoDto segundoTopico = pagina.getContent().get(1);
		
        assertNotNull(pagina);
        assertEquals(2, pagina.getContent().size());      
        assertEquals(TITULO, primeiroTopico.titulo());
        assertEquals(topicos.get(1).getTitulo(), segundoTopico.titulo());
	}

	private void verificarComportamentoMockObterTopico() {
		verify(topicoRepository, times(1)).getReferenceById(ID);
	}

	private void verificarComportamentoMockCadastrar(TopicoDto dto) {
		verify(topicoRepository, times(1)).existsByTituloAndMensagem(dto.titulo(), dto.mensagem());
		verify(topicoRepository, times(1)).save(any(Topico.class));		
	}

	private void verificaTopicoDetalhado(TopicoDto dto, TopicoDetalhamentoDto topicoDetalhadoDto) {
		assertNotNull(topicoDetalhadoDto);
		verificarCamposEsperados(dto, topicoDetalhadoDto);
	}
	
	
	
	

	private void verificarCamposEsperados(TopicoDto dto, TopicoDetalhamentoDto topicoDetalhadoDto) {
		assertEquals(dto.titulo(), topicoDetalhadoDto.titulo());
		assertEquals(dto.mensagem(), topicoDetalhadoDto.mensagem());
		assertEquals(AUTOR, topicoDetalhadoDto.nomeAutor());
		assertEquals(DATA_CRIACAO, topicoDetalhadoDto.dataCriacao());
		assertEquals(STATUS_TOPICO, topicoDetalhadoDto.status());
		
	}
	
	private List<Topico> obterTopicos() {	
		
		return List.of(
				criarTopicoModel(1L, TITULO, MENSAGEM),
				criarTopicoModel(2L, "Novo tiulo", "Nova mensagem")
				);
	}
	
	
	private Topico criarTopicoModel(Long id, String titulo, String mensagem) {
		var usuario = criarUsuario();
		var curso  = criarCurso();
		var respostas = criaListaDeRespostas();
		return new Topico(id, titulo, mensagem, DATA_CRIACAO, STATUS_TOPICO, usuario, curso, respostas);
	}

	private Curso criarCurso() {
		// TODO Auto-generated method stub
		return new Curso();
	}

	private List<Resposta> criaListaDeRespostas() {
		return new ArrayList<>();
	}

	private  Topico criarTopicoDetalhado(TopicoDto dto, Long id) {
		var topico = new Topico(dto);
		topico.setId(id);
		topico.setDataCriacao(DATA_CRIACAO);
		topico.setAutor(criarUsuario());
		return topico;
	}
	private  Topico criarTopicoDetalhado(TopicoDto dto) {
		return criarTopicoDetalhado(dto, ID);
	}
	
	
	private Usuario criarUsuario() {
		var usuario = new Usuario();
		usuario.setNome(AUTOR);
		return usuario;
	}
	
	
	private TopicoDto criarTopicoDto(String titulo, String mensagem) {
        return new TopicoDto(titulo, mensagem, CURSO);
    }
	
	private TopicoDto criarTopicoDto() {
        return criarTopicoDto(TITULO, MENSAGEM);
    }
	
	
	

}
