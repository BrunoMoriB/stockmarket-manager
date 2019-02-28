package com.bolsavalores.operacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/operacao")
public class OperacaoResource {

	@Autowired
	public OperacaoRepository operacaoRepository;
	
	@PostMapping(value="/operacao")
	public Operacao salvaOperacao(@RequestBody Operacao operacao) {
		return operacaoRepository.save(operacao);
	}
	
	@GetMapping(value="/listaOperacoes")
	public List<Operacao> listaOperacoes(){
		return operacaoRepository.findAll();
	}
}
