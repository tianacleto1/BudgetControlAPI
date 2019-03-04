package com.anacleto.budgetcontrol.api.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.anacleto.budgetcontrol.api.model.Endereco;
import com.anacleto.budgetcontrol.api.model.Pessoa;
import com.anacleto.budgetcontrol.api.repository.PessoaRepository;
import com.anacleto.budgetcontrol.api.service.PessoaService;

@RunWith(SpringRunner.class)
@WebMvcTest({ PessoaResource.class, PessoaService.class })
public class PessoaResourceTest {
	
	@MockBean
	private PessoaRepository mockRepository;
	
	@MockBean
	private PessoaService mockService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PessoaResource pessoaResource;
	
	private final Pessoa pessoaMock = criaMockPessoa();
	
	@Test
	public void getAllPessoasTest() {
		List<Pessoa> pessoas = new ArrayList<>();
		pessoas.add(pessoaMock);
		
		when(mockRepository.findAll()).thenReturn(pessoas);
		Pessoa pessoaTest = pessoaResource.listarPessoas().get(0);
		
		assertEquals("NomeTest", pessoaTest.getNome());
		assertFalse(pessoaTest.getAtivo());
	}
	
	@Test
	public void whenGetPessoaByCodigoExistShouldReturnPessoaJsonTest() throws Exception {
		when(mockRepository.findById(anyLong())).thenReturn(Optional.of(pessoaMock));
		
		this.mockMvc.perform(get("/pessoas/{codigo}", anyLong())
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.codigo", is(1)))
						.andExpect(jsonPath("$.nome", is(pessoaMock.getNome())))
						.andExpect(jsonPath("$.ativo", is(Boolean.FALSE)))
						.andExpect(jsonPath("$.endereco.logradouro", is(pessoaMock.getEndereco()
																			      .getLogradouro())));
	}
	
	@Test
	public void whenGetPessoaByCodigoDoesnotExistShouldReturnNoContentTest() throws Exception {
		when(mockRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		this.mockMvc.perform(get("/pessoas/{codigo}", anyLong())
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isNotFound());
	}
	
	@Test
	public void criarPessoaTest() throws Exception {
		when(mockRepository.save(any())).thenReturn(pessoaMock);
		
		this.mockMvc.perform(post("/pessoas")
						.content("{\"nome\" : \"Fulano\",\"ativo\" : \"false\"}")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isCreated())
						.andExpect(jsonPath("$.nome", is(pessoaMock.getNome())))
						.andExpect(jsonPath("$.ativo", is(Boolean.FALSE)));
	}
	
	@Test
	public void removerPessoaTest() throws Exception {
		doNothing().when(mockRepository).deleteById(anyLong());
		
		this.mockMvc.perform(delete("/pessoas/{codigo}", anyLong())
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isNoContent());
	}
	
	
	@Test
	public void atualizarPessoaTest() throws Exception {
		when(mockService.atualizar(anyLong(), any())).thenReturn(pessoaMock);
		
		this.mockMvc.perform(put("/pessoas/{codigo}", anyLong(), any())
						.content("{\"nome\" : \"Fulano\",\"ativo\" : \"false\"}")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk());
	} 
	
	private Pessoa criaMockPessoa() {
		Pessoa pessoaMock = new Pessoa();
		pessoaMock.setCodigo(1L);
		pessoaMock.setNome("NomeTest");
		pessoaMock.setAtivo(Boolean.FALSE);
		
		Endereco enderecoMock = new Endereco();
		enderecoMock.setLogradouro("LogradouroTest");
		enderecoMock.setNumero("numeroTest");
		enderecoMock.setComplemento("complementoTest");
		enderecoMock.setBairro("bairroTest");
		enderecoMock.setCep("cepTest");
		enderecoMock.setCidade("cidadeTest");
		enderecoMock.setCidade("cidadeTest");
		
		pessoaMock.setEndereco(enderecoMock);
		
		return pessoaMock;
	}
}
