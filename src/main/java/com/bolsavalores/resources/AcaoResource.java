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
import com.bolsavalores.repositories.AcaoRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@RestController
@RequestMapping(value=Resources.ACOES)
public class AcaoResource {

	@Autowired
	AcaoRepository acaoRepository;
	
	@Autowired
	JsonConverter jsonConverter;
	
	@GetMapping("/{id}")
	public ResponseEntity<String> getAcao(@PathVariable long id) {
		try {
			Acao acao = acaoRepository.findById(id);
			
//			if(acao == null)
//				TODO tratar
			
			AcaoResponse acaoResponse = new AcaoResponse(acao.getId(), acao.getCodigo(), getEmpresaResponse(acao.getEmpresa()));
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
			
			Collections.sort(acoes); //TODO: Ordernar no front-end
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
			
			AcaoResponse acaoResponse = new AcaoResponse(acao.getId(), acao.getCodigo(), getEmpresaResponse(acao.getEmpresa()));
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
		return new EmpresaResponse(empresa.getId(), empresa.getRazaoSocial(), empresa.getNomePregao(), empresa.getCnpj(), empresa.getQuantidadePapeis(), setores);
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