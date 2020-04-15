package com.bolsavalores.resources;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsavalores.entities.PesoBalanco;
import com.bolsavalores.entities.PesoBalancoIdentity;
import com.bolsavalores.repositories.PesoBalancoRepository;

@RestController
@RequestMapping(value="/pesobalanco")
public class PesoBalancoResource {

	@Autowired
	PesoBalancoRepository pesoBalancoRepository;
	
	@GetMapping()
	public List<PesoBalanco> getListPesoBalanco(){
		return pesoBalancoRepository.findAll();
	}
	
	@GetMapping("/busca")
	public PesoBalanco buscaPesoBalancoByIndicadorEpeso(@RequestParam String indicador, @RequestParam int peso) {
		PesoBalancoIdentity pesoBalancoIdentity = new PesoBalancoIdentity(indicador, peso);
		
		return pesoBalancoRepository.findByPesoBalancoIdentity(pesoBalancoIdentity); 
	}
	
	@PostMapping()
	public PesoBalanco savePesoBalanco(@RequestParam String indicador, @RequestParam int peso ,@RequestBody PesoBalanco pesoBalanco) {
		PesoBalancoIdentity pesoBalancoIdentity = new PesoBalancoIdentity(indicador, peso);
		pesoBalanco.setPesoBalancoIdentity(pesoBalancoIdentity);
		
		return pesoBalancoRepository.save(pesoBalanco);
	}
	
	@DeleteMapping()
	public void deletaPesoBalanco(@RequestParam String indicador, @RequestParam int peso) {
		PesoBalancoIdentity pesoBalancoIdentity = new PesoBalancoIdentity(indicador, peso);
		PesoBalanco pesoBalanco 				= pesoBalancoRepository.findByPesoBalancoIdentity(pesoBalancoIdentity); 
		
		pesoBalancoRepository.delete(pesoBalanco);
	}
}
