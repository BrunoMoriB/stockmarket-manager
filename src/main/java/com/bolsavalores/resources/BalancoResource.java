package com.bolsavalores.resources;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsavalores.entities.Balanco;
import com.bolsavalores.repositories.BalancoRepository;

@CrossOrigin
@RestController
@RequestMapping(value="/balanco")
public class BalancoResource {

	@Autowired
	BalancoRepository balancoRepository;
	
	@GetMapping("/busca")
	public Balanco getBalanco(@RequestParam long id) {
		return balancoRepository.findById(id);
	} 
	
	@GetMapping()
	public List<Balanco> getBalancos(){
		return balancoRepository.findAll();
	}
	
	@PostMapping()
	public Balanco salvaBalanco(@RequestBody Balanco balanco) {
		List<Balanco> balancosAnteriores = balancoRepository.findByAcaoId(balanco.getAcao().getId());
		
		balanco.setEvolucaoLucroLiquidoMeses(getEvolucaoLucroLiquidoTrimestral(balanco, balancosAnteriores));
		balanco.setLucroLiquidoAnual(getLucroLiquidoAnual(balanco, balancosAnteriores));
		return balancoRepository.save(balanco);
	}
	
	@DeleteMapping()
	public void deletaBalanco(@RequestParam long id) {
		balancoRepository.deleteById(id);
	}
	
	@GetMapping("/buscaPorAcaoId")
	public List<Balanco> buscaBalancosByAcaoId(@RequestParam long acaoId){
		return balancoRepository.findByAcaoId(acaoId);
	}
	
	private long getLucroLiquidoAnual(Balanco balanco, List<Balanco> balancosAnteriores) {
		Map<Integer, Integer> mapTrimestres = getMapTrimestres(balanco.getData(), new HashMap<Integer, Integer>());
		return balancosAnteriores.stream()
					.filter(b -> verificaDatasBalancosAnteriores(b, mapTrimestres))
					.mapToLong(b -> b.getLucroLiquidoTrimestral())
					.sum();
	}
	
	private boolean verificaDatasBalancosAnteriores(Balanco balanco, Map<Integer, Integer> mapTrimestres) {
		Entry<Integer, Integer> resultado = mapTrimestres.entrySet().stream()
					.filter(mapTri -> mapTri.getKey() == balanco.getData().getYear() && mapTri.getValue() == balanco.getData().getMonthValue())
					.findAny()
					.orElse(null);
		
		return resultado != null ? true : false;
	}
	
	private Map<Integer, Integer> getMapTrimestres(LocalDate data, Map<Integer, Integer> mapTrimestres){
		int mes = data.getMonthValue() - 3;
		int ano = data.getYear();
		
		if(mes > 0) {
			mapTrimestres.put(ano, mes);
		}else {
			mes = 12;
			ano = ano - 1;
			mapTrimestres.put(ano, mes);
		}
		
		if(mapTrimestres.size() < 3) {
			LocalDate trimestreAnterior = LocalDate.of(ano, mes, 30);
			mapTrimestres.putAll(getMapTrimestres(trimestreAnterior, mapTrimestres));
		}
		
		return mapTrimestres;
	}
	
	private double getEvolucaoLucroLiquidoTrimestral(Balanco balanco, List<Balanco> balancosAnteriores) {

		Balanco balancoTrimestralAnterior = getBalancoTrimestralAnoAnterior(balanco, balancosAnteriores);
		
		if(balancoTrimestralAnterior == null)
			return 0;
		
		double diferencaLucroLiquidoTri = balanco.getLucroLiquidoTrimestral() - balancoTrimestralAnterior.getLucroLiquidoTrimestral();
		double evolucaoLucroLiquidoTri	= diferencaLucroLiquidoTri / balancoTrimestralAnterior.getLucroLiquidoTrimestral();
		
		return evolucaoLucroLiquidoTri;
	}
	
	private Balanco getBalancoTrimestralAnoAnterior(Balanco balanco, List<Balanco> balancosAnteriores) {
		LocalDate dataBalanco  = balanco.getData();

		Month mes = dataBalanco.getMonth();
		int ano   = dataBalanco.getYear();
		
		Balanco balancoTrimestralAnoAnterior = balancosAnteriores.stream()
					.filter(b -> verificaDataBalancoAnterior(b, mes, ano))
					.findAny()
					.orElse(null);
		
		return balancoTrimestralAnoAnterior;
	}
	
	private boolean verificaDataBalancoAnterior(Balanco balanco, Month mes, int ano) {
		LocalDate dataBalanco = balanco.getData();
		int anoAnterior		  = ano - 1;
		return mes.compareTo(dataBalanco.getMonth()) == 0 && dataBalanco.getYear() == anoAnterior ? true : false;
	}
}
