package com.bolsavalores.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bolsavalores.models.Acao;
import com.bolsavalores.repository.AcaoRepository;

@RestController
@RequestMapping(value="/api")
public class AcaoController {

	@Autowired
	AcaoRepository acaoRepository;
	
	@GetMapping(value="/listaAcoes")
	public List<Acao> listaAcoes(){
		return acaoRepository.findAll();
	}
	
	@GetMapping(value="/acao/{id}")
	public Acao buscaAcao(@PathVariable(value="id") long id) {
		return acaoRepository.findById(id);
	}

	@PostMapping(value="/acao")
	public Acao salvaAcao(@RequestBody Acao acao) {
		 return acaoRepository.save(acao);
	}
	
}
