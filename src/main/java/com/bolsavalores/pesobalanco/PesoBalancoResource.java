package com.bolsavalores.pesobalanco;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/pesobalanco")
public class PesoBalancoResource {

	@Autowired
	PesoBalancoRepository pesoBalancoRepository;
	
	@GetMapping(value="/listPesoBalancos")
	public List<PesoBalanco> getListPesoBalanco(){
		return pesoBalancoRepository.findAll();
	}
	
	@PostMapping(value="/save")
	public PesoBalanco savePesoBalanco(@RequestBody PesoBalanco pesoBalanco) {
		return pesoBalancoRepository.save(pesoBalanco);
	}
}
