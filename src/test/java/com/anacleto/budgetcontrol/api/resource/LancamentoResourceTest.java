package com.anacleto.budgetcontrol.api.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.anacleto.budgetcontrol.api.model.Lancamento;
import com.anacleto.budgetcontrol.api.model.LancamentoMock;
import com.anacleto.budgetcontrol.api.repository.LancamentoRepository;
import com.anacleto.budgetcontrol.api.repository.PessoaRepository;
import com.anacleto.budgetcontrol.api.repository.filter.LancamentoFilter;
import com.anacleto.budgetcontrol.api.service.LancamentoService;

@RunWith(SpringRunner.class)
@WebMvcTest({ LancamentoResource.class, LancamentoService.class, PessoaRepository.class })
@WithMockUser
public class LancamentoResourceTest {

	@MockBean
	private LancamentoRepository mockRepository;
	
	@MockBean 
	private LancamentoService mockService;
	
	@MockBean
	private org.springframework.data.domain.Pageable pageable;
	
	@MockBean
	private HttpServletResponse response;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private LancamentoResource lancamentoResource;
	
	private final Lancamento lancamentoMock = LancamentoMock.criaMockLancamento();
	private final Page<Lancamento> pageImpl = new PageImpl<>(Arrays.asList(lancamentoMock));
	
	@Test
	public void getAllLancamentosTest() {
		when(mockRepository.filtrar(any(), any())).thenReturn(pageImpl);
		
		Page<Lancamento> lancamentoTest = lancamentoResource.pesquisarLancamento(any(), any());
		
		assertEquals("descricaoTest", lancamentoTest.getContent().get(0).getDescricao());
		assertTrue(lancamentoTest.getContent().get(0).getPessoa().getAtivo());
	}
	
	@Test
	public void getLancamentoByDescricaoTest() {
		LancamentoFilter filter = new LancamentoFilter();
		filter.setDescricao("descricaoTest");
		
		when(mockRepository.filtrar(any(), any())).thenReturn(pageImpl);
		
		Page<Lancamento> lancamentoTest = lancamentoResource.pesquisarLancamento(filter, pageable);
		
		assertEquals("descricaoTest", lancamentoTest.getContent().get(0).getDescricao());
		assertTrue(lancamentoTest.getContent().get(0).getPessoa().getAtivo());
	}

	@Test
	public void whenGetLancamentoByCodigoExistShouldReturnLancamentoJsonTest() throws Exception {
		when(mockRepository.findById(anyLong())).thenReturn(Optional.of(lancamentoMock));
		
		this.mockMvc.perform(get("/lancamentos/{codigo}", anyLong())
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.codigo", is(2)))
						.andExpect(jsonPath("$.descricao", is(lancamentoMock.getDescricao())))
						.andExpect(jsonPath("$.pessoa.ativo", is(Boolean.TRUE))); 
	}
	
	@Test
	public void whenGetLancamentoByCodigoDoesnotExistShouldReturnNoContentTest() throws Exception {
		when(mockRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		this.mockMvc.perform(get("/lancamentos/{codigo}", anyLong())
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isNotFound());
	} 
	
	@Test
	public void criarLancamentoMockMvcTest() throws Exception {
		when(mockService.salvar(any())).thenReturn(lancamentoMock);
		
		this.mockMvc.perform(post("/lancamentos")
						.content("{\"descricao\" : \"Test\","
								+ "\"dataVencimento\" : \"2019-04-10\","
								+ "\"valor\" : \"1000.0\","
								+ "\"tipo\" : \"RECEITA\","
								+ "\"categoria\" : {\"codigo\" : \"5\"},"
								+ "\"pessoa\" : {\"codigo\" : \"1\"}}")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isCreated())
						.andExpect(jsonPath("$.descricao", is(lancamentoMock.getDescricao())))
						.andExpect(jsonPath("$.valor", is(lancamentoMock.getValor())));
	} 
	
	@Test
	public void criaLancamentoTest() {
		when(mockService.salvar(any())).thenReturn(lancamentoMock);
		
		lancamentoResource.criarLancamento(lancamentoMock, response);
		
		assertEquals("descricaoTest", lancamentoMock.getDescricao());
		assertTrue(lancamentoMock.getPessoa().getAtivo());
	}
	
	@Test
	public void whenRemoveLancamentoIsCalledWithCodigoThatDoesntExist_ThenItShouldThrowEmptyResultDataAccessExceptionTest() throws Exception {
		doThrow(EmptyResultDataAccessException.class).when(mockRepository).deleteById(anyLong());
		
		try {
			lancamentoResource.removerLancamento(-1L);
			
			fail("It Should throws EmptyResultDataAccessException");
		} catch (EmptyResultDataAccessException e) {
			assertSame(EmptyResultDataAccessException.class, e.getClass());
		}
	}
	
	@Test
	public void whenRemoveLancamentoIsCalledWithCodigoThatExist_ThenItShouldBeRemovedSuccessfullyTest() throws Exception {
		doNothing().when(mockRepository).deleteById(anyLong());
		
		this.mockMvc.perform(delete("/lancamentos/{codigo}", anyLong())
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isNoContent());
	}
}
