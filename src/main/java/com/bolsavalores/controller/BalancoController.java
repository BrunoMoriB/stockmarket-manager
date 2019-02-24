package com.bolsavalores.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bolsavalores.models.Balanco;
import com.bolsavalores.repository.BalancoRepository;

@RestController
@RequestMapping(value="/api")
public class BalancoController {

	@Autowired
	BalancoRepository balancoRepository;
	
	@GetMapping(value="/listaBalancos")
	public List<Balanco> listabalancos(){
		return balancoRepository.findAll();
	}
}
