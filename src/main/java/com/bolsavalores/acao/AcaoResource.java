package com.bolsavalores.acao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/acao")
public class AcaoResource {

	@Autowired
	AcaoRepository acaoRepository;
	
	@GetMapping(value="/lista")
	public List<Acao> listaAcoes(){
		return acaoRepository.findAll();
	}
	
	@GetMapping(value="/{id}")
	public Acao buscaAcao(@PathVariable(value="id") long id) {
		return acaoRepository.findById(id);
	}

	@PostMapping(value="/save")
	public Acao salvaAcao(@RequestBody Acao acao) {
		 return acaoRepository.save(acao);
	}
	
}
