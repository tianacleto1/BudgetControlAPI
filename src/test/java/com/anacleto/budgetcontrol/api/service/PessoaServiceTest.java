package com.anacleto.budgetcontrol.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import com.anacleto.budgetcontrol.api.model.Endereco;
import com.anacleto.budgetcontrol.api.model.Pessoa;
import com.anacleto.budgetcontrol.api.repository.PessoaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PessoaServiceTest {
	
	@MockBean
	private PessoaRepository mockRepository;
	
	@Autowired
	private PessoaService mockService;
	
	@Test
	public void whenUpdatePessoaIsOkShouldReturnEntityPessoaUpdatedTest() {
		Pessoa pessoaMock = criaMockPessoa();
		
		when(mockRepository.findById(anyLong())).thenReturn(Optional.of(pessoaMock));
		when(mockRepository.save(pessoaMock)).thenReturn(pessoaMock);
		
		mockService.atualizar(pessoaMock.getCodigo(), pessoaMock);
		
		assertEquals(new Long(1), pessoaMock.getCodigo());
		assertEquals("NomeTest", pessoaMock.getNome());
		assertFalse(pessoaMock.getAtivo());
	}
	
	@Test
	public void whenUpdatePessoaIsNoOkShouldThrowEmptyResultDataAccessExceptionTest() {
		when(mockRepository.findById(anyLong())).thenReturn(Optional.empty());
		
		try {
			mockService.atualizar(anyLong(), null);
			fail("Should throw EmptyResultDataAccessException");
		} catch (EmptyResultDataAccessException e) {
			assertEquals("Incorrect result size: expected 1, actual 0", e.getMessage());
		}
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
