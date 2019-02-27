package com.anacleto.budgetcontrol.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.anacleto.budgetcontrol.api.event.RecursoCriadoEvent;
import com.anacleto.budgetcontrol.api.model.Pessoa;
import com.anacleto.budgetcontrol.api.repository.PessoaRepository;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

	@Autowired
	private PessoaRepository pessoaRespository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Pessoa> listarPessoas() {
		return pessoaRespository.findAll();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Pessoa> criarPessoa(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pessoaRespository.save(pessoa);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));

		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoa> getPessoaById(@PathVariable Long codigo) {
		Optional<Pessoa> pessoaOp = pessoaRespository.findById(codigo);
		
		return pessoaOp.isPresent() ? ResponseEntity.ok().body(pessoaOp.get())
									: ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerPessoa(@PathVariable Long codigo) {
		pessoaRespository.deleteById(codigo);
	}
}
