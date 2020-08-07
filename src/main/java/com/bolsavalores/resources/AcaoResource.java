package com.bolsavalores.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.bolsavalores.helpers.JsonConverter;
import com.bolsavalores.models.Acao;
import com.bolsavalores.models.Empresa;
import com.bolsavalores.repositories.AcaoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@CrossOrigin
@RestController
@RequestMapping(value="/acao")
public class AcaoResource {

	@Autowired
	AcaoRepository acaoRepository;
	
	@Autowired
	JsonConverter jsonConverter;
	
	@GetMapping("/busca")
	public ResponseEntity<String> getAcao(@RequestParam long id) {
		try {
			Acao acao = acaoRepository.findById(id);
			AcaoResponse acaoResponse = new AcaoResponse(acao.getId(), acao.getCodigo(), getEmpresaResponse(acao.getEmpresa()));
			return ResponseEntity.ok(jsonConverter.toJson(acaoResponse));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível buscar a Ação. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping()
	public ResponseEntity<String> getAcoes(){
		try{
			new SetorResponse(3L, "adeasw");
			List<Acao> acoes = acaoRepository.findAll();
			Collections.sort(acoes);
			List<AcaoResponse> acoesResponse = new ArrayList<AcaoResponse>();
			acoes.forEach(a -> acoesResponse.add(new AcaoResponse(a.getId(), a.getCodigo(), getEmpresaResponse(a.getEmpresa()))));
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
		 AcaoResponse acaoResponse = new AcaoResponse(acao.getId(), acao.getCodigo(), getEmpresaResponse(acao.getEmpresa()));
			return ResponseEntity.ok(jsonConverter.toJson(acaoResponse));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível salvar a Ação. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping()
	public void deletaAcao(@RequestParam long id) {
		acaoRepository.deleteById(id);
	}
	
	@GetMapping("/buscaPorNomeOuCodigo")
	public ResponseEntity<String> buscaAcaoByNomeOuCodigo(@RequestParam String nome, String codigo){
		try {
			Acao acao = acaoRepository.findByNomeOrCodigo(nome.toUpperCase(), codigo.toUpperCase());
			AcaoResponse acaoResponse = new AcaoResponse(acao.getId(), acao.getCodigo(), getEmpresaResponse(acao.getEmpresa()));
			return ResponseEntity.ok(jsonConverter.toJson(acaoResponse));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível buscar a Ação. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	private EmpresaResponse getEmpresaResponse(Empresa empresa) {
		Set<SetorResponse> setores = new HashSet<SetorResponse>();
		empresa.getSetores().stream().forEach(s -> setores.add(new SetorResponse(s.getId(), s.getNome())));
		return new EmpresaResponse(empresa.getId(), empresa.getRazaoSocial(), empresa.getNomePregao(), empresa.getCnpj(), empresa.getQuantidade(), setores);
	}
	
	@Getter
    @AllArgsConstructor
    static class AcaoResponse {
		long id;
		String codigo;
		EmpresaResponse empresa;
    }
	
	@Getter
    @AllArgsConstructor
	static class EmpresaResponse{
		long id;
		String razaoSocial;
		String nomePregao;
		String cnpj;
		long quantidade;
		Set<SetorResponse> setores;
	}
	
	@Getter
    @AllArgsConstructor
	static class SetorResponse{
		long id;
		String nome;
	}
}
