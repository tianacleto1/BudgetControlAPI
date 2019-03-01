package com.anacleto.budgetcontrol.api.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class PessoaTest {

	@Test
	public void pessoaTest() {
		Pessoa pessoa = new Pessoa();
		pessoa.setCodigo(01L);
		pessoa.setNome("NomeTest");
		pessoa.setAtivo(Boolean.FALSE);
		
		Endereco endereco = new Endereco();
		endereco.setLogradouro("logradouroTest");
		endereco.setNumero("10");
		endereco.setComplemento("complementoTest");
		endereco.setBairro("bairroTest");
		endereco.setCep("cepTest");
		endereco.setCidade("cidadeTest");
		endereco.setEstado("estadoTest");
		
		pessoa.setEndereco(endereco);
		
		assertEquals(new Long(01L), pessoa.getCodigo());
		assertEquals("NomeTest", pessoa.getNome());
		assertFalse(pessoa.getAtivo());
		assertEquals("logradouroTest", pessoa.getEndereco().getLogradouro());
		assertEquals("10", pessoa.getEndereco().getNumero());
		assertEquals("complementoTest", pessoa.getEndereco().getComplemento());
		assertEquals("bairroTest", pessoa.getEndereco().getBairro());
		assertEquals("cepTest", pessoa.getEndereco().getCep());
		assertEquals("cidadeTest", pessoa.getEndereco().getCidade());
		assertEquals("estadoTest", pessoa.getEndereco().getEstado());
		
		assertEquals(32, pessoa.hashCode());
		assertFalse(pessoa.equals(new Pessoa()));
	}
}
