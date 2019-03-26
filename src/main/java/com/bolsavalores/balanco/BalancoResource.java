package com.bolsavalores.balanco;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bolsavalores.acao.Acao;
import com.bolsavalores.acao.AcaoRepository;

@RestController
@RequestMapping(value="/balanco")
public class BalancoResource {

	@Autowired
	BalancoRepository balancoRepository;
	
	@Autowired
	AcaoRepository acaoRepository;
	
	@GetMapping(value="/lista")
	public List<Balanco> listaBalancos(){
		return balancoRepository.findAll();
	}
	
	@GetMapping(value="/{id}")
	public Balanco buscaBalanco(@PathVariable(value="id") long id) {
		return balancoRepository.findById(id);
	} 
	
	@PostMapping(value="/{id}/save")
	public Balanco salvaBalanco(@PathVariable(value="id") long acaoId, @RequestBody Balanco balanco) {
		Acao acao = acaoRepository.findById(acaoId);
		balanco.setAcao(acao);
		return balancoRepository.save(balanco);
	}
}
