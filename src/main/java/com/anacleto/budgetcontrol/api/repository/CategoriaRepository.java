package com.anacleto.budgetcontrol.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anacleto.budgetcontrol.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
