package com.bolsavalores.balanco;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/balanco")
public class BalancoResource {

	@Autowired
	BalancoRepository balancoRepository;
	
	@GetMapping(value="/listaBalancos")
	public List<Balanco> listaBalancos(){
		return balancoRepository.findAll();
	}
}
