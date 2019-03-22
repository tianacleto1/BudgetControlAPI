package com.anacleto.budgetcontrol.api.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.anacleto.budgetcontrol.api.model.Lancamento;
import com.anacleto.budgetcontrol.api.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {

	Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
}
