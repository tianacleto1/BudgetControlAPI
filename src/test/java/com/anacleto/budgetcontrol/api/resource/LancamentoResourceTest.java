package com.anacleto.budgetcontrol.api.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.anacleto.budgetcontrol.api.model.Categoria;
import com.anacleto.budgetcontrol.api.model.Endereco;
import com.anacleto.budgetcontrol.api.model.Lancamento;
import com.anacleto.budgetcontrol.api.model.Pessoa;
import com.anacleto.budgetcontrol.api.model.TipoLancamento;
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
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private LancamentoResource lancamentoResource;
	
	private Lancamento lancamentoMock = criaMockLancamento();
	
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
	
	private Lancamento criaMockLancamento() {
		Lancamento lancamentoMock = new Lancamento();
		lancamentoMock.setCodigo(2L);
		lancamentoMock.setDescricao("descricaoTest");
		lancamentoMock.setDataVencimento(LocalDate.of(2017, 2, 9));
		lancamentoMock.setDataPagamento(LocalDate.of(2017, 2, 9));
		lancamentoMock.setValor(new BigDecimal(100.32));
		lancamentoMock.setTipo(TipoLancamento.DESPESA);
		
		Categoria categoriaMock = new Categoria();
		categoriaMock.setCodigo(2L);
		categoriaMock.setNome("Supermercado");
		
		lancamentoMock.setCategoria(categoriaMock);
		
		Pessoa pessoaMock = new Pessoa();
		pessoaMock.setCodigo(2L);
		pessoaMock.setNome("nomeTest");
		Endereco enderecoMock = new Endereco();
		enderecoMock.setLogradouro("logradouroTest");
		enderecoMock.setNumero("100");
		enderecoMock.setBairro("bairroTest");
		enderecoMock.setCep("cepTest");
		enderecoMock.setCidade("cidadeTest");
		enderecoMock.setEstado("estadoTest");
		
		pessoaMock.setEndereco(enderecoMock);
		pessoaMock.setAtivo(true);
		
		lancamentoMock.setPessoa(pessoaMock);
		
		return lancamentoMock;
	}
}
