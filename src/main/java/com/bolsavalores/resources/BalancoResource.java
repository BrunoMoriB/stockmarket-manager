package com.bolsavalores.resources;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.bolsavalores.helpers.CalculadoraFundamentalista;
import com.bolsavalores.helpers.JsonConverter;
import com.bolsavalores.models.Acao;
import com.bolsavalores.models.Balanco;
import com.bolsavalores.models.exceptions.StockmarketException;
import com.bolsavalores.repositories.AcaoRepository;
import com.bolsavalores.repositories.BalancoRepository;
import com.bolsavalores.services.BalancoService;
import com.fasterxml.jackson.core.JsonProcessingException;

@CrossOrigin
@RestController
@RequestMapping(value="/balanco")
public class BalancoResource {

    private static final Logger LOG = LoggerFactory.getLogger(BalancoResource.class);
	
	@Autowired
	BalancoRepository balancoRepository;
	
	@Autowired
	AcaoRepository acaoRepository;
	
	@Autowired
	BalancoService balancoService;
	
	@Autowired
	JsonConverter jsonConverter;
	
	@Autowired
	CalculadoraFundamentalista calculadoraFundamentalista;
	
	@GetMapping("/busca")
	public Balanco getBalanco(@RequestParam long id) {
		return balancoRepository.findById(id);
	} 
	
	@GetMapping()
	public List<Balanco> getBalancos(){
		return balancoRepository.findAll();
	}

	@GetMapping("/buscaPorAcaoId")
	public ResponseEntity<String> buscaBalancosByAcaoId(@RequestParam long acaoId){
		try {
			List<Balanco> balancos = balancoRepository.findByAcaoId(acaoId);
			Collections.sort(balancos);
			return ResponseEntity.ok(jsonConverter.toJson(balancos));
		} catch (JsonProcessingException e) {
			LOG.error("Não foi possível buscar os Balancos. ", e);
			return new ResponseEntity<String>("Não foi possível buscar os Balancos. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/buscaBalancosRecalculados")
	public ResponseEntity<String> getBalancosRecalculados(@RequestParam long acaoId){
		try {
			List<Balanco> balancosRecalculados = balancoService.getBalancosRecalculadosByAcaoId(acaoId);
			return ResponseEntity.ok(jsonConverter.toJson(balancosRecalculados));
		} catch (Exception e) {
			LOG.error("Não foi possível recalcular os Balancos. ", e);
			return new ResponseEntity<String>("Não foi possível recalcular os Balancos. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/buscaTodosBalancosDailyUpdatedRecalculados")
	public ResponseEntity<String> getTodosBalancosDailyUpdatedRecalculados(){
		LOG.info("Iniciando processo de atualização dos Balanços das empresas.");
		
		try {
			List<Acao> acoes  = acaoRepository.findAll();
			acoes.forEach(a -> {
					try {
						balancoService.getBalancosRecalculadosByAcaoId(a.getId());
					} catch (ParseException | StockmarketException e) {
						LOG.error("Não foi possível recalcular os Balancos para Ação(id " + a.getId() + "). " + e.getMessage());
					}
				});
			
			List<Balanco> balancosRecalculados = balancoRepository.findBalancosDailyUpdated();
			
			LOG.info("Processo de atualização dos Balanços das empresas foi finalizado!");
			
			return ResponseEntity.ok(jsonConverter.toJson(balancosRecalculados));
		} catch (Exception e) {
			LOG.error("Não foi possível recalcular os Balancos. ", e);
			return new ResponseEntity<String>("Não foi possível recalcular os Balancos. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/buscaBalancosDailyUpdatedRequisitosMinimos")
	public ResponseEntity<String> getBalancosDailyUpdatedRequisitosMinimos(){
		try {
			List<Balanco> balancosDailyUpdated = balancoRepository.findBalancosDailyUpdated();
			
			if(balancosDailyUpdated == null || balancosDailyUpdated.isEmpty())
				throw new StockmarketException("Nenhum balanço daily updated encontrado. ");
			
			balancosDailyUpdated.removeIf( b -> !calculadoraFundamentalista.isDadosBalancoValidos(b.getMultiplosFundamentalistas(), b.getDesempenhoFinanceiro()) ||
					!calculadoraFundamentalista.validaRequisitosMinimos(b.getMultiplosFundamentalistas(), b.getDesempenhoFinanceiro()));

			Collections.sort(balancosDailyUpdated, Balanco.Comparators.NOTA);
			
			return ResponseEntity.ok(jsonConverter.toJson(balancosDailyUpdated));
		} catch (Exception e) {
			LOG.error("Não foi possível encontrar os Balanços daily updated. ", e);
			return new ResponseEntity<String>("Não foi possível encontrar os Balanços daily updated. ", HttpStatus.BAD_REQUEST);
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
