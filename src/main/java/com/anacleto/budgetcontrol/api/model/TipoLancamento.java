package com.anacleto.budgetcontrol.api.model;

public enum TipoLancamento {

	RECEITA("Receita"),
	DESPESA("Despesa");
	
	private final String nome;
	
	TipoLancamento(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
}
