package com.bolsavalores.acao;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value="/acao")
public class AcaoResource {

	@Autowired
	AcaoRepository acaoRepository;
	
	@GetMapping()
	public List<Acao> listaAcoes(){
		List<Acao> acoes = acaoRepository.findAll();
		Collections.sort(acoes);
		acoes.forEach(System.out::println);
		return acoes;
	}
	
	@GetMapping("/busca")
	public Acao buscaAcao(@RequestParam long id) {
		return acaoRepository.findById(id);
	}
	
	@GetMapping("/buscaPorNomeOuCodigo")
	public Acao buscaAcaoByNomeOuCodigo(@RequestParam String nome, String codigo){
		return acaoRepository.findByNomeOrCodigo(nome.toUpperCase(), codigo.toUpperCase());
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
