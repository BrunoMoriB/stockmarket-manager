package com.bolsavalores.resources;

import java.util.Collections;
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

import com.bolsavalores.entities.Setor;
import com.bolsavalores.repositories.SetorRepository;

@CrossOrigin 
@RestController
@RequestMapping(value="/setor")
public class SetorResource {

	@Autowired
	SetorRepository setorRepository;
	
	@GetMapping("/busca")
	public Setor getSetor(@RequestParam long id) {
		return setorRepository.findById(id);
	}
	
	@GetMapping
	public List<Setor> getSetores(){
		List<Setor> setores = setorRepository.findAll();
		Collections.sort(setores);
		return setores;
	}
	
	@PostMapping
	public Setor salvaSetor(@RequestBody Setor setor) {
		return setorRepository.save(setor);
	}
	
	@DeleteMapping
	public void deletaSetor(@RequestParam long id) {
		setorRepository.deleteById(id);
	}
	
}
