package com.anacleto.budgetcontrol.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.anacleto.budgetcontrol.api.model.Categoria;
import com.anacleto.budgetcontrol.api.model.Endereco;
import com.anacleto.budgetcontrol.api.model.Lancamento;
import com.anacleto.budgetcontrol.api.model.Pessoa;
import com.anacleto.budgetcontrol.api.model.TipoLancamento;
import com.anacleto.budgetcontrol.api.repository.LancamentoRepository;
import com.anacleto.budgetcontrol.api.repository.PessoaRepository;
import com.anacleto.budgetcontrol.api.service.exception.PessoaInexistenteOuInativaException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LancamentoServiceTest {
	
	@MockBean
	private LancamentoRepository mockLancamentoRepository;
	
	@MockBean
	private PessoaRepository mockPessoaRepository;
	
	@Autowired
	private LancamentoService mockService;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	private Lancamento lancamentoMock = criarMockLancamento();
	private Pessoa pessoaMock = criarPessoaMock();
	
	@Test
	public void whenPessoaExistsAndIsActivoShouldSaveLancamentoWithSuccessTest() {
		when(mockPessoaRepository.findById(anyLong())).thenReturn(Optional.of(pessoaMock));
		when(mockLancamentoRepository.save(any())).thenReturn(lancamentoMock);
		
		Lancamento lancamentoTest = mockService.salvar(lancamentoMock);
		
		assertEquals("descricaoTest", lancamentoTest.getDescricao());
		assertTrue(lancamentoTest.getPessoa().getAtivo());
	}
	
	@Test
	public void whenPessoaDoentExistShouldThrowPessoaInexistenteOuInativaExceptionTest() {
		when(mockPessoaRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		lancamentoMock.getPessoa().setCodigo(-1L);
		
		try {
			mockService.salvar(lancamentoMock);
			fail("Should throw PessoaInexistenteOuInativaException");
		} catch (PessoaInexistenteOuInativaException ex) {
			assertSame(PessoaInexistenteOuInativaException.class, ex.getClass());
		}
	}
	
	@Test
	public void whenPessoaIsNotActiveShouldThrowPessoaInexistenteOuInativaExceptionTest() {
		lancamentoMock.getPessoa().setAtivo(Boolean.FALSE);
		
		when(mockPessoaRepository.findById(anyLong())).thenReturn(Optional.of(lancamentoMock.getPessoa()));
		
		try {
			mockService.salvar(lancamentoMock);
			fail("Should throw PessoaInexistenteOuInativaException");
		} catch (PessoaInexistenteOuInativaException ex) {
			assertSame(PessoaInexistenteOuInativaException.class, ex.getClass());
		}
	}

	private Lancamento criarMockLancamento() {
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
		
		Pessoa pessoaMock = criarPessoaMock();
		lancamentoMock.setPessoa(pessoaMock);
		
		return lancamentoMock;
	}
	
	private Pessoa criarPessoaMock() {
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
		
		return pessoaMock;
	}
}
