package com.anacleto.budgetcontrol.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LancamentoMock {

	public static Lancamento criaMockLancamento() {
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
		
		lancamentoMock.setPessoa(PessoaMock.criaMockPessoa());
		
		return lancamentoMock;
	}
}
