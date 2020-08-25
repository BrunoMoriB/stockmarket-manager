package com.bolsavalores.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.bolsavalores.models.Empresa;
import com.bolsavalores.models.Setor;
import com.bolsavalores.repositories.EmpresaRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RestController
@RequestMapping(value=Resources.EMPRESAS)
public class EmpresaResource {

	@Autowired
	EmpresaRepository empresaRepository;
	
	@Autowired
	JsonConverter jsonConverter;
	
	@GetMapping("/{id}")
	public ResponseEntity<String> getEmpresa(@PathVariable long id) {
		try {
			Empresa empresa = empresaRepository.findById(id);
			
//			if(empresa == null)
//				TODO tratar
			
			EmpresaResponse empresaResponse = new EmpresaResponse(empresa.getId(), 
																  empresa.getRazaoSocial(),
																  empresa.getNomePregao(), 
																  empresa.getCnpj(), 
																  empresa.getQuantidadePapeis(), 
																  getSetoresResponse(empresa.getSetores()), 
																  getAcoesResponse(empresa.getAcoes()));
			return ResponseEntity.ok(jsonConverter.toJson(empresaResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível buscar a Empresa. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping()
	public ResponseEntity<String> getEmpresas(){
		try{
			List<Empresa> empresas = empresaRepository.findAll();
			
//			if(empresa == null || empresas.isEmpty())
//				TODO tratar
			
			Collections.sort(empresas);
			List<EmpresaResponse> empresasResponse = new ArrayList<EmpresaResponse>();
			empresas.forEach(e -> empresasResponse.add(new EmpresaResponse(e.getId(), 
																		  e.getRazaoSocial(), 
																		  e.getNomePregao(), 
																		  e.getCnpj(), 
																		  e.getQuantidadePapeis(), 
																		  getSetoresResponse(e.getSetores()), 
																		  getAcoesResponse(e.getAcoes()))));
			return ResponseEntity.ok(jsonConverter.toJson(empresasResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível buscar as Empresas. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/buscaPorAcaoId")
	public ResponseEntity<String> getEmpresaByAcaoId(@RequestParam long id) {
		try {
			Empresa empresa = empresaRepository.findByAcaoId(id);
			
//			if(empresa == null)
//				TODO tratar
			
			EmpresaResponse empresaResponse = new EmpresaResponse(empresa.getId(), 
																  empresa.getRazaoSocial(),
																  empresa.getNomePregao(), 
																  empresa.getCnpj(), 
																  empresa.getQuantidadePapeis(), 
																  getSetoresResponse(empresa.getSetores()), 
																  getAcoesResponse(empresa.getAcoes()));
			return ResponseEntity.ok(jsonConverter.toJson(empresaResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível buscar a Empresa pela acaoId. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping()
	public ResponseEntity<String> salvaEmpresa(@RequestBody Empresa empresa) {
		try {
			empresa =empresaRepository.save(empresa);
			EmpresaResponse empresaResponse = new EmpresaResponse(empresa.getId(), 
				 											   empresa.getRazaoSocial(), 
				 											   empresa.getNomePregao(), 
				 											   empresa.getCnpj(), 
				 											   empresa.getQuantidadePapeis(), 
				 											   getSetoresResponse(empresa.getSetores()), 
				 											   getAcoesResponse(empresa.getAcoes()));
			return ResponseEntity.ok(jsonConverter.toJson(empresaResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível salvar a Empresa. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping()
	public ResponseEntity<?> deletaEmpresa(@RequestParam long id) {
		try {
			empresaRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível deletar a Empresa. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/buscaPorNome")
	public ResponseEntity<String> getEmpresaByNome(@RequestParam String nome){
		try {
			Empresa empresa = empresaRepository.findByNome(nome);
			
//			if(empresa == null)
//				TODO tratar
			
			EmpresaResponse empresaResponse = new EmpresaResponse(empresa.getId(), 
																  empresa.getRazaoSocial(), 
																  empresa.getNomePregao(), 
																  empresa.getCnpj(), 
																  empresa.getQuantidadePapeis(), 
																  getSetoresResponse(empresa.getSetores()), 
																  getAcoesResponse(empresa.getAcoes()));
			return ResponseEntity.ok(jsonConverter.toJson(empresaResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível buscar a Empresa. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	private Set<SetorResponse> getSetoresResponse(Set<Setor> setores) {
		Set<SetorResponse> setoresResponse = new HashSet<SetorResponse>();
		setores.stream().forEach(s -> setoresResponse.add(new SetorResponse(s.getId(), s.getNome())));
		return setoresResponse;
	}
	
	private Set<AcaoResponse> getAcoesResponse(Set<Acao> acoes){
		Set<AcaoResponse> acoesResponse = new HashSet<AcaoResponse>();
		acoes.stream().forEach(a -> acoesResponse.add(new AcaoResponse(a.getId(), a.getCodigo())));
		return acoesResponse;
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
		Set<AcaoResponse> acoes;
	}
	
	@Getter
    @AllArgsConstructor
    static class AcaoResponse {
		long id;
		String codigo;
    }
	
	@Getter
    @AllArgsConstructor
	static class SetorResponse{
		long id;
		String nome;
	}
}
