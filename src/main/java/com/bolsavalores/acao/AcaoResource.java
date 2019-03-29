package com.bolsavalores.acao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/acao")
public class AcaoResource {

	@Autowired
	AcaoRepository acaoRepository;
	
	@GetMapping()
	public List<Acao> listaAcoes(){
		return acaoRepository.findAll();
	}
	
	@GetMapping("/busca")
	public Acao buscaAcao(@RequestParam long id) {
		return acaoRepository.findById(id);
	}
	
	@PostMapping()
	public Acao salvaAcao(@RequestBody Acao acao) {
		 return acaoRepository.save(acao);
	}
	
	@DeleteMapping()
	public void deletaAcao(@RequestParam long id) {
		acaoRepository.deleteById(id);
	}
	
}
