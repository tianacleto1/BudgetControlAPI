package com.anacleto.budgetcontrol.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.anacleto.budgetcontrol.api.model.Pessoa;
import com.anacleto.budgetcontrol.api.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;

	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaSalva = buscarPessoaByCodigo(codigo);
		
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
		
		return pessoaRepository.save(pessoaSalva);
	}

	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaSalva = buscarPessoaByCodigo(codigo);
		
		pessoaSalva.setAtivo(ativo);
		
		pessoaRepository.save(pessoaSalva);
	}
	
	public Pessoa buscarPessoaByCodigo(Long codigo) {
		Optional<Pessoa> pessoaOp = pessoaRepository.findById(codigo);
		
		if (!pessoaOp.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return pessoaOp.get();
	}
}
