package com.bolsavalores.balanco;

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
import com.bolsavalores.acao.AcaoRepository;

@CrossOrigin
@RestController
@RequestMapping(value="/balanco")
public class BalancoResource {

	@Autowired
	BalancoRepository balancoRepository;
	
	@Autowired
	AcaoRepository acaoRepository;
	
	@GetMapping()
	public List<Balanco> listaBalancos(){
		return balancoRepository.findAll();
	}
	
	@GetMapping("/busca")
	public Balanco buscaBalanco(@RequestParam long id) {
		return balancoRepository.findById(id);
	} 
	
	@GetMapping("/buscaPorAcaoId")
	public List<Balanco> buscaBalancosByAcaoId(@RequestParam long acaoId){
		return balancoRepository.findByAcaoId(acaoId);
	}
	
	@PostMapping()
	public Balanco salvaBalanco(@RequestBody Balanco balanco) {
		return balancoRepository.save(balanco);
	}
	
	@DeleteMapping
	public void deletaBalanco(@RequestParam long id) {
		balancoRepository.deleteById(id);
	}
}
