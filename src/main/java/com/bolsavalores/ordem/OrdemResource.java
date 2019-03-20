package com.bolsavalores.ordem;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/ordem")
public class OrdemResource {

	@Autowired
	OrdemRepository ordemRepository;
	
	@GetMapping(value="/lista")
	public List<Ordem> getListOrdem(){
		return ordemRepository.findAll();
	}
	
	@GetMapping(value="/{id}")
	public Ordem buscaOrdem(@PathVariable(value="id") long id) {
		return ordemRepository.findById(id);
	} 
	
	@PostMapping(value="/save")
	public Ordem salvaOrdem(@RequestBody Ordem ordem) {
		return ordemRepository.save(ordem);
	}
}
