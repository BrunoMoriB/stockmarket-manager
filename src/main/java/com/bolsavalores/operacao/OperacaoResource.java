package com.bolsavalores.operacao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsavalores.ordem.Ordem;
import com.bolsavalores.ordem.OrdemRepository;

@RestController
@RequestMapping(value="/operacao")
public class OperacaoResource {

	@Autowired
	public OperacaoRepository operacaoRepository;
	
	@Autowired
	public OrdemRepository ordemRepository;
	
	@GetMapping()
	public List<Operacao> listaOperacoes(){
		return operacaoRepository.findAll();
	} 
	
	@GetMapping(value="/busca")
	public Operacao buscaOperacao(@RequestParam long id) {
		return operacaoRepository.findById(id);
	} 
	
	@PostMapping()
	public Operacao salvaOperacao(@RequestParam(value="id_oc")  long idCompra, @RequestParam(value="id_ov") long idVenda, @RequestBody Operacao operacao) {
		Ordem ordemCompra = ordemRepository.findById(idCompra);
		Ordem ordemvenda  = ordemRepository.findById(idVenda);
		operacao.setOrdemCompra(ordemCompra);
		operacao.setOrdemVenda(ordemvenda);
		return operacaoRepository.save(operacao);
	}
	
	@DeleteMapping()
	public void deletaOperacao(@RequestParam long id) {
		operacaoRepository.deleteById(id);
	}
}
