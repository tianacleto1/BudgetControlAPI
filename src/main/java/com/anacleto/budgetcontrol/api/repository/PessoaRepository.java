package com.anacleto.budgetcontrol.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anacleto.budgetcontrol.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}
