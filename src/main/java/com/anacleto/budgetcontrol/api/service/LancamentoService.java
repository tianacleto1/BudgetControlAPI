package com.anacleto.budgetcontrol.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anacleto.budgetcontrol.api.model.Lancamento;
import com.anacleto.budgetcontrol.api.model.Pessoa;
import com.anacleto.budgetcontrol.api.repository.LancamentoRepository;
import com.anacleto.budgetcontrol.api.repository.PessoaRepository;
import com.anacleto.budgetcontrol.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	@Autowired
	private PessoaRepository pessoaRespository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	public Lancamento salvar(Lancamento lancamento) {
		Optional<Pessoa> pessoaOp = pessoaRespository.findById(lancamento.getPessoa().getCodigo());
		 
		if (!pessoaOp.isPresent() || !pessoaOp.get().isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		} 
			
		return lancamentoRepository.save(lancamento);
	}


}
