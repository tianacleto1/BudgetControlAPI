package com.anacleto.budgetcontrol.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
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

import com.anacleto.budgetcontrol.api.model.Pessoa;
import com.anacleto.budgetcontrol.api.model.PessoaMock;
import com.anacleto.budgetcontrol.api.repository.PessoaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PessoaServiceTest {
	
	@MockBean
	private PessoaRepository mockRepository;
	
	@Autowired
	private PessoaService mockService;
	
	private Pessoa pessoaMock = PessoaMock.criaMockPessoa();
	
	@Test
	public void whenUpdatePessoaIsOkShouldReturnEntityPessoaUpdatedTest() {
		when(mockRepository.findById(anyLong())).thenReturn(Optional.of(pessoaMock));
		when(mockRepository.save(pessoaMock)).thenReturn(pessoaMock);
		
		mockService.atualizar(pessoaMock.getCodigo(), pessoaMock);
		
		assertEquals(new Long(1), pessoaMock.getCodigo());
		assertEquals("NomeTest", pessoaMock.getNome());
		assertTrue(pessoaMock.getAtivo());
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
	
	@Test
	public void whenPessoaExistAndAtivoIsFalseShouldSetToTrueTest() {
		when(mockRepository.findById(anyLong())).thenReturn(Optional.of(pessoaMock));
		when(mockRepository.save(any())).thenReturn(pessoaMock);
		
		mockService.atualizarPropriedadeAtivo(pessoaMock.getCodigo(), true);
		
		assertTrue(pessoaMock.getAtivo());
	}
}
