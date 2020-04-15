package com.bolsavalores.resources;

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

import com.bolsavalores.entities.Balanco;
import com.bolsavalores.repositories.BalancoRepository;

@CrossOrigin
@RestController
@RequestMapping(value="/balanco")
public class BalancoResource {

	@Autowired
	BalancoRepository balancoRepository;
	
	@GetMapping("/busca")
	public Balanco getBalanco(@RequestParam long id) {
		return balancoRepository.findById(id);
	} 
	
	@GetMapping()
	public List<Balanco> getBalancos(){
		return balancoRepository.findAll();
	}
	
	@PostMapping()
	public Balanco salvaBalanco(@RequestBody Balanco balanco) {
		return balancoRepository.save(balanco);
	}
	
	@DeleteMapping()
	public void deletaBalanco(@RequestParam long id) {
		balancoRepository.deleteById(id);
	}
	
	@GetMapping("/buscaPorAcaoId")
	public List<Balanco> buscaBalancosByAcaoId(@RequestParam long acaoId){
		return balancoRepository.findByAcaoId(acaoId);
	}
}
