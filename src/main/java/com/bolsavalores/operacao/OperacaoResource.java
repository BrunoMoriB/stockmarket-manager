package com.bolsavalores.operacao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/operacao")
public class OperacaoResource {

	@Autowired
	public OperacaoRepository operacaoRepository;
	
	@GetMapping()
	public List<Operacao> listaOperacoes(){
		return operacaoRepository.findAll();
	} 
	
	@GetMapping(value="/busca")
	public Operacao buscaOperacao(@RequestParam(value="id") long id) {
		return operacaoRepository.findById(id);
	} 
	
	@PostMapping(value="/save")
	public Operacao salvaOperacao(@RequestBody Operacao operacao) {
		return operacaoRepository.save(operacao);
	}
}
