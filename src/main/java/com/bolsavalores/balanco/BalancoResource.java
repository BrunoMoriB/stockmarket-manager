package com.bolsavalores.balanco;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/balanco")
public class BalancoResource {

	@Autowired
	BalancoRepository balancoRepository;
	
	@GetMapping(value="/lista")
	public List<Balanco> listaBalancos(){
		return balancoRepository.findAll();
	}
	
	@GetMapping(value="/{id}")
	public Balanco buscaBalanco(@PathVariable(value="id") long id) {
		return balancoRepository.findById(id);
	} 
	
	@PostMapping(value="/save")
	public Balanco salvaBalanco(@RequestBody Balanco balanco) {
		return balancoRepository.save(balanco);
	}
}
