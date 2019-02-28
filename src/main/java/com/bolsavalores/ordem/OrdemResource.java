package com.bolsavalores.ordem;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/ordem")
public class OrdemResource {

	@Autowired
	OrdemRepository ordemRepository;
	
	@GetMapping(value="/listaOrdens")
	public List<Ordem> getListOrdem(){
		return ordemRepository.findAll();
	}
	
	@PostMapping(value="/save")
	public Ordem salvaOrdem(@RequestBody Ordem ordem) {
		return ordemRepository.save(ordem);
	}
}
