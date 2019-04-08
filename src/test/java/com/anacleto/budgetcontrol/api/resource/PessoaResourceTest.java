package com.anacleto.budgetcontrol.api.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.anacleto.budgetcontrol.api.model.Pessoa;
import com.anacleto.budgetcontrol.api.model.PessoaMock;
import com.anacleto.budgetcontrol.api.repository.PessoaRepository;
import com.anacleto.budgetcontrol.api.service.PessoaService;

@RunWith(SpringRunner.class)
@WebMvcTest({ PessoaResource.class, PessoaService.class })
@WithMockUser
public class PessoaResourceTest {
	
	@MockBean
	private PessoaRepository mockRepository;
	
	@MockBean
	private PessoaService mockService;
	
	@MockBean
	private HttpServletResponse response;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PessoaResource pessoaResource;
	
	private final Pessoa pessoaMock = PessoaMock.criaMockPessoa();
	
	@Test
	public void getAllPessoasTest() {
		List<Pessoa> pessoas = Arrays.asList(pessoaMock);
		
		when(mockRepository.findAll()).thenReturn(pessoas);
		Pessoa pessoaTest = pessoaResource.listarPessoas().get(0);
		
		assertEquals("NomeTest", pessoaTest.getNome());
		assertTrue(pessoaTest.getAtivo());
	}
	
	@Test
	public void whenGetPessoaByCodigoExistShouldReturnPessoaJsonTest() throws Exception {
		when(mockService.buscarPessoaByCodigo(anyLong())).thenReturn(pessoaMock);
		
		this.mockMvc.perform(get("/pessoas/{codigo}", anyLong())
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.codigo", is(1)))
						.andExpect(jsonPath("$.nome", is(pessoaMock.getNome())))
						.andExpect(jsonPath("$.ativo", is(Boolean.TRUE)))
						.andExpect(jsonPath("$.endereco.logradouro", is(pessoaMock.getEndereco()
																			      .getLogradouro())));
	}
	
	@Test
	public void whenGetPessoaByCodigoDoesnotExistShouldThrowEmptyResultDataAccessExceptionAndReturnNoContentTest() throws Exception {
		when(mockService.buscarPessoaByCodigo(anyLong())).thenThrow(EmptyResultDataAccessException.class);
		
		this.mockMvc.perform(get("/pessoas/{codigo}", -1)
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isNotFound());
	}
	/*
	@Test
	public void criarPessoa_withMockMvcTest() throws Exception {
		when(mockRepository.save(any())).thenReturn(pessoaMock);
		
		this.mockMvc.perform(post("/pessoas")
						.content("{\"nome\" : \"Fulano\",\"ativo\" : \"false\"}")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isCreated())
						.andExpect(jsonPath("$.nome", is(pessoaMock.getNome())))
						.andExpect(jsonPath("$.ativo", is(Boolean.TRUE)));
	}*/
	
	@Test
	public void criarPessoaTest() {
		pessoaMock.setAtivo(false);
		when(mockRepository.save(any())).thenReturn(pessoaMock);
		
		ResponseEntity<Pessoa> resp = pessoaResource.criarPessoa(pessoaMock, response);
		
		assertFalse(resp.getBody().getAtivo());
	}
	/*
	@Test
	public void removerPessoa_withMockMvcTest() throws Exception {
		doNothing().when(mockRepository).deleteById(anyLong());
		
		this.mockMvc.perform(delete("/pessoas/{codigo}", anyLong())
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isNoContent());
	} */
	
	@Test
	public void removerPessoa_withMockMvcTest() {
		doNothing().when(mockRepository).deleteById(anyLong());
		
		pessoaResource.removerPessoa(1L);
	}
	
	/*
	@Test
	public void atualizarPessoa_withMockMvcTest() throws Exception {
		when(mockService.atualizar(anyLong(), any())).thenReturn(pessoaMock);
		
		this.mockMvc.perform(put("/pessoas/{codigo}", anyLong(), any())
						.content("{\"nome\" : \"Fulano\",\"ativo\" : \"false\"}")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk());
	} */
	
	@Test
	public void atualizarPessoaTest() {
		when(mockService.atualizar(anyLong(), any())).thenReturn(pessoaMock);
		
		ResponseEntity<Pessoa> responseEntity = pessoaResource.atualizarPessoa(1L, pessoaMock);
		
		assertEquals("NomeTest", responseEntity.getBody().getNome());
		assertTrue(responseEntity.getBody().getAtivo());
	}
	
	/*
	@Test
	public void atualizarPropriedadeAtivo_withMockMvcTest() throws Exception {
		doNothing().when(mockService).atualizarPropriedadeAtivo(anyLong(), any());
		
		this.mockMvc.perform(put("/pessoas/{codigo}/ativo", anyLong(), any())
						.content("true")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isNoContent());
	} */
	
	@Test
	public void atualizarPropriedadeAtivoTest() {
		doNothing().when(mockService).atualizarPropriedadeAtivo(anyLong(), any());
		
		pessoaResource.atualizarPropriedadeAtivo(1L, true);
	}
}
