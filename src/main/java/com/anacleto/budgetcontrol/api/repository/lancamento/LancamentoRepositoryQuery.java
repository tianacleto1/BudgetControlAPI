package com.anacleto.budgetcontrol.api.repository.lancamento;

import java.util.List;

import com.anacleto.budgetcontrol.api.model.Lancamento;
import com.anacleto.budgetcontrol.api.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {

	List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);
}
