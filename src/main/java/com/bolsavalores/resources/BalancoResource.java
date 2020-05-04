package com.bolsavalores.resources;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsavalores.entities.Balanco;
import com.bolsavalores.helpers.JsonConverter;
import com.bolsavalores.repositories.BalancoRepository;
import com.bolsavalores.services.BalancoService;

@CrossOrigin
@RestController
@RequestMapping(value="/balanco")
public class BalancoResource {

	@Autowired
	BalancoRepository balancoRepository;
	
	@Autowired
	BalancoService balancoService;
	
	@Autowired
	JsonConverter jsonConverter;
	
	@GetMapping("/busca")
	public Balanco getBalanco(@RequestParam long id) {
		return balancoRepository.findById(id);
	} 
	
	@GetMapping()
	public List<Balanco> getBalancos(){
		return balancoRepository.findAll();
	}

	@GetMapping("/buscaPorAcaoId")
	public List<Balanco> buscaBalancosByAcaoId(@RequestParam long acaoId){
		return balancoRepository.findByAcaoId(acaoId);
	}
	
	@GetMapping("/buscaBalancosRecalculados")
	public ResponseEntity<String> getBalancosRecalculados(@RequestParam long acaoId){
		try {
			List<Balanco> balancosRecalculados = balancoService.getBalancosRecalculadosByAcaoId(acaoId);
			return ResponseEntity.ok(jsonConverter.toJson(balancosRecalculados));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível recalcular os Balancos. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping()
	public Balanco salvaBalanco(@RequestBody Balanco balanco) {
		try {
			return balancoService.salvaBalanco(balanco);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@DeleteMapping()
	public void deletaBalanco(@RequestParam long id) {
		balancoRepository.deleteById(id);
	}
	
	
	
}
