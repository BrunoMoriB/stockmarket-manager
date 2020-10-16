package com.bolsavalores.resources;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsavalores.helpers.JsonConverter;
import com.bolsavalores.models.Acao;
import com.bolsavalores.models.Cotacao;
import com.bolsavalores.models.Empresa;
import com.bolsavalores.models.Provento;
import com.bolsavalores.repositories.AcaoRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@CrossOrigin
@RestController
@RequestMapping(value="/acoes")
public class AcaoResource {

	@Autowired
	AcaoRepository acaoRepository;
	
	@Autowired
	JsonConverter jsonConverter;
	
    private static final Logger LOG = LoggerFactory.getLogger(AcaoResource.class);
	
	@GetMapping("/{id}")
	public ResponseEntity<String> getAcao(@PathVariable long id) {
		try {
			Acao acao = acaoRepository.findById(id);
			
//			if(acao == null)
//				TODO tratar
			
			AcaoResponse acaoResponse = new AcaoResponse(acao.getId(), acao.getCodigo(), getEmpresaResponse(acao.getEmpresa()), getCotacoesResponse(acao.getCotacoes()), getProventosResponse(acao.getProventos()));
			return ResponseEntity.ok(jsonConverter.toJson(acaoResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível buscar a Ação. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping()
	public ResponseEntity<String> getAcoes(){
		try{
			List<Acao> acoes = acaoRepository.findAll();
			
//			if(acoes == null || acoes.isEmpty())
//				TODO tratar
			
			Collections.sort(acoes);
			List<AcaoResponse> acoesResponse = new ArrayList<AcaoResponse>();
			acoes.stream().filter(a -> validaAcao(a)).forEach(a -> acoesResponse.add(new AcaoResponse(a.getId(), a.getCodigo(), getEmpresaResponse(a.getEmpresa()), getCotacoesResponse(a.getCotacoes()), getProventosResponse(a.getProventos()))));
			return ResponseEntity.ok(jsonConverter.toJson(acoesResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível buscar as Ações. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping()
	public ResponseEntity<String> salvaAcao(@RequestBody Acao acao) {
		try {
		 acao = acaoRepository.save(acao);
		 AcaoResponse acaoResponse = new AcaoResponse(acao.getId(), acao.getCodigo(), getEmpresaResponse(acao.getEmpresa()), getCotacoesResponse(acao.getCotacoes()), getProventosResponse(acao.getProventos()));
			return ResponseEntity.ok(jsonConverter.toJson(acaoResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível salvar a Ação. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping()
	public ResponseEntity<?> deletaAcao(@RequestParam long id) {
		try {
			acaoRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível deletar a Ação. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/buscaPorNomeOuCodigo")
	public ResponseEntity<String> getAcaoByNomeOuCodigo(@RequestParam String nome, String codigo){
		try {
			Acao acao = acaoRepository.findByNomeOrCodigo(nome, codigo);
			
//			if(acao == null)
//			TODO tratar
			
			AcaoResponse acaoResponse = new AcaoResponse(acao.getId(), acao.getCodigo(), getEmpresaResponse(acao.getEmpresa()), getCotacoesResponse(acao.getCotacoes()), getProventosResponse(acao.getProventos()));
			return ResponseEntity.ok(jsonConverter.toJson(acaoResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível buscar a Ação. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	private EmpresaResponse getEmpresaResponse(Empresa empresa) {
		if(empresa == null)
			return null;
		
		Set<SetorResponse> setores = new HashSet<SetorResponse>();
		empresa.getSetores().stream().forEach(s -> setores.add(new SetorResponse(s.getId(), s.getNome())));
		EmpresaResponse teste = null;
		
		try {
			teste = new EmpresaResponse(empresa.getId(), empresa.getRazaoSocial(), empresa.getNomePregao(), empresa.getCnpj(), empresa.getQuantidadePapeis(), setores);
		}catch(Exception e) {
			LOG.error("ERROR >> " + empresa.getNomePregao() + " - " + empresa.getId() + e.getMessage());
		}
		
		return teste;
	}
	
	private boolean validaAcao(Acao acao) {
		return acao != null && 
			   acao.getCodigo() != null && !acao.getCodigo().isEmpty() &&
			   validaEmpresa(acao.getEmpresa());
	}
	
	private boolean validaEmpresa(Empresa empresa) {
		return empresa != null && 
			   empresa.getCnpj() != null && !empresa.getCnpj().isEmpty() &&
			   empresa.getNomePregao() != null && !empresa.getNomePregao().isEmpty() &&
			   empresa.getQuantidadePapeis() != null && 
			   empresa.getRazaoSocial() != null && !empresa.getRazaoSocial().isEmpty() &&
			   empresa.getSetores() != null && !empresa.getSetores().isEmpty();
	}
	
	private List<CotacaoResponse> getCotacoesResponse(Set<Cotacao> cotacoes) {
		List<CotacaoResponse> cotacoesResponse = new ArrayList<CotacaoResponse>();
		cotacoes.forEach(c -> cotacoesResponse.add(new CotacaoResponse(c.getId(), c.getData(), c.getValor())));
		return cotacoesResponse;
	}
	
	private List<ProventoResponse> getProventosResponse(Set<Provento> proventos){
		List<ProventoResponse> proventosResponse = new ArrayList<ProventoResponse>();
		proventos.forEach(p -> proventosResponse.add(new ProventoResponse(p.getId(), p.getTipo(), p.getValor(), p.getDataEx(), p.getDataPagamento())));
		return proventosResponse;
	}
	
	@Getter
    @AllArgsConstructor
    static class AcaoResponse {
		long id;
		String codigo;
		EmpresaResponse empresa;
		List<CotacaoResponse> cotacoes;
		List<ProventoResponse> proventos;
    }
	
	@Getter
	@AllArgsConstructor
	static class CotacaoResponse{
		long id;
		LocalDate data;
		double valor;
	}
	
	@Getter
	@AllArgsConstructor
	static class ProventoResponse{
		long id;
		String tipo;
		double valor;
		LocalDate dataEx;
		LocalDate dataPagamento;
	}
	
	@Getter
    @AllArgsConstructor
	static class EmpresaResponse{
		long id;
		String razaoSocial;
		String nomePregao;
		String cnpj;
		long quantidadePapeis;
		Set<SetorResponse> setores;
	}
	
	@Getter
    @AllArgsConstructor
	static class SetorResponse{
		long id;
		String nome;
	}
}