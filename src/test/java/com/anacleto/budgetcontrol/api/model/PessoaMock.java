package com.anacleto.budgetcontrol.api.model;

public class PessoaMock {

	public static Pessoa criaMockPessoa() {
		Pessoa pessoaMock = new Pessoa();
		pessoaMock.setCodigo(1L);
		pessoaMock.setNome("NomeTest");
		pessoaMock.setAtivo(Boolean.TRUE);
		
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
