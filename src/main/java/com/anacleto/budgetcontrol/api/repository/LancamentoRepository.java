package com.anacleto.budgetcontrol.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.anacleto.budgetcontrol.api.model.Lancamento;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
