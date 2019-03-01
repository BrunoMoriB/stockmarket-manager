package com.bolsavalores.pesobalanco;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/peso_balanco")
public class PesoBalancoResource {

	@Autowired
	PesoBalancoRepository pesoBalancoRepository;
	
	@GetMapping(value="/listPesoBalancos")
	public List<PesoBalanco> getListPesoBalanco(){
		return pesoBalancoRepository.findAll();
	}
}
