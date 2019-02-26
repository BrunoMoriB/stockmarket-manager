package com.bolsavalores.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsavalores.models.Operacao;
import com.bolsavalores.repository.OperacaoRepository;

@RestController
@RequestMapping(value="/operacao")
public class OperacaoController {

	@Autowired
	public OperacaoRepository operacaoRepository;
	
	@GetMapping(value="/listaOperacoes")
	public List<Operacao> listaOperacoes(){
		return operacaoRepository.findAll();
	}
}
