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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsavalores.helpers.JsonConverter;
import com.bolsavalores.models.Empresa;
import com.bolsavalores.models.Setor;
import com.bolsavalores.repositories.SetorRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@CrossOrigin 
@RestController
@RequestMapping(value="/setores")
public class SetorResource {

	@Autowired
	SetorRepository setorRepository;
	
	@Autowired
	JsonConverter jsonConverter;
	
	@GetMapping("/{id}")
	public ResponseEntity<String> getSetor(@PathVariable long id) {
		try {
			Setor setor = setorRepository.findById(id);
			
	//		if(setor == null || setor.isEmpty())
	//			TODO tratar
			
			SetorResponse setorResponse = new SetorResponse(setor.getId(), setor.getNome(), getEmpresasResponse(setor.getEmpresas()));
			return ResponseEntity.ok(jsonConverter.toJson(setorResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível buscar o Setor. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping
	public ResponseEntity<String> getSetores(){
		try {
			List<Setor> setores = setorRepository.findAll();
			Collections.sort(setores);
			
	//		if(setores == null || setores.isEmpty())
	//			TODO tratar
			
			List<SetorResponse> setorResponse = new ArrayList<SetorResponse>();
			setores.stream().forEach(s -> setorResponse.add(new SetorResponse(s.getId(), s.getNome(), getEmpresasResponse(s.getEmpresas()))));
			return ResponseEntity.ok(jsonConverter.toJson(setorResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível buscar os Setores. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping
	public ResponseEntity<String> salvaSetor(@RequestBody Setor setor) {
		try {
			setor = setorRepository.save(setor);
			SetorResponse setorResponse = new SetorResponse(setor.getId(), setor.getNome(), getEmpresasResponse(setor.getEmpresas()));
			return ResponseEntity.ok(jsonConverter.toJson(setorResponse));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível salvar o Setor. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping
	public ResponseEntity<?> deletaSetor(@RequestParam long id) {
		try {
			setorRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível deletar o Setor. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	private Set<EmpresaResponse> getEmpresasResponse(Set<Empresa> empresas){
		Set<EmpresaResponse> empresasResponse = new HashSet<EmpresaResponse>();
		empresas.stream().forEach(e -> {
			Set<AcaoResponse> acoesResponse = new HashSet<AcaoResponse>();
			e.getAcoes().stream().forEach(a -> acoesResponse.add(new AcaoResponse(a.getId(), a.getCodigo())));
			empresasResponse.add(new EmpresaResponse(e.getId(), e.getRazaoSocial(), e.getNomePregao(), e.getCnpj(), e.getQuantidade(), acoesResponse));
		});
		
		return empresasResponse;
	}
	
	@Getter
    @AllArgsConstructor
	static class SetorResponse{
		long id;
		String nome;
		Set<EmpresaResponse> empresas;
	}
	
	@Getter
    @AllArgsConstructor
	static class EmpresaResponse{
		long id;
		String razaoSocial;
		String nomePregao;
		String cnpj;
		long quantidade;
		Set<AcaoResponse> acoes;
	}
	
	@Getter
    @AllArgsConstructor
    static class AcaoResponse {
		long id;
		String codigo;
    }
}
