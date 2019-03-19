package com.anacleto.budgetcontrol.api.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.anacleto.budgetcontrol.api.model.Lancamento;
import com.anacleto.budgetcontrol.api.model.LancamentoMock;
import com.anacleto.budgetcontrol.api.repository.LancamentoRepository;
import com.anacleto.budgetcontrol.api.repository.PessoaRepository;
import com.anacleto.budgetcontrol.api.service.LancamentoService;

@RunWith(SpringRunner.class)
@WebMvcTest({ LancamentoResource.class, LancamentoService.class, PessoaRepository.class })
public class LancamentoResourceTest {

	@MockBean
	private LancamentoRepository mockRepository;
	
	@MockBean 
	private LancamentoService mockService;
	
	@MockBean
	private HttpServletResponse response;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private LancamentoResource lancamentoResource;
	
	private Lancamento lancamentoMock = LancamentoMock.criaMockLancamento();
	
	@Test
	public void getAllLancamentosTest() {
		when(mockRepository.findAll()).thenReturn(Arrays.asList(lancamentoMock));
		
		Lancamento lancamentoTest = lancamentoResource.getAllLancamentos().get(0);
		
		assertEquals("descricaoTest", lancamentoTest.getDescricao());
		assertTrue(lancamentoTest.getPessoa().getAtivo());
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
	public void craiLancamentoTest() {
		when(mockService.salvar(any())).thenReturn(lancamentoMock);
		
		lancamentoResource.criarLancamento(lancamentoMock, response);
		
		assertEquals("descricaoTest", lancamentoMock.getDescricao());
		assertTrue(lancamentoMock.getPessoa().getAtivo());
	} 
}
