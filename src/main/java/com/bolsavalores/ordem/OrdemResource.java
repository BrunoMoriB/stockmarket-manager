package com.bolsavalores.ordem;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bolsavalores.acao.Acao;
import com.bolsavalores.acao.AcaoRepository;

@RestController
@RequestMapping(value="/ordem")
public class OrdemResource {

	@Autowired
	OrdemRepository ordemRepository;
	
	@Autowired
	AcaoRepository acaoRepository;
	
	@GetMapping()
	public List<Ordem> listaOrdens(){
		return ordemRepository.findAll();
	}
	
	@GetMapping(value="/busca")
	public Ordem buscaOrdem(@RequestParam long id) {
		return ordemRepository.findById(id);
	} 
	
	@PostMapping()
	public Ordem salvaOrdem(@RequestParam(value="idAcao") long idAcao, @RequestBody Ordem ordem) {
		Acao acao = acaoRepository.findById(idAcao);
		ordem.setAcao(acao);
		return ordemRepository.save(ordem);
	}
	
	@DeleteMapping
	public void deletaOrdem(@RequestParam long id) {
		ordemRepository.deleteById(id);
	}
}
