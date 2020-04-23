package com.bolsavalores.helper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.bolsavalores.entities.Balanco;

public class CalculadoraFundamentalista {
	
	public Double getMediaPrecoSobreLucro(Balanco balanco, List<Balanco> balancos) throws ParseException {
		Set<String> setTrimestres	     = getSetTrimestresAnteriores(balanco.getData(), new HashSet<String>(), 7);
		List<Balanco> balancosAnteriores = balancos.stream()
				.filter(b -> verificaDatasBalancosAnteriores(b, setTrimestres))
				.collect(Collectors.toList());
		
		if(balancosAnteriores == null || balancosAnteriores.size() != 7)
			return null;
		
		Double precoSobreLucro = balancosAnteriores.stream().mapToDouble(b -> b.getPrecoSobreLucro()).sum();
		precoSobreLucro+= balanco.getPrecoSobreLucro();
		precoSobreLucro/=8;
		
		return NumberFormat.getInstance().parse(new DecimalFormat("#.##").format(precoSobreLucro)).doubleValue();
	}
	
	public Double getMediaPrecoSobreValorPatrimonial(Balanco balanco, List<Balanco> balancos) throws ParseException {
		Set<String> setTrimestres	     = getSetTrimestresAnteriores(balanco.getData(), new HashSet<String>(), 7);
		List<Balanco> balancosAnteriores = balancos.stream()
				.filter(b -> verificaDatasBalancosAnteriores(b, setTrimestres))
				.collect(Collectors.toList());
		
		if(balancosAnteriores == null || balancosAnteriores.size() != 7)
			return null;
		
		Double precoSobreValorPatrimonial = balancosAnteriores.stream().mapToDouble(b -> b.getPrecoSobreValorPatrimonial()).sum();
		precoSobreValorPatrimonial+= balanco.getPrecoSobreValorPatrimonial();
		precoSobreValorPatrimonial/=8;
		
		return NumberFormat.getInstance().parse(new DecimalFormat("#.##").format(precoSobreValorPatrimonial)).doubleValue();
	}
	
	public int getNota(Balanco balanco) {
		int nota = 0;
		
		if(!isDadosBalancoValidos(balanco))
			return nota;
		
		if(balanco.getIsLucroCrescenteTresAnos())
			nota+=2;
		
		if(balanco.getIsLucroCrescenteTresMeses())
			nota+=2;
		
		if(balanco.getPrecoSobreLucro() <= 10)
			nota+=2;
		else if(balanco.getPrecoSobreLucro() <= 13)
			nota+=1;
		
		if(balanco.getPrecoSobreValorPatrimonial() <= 1.6)
			nota+=2;
		else if(balanco.getPrecoSobreValorPatrimonial() <= 2.3)
			nota+=1;
		
		if(balanco.getRoe() >= 20)
			nota+=2;
		else if(balanco.getRoe() >= 10)
			nota+=1;
		
		if(balanco.getDividaBrutaSobrePatrimonioLiq() <= 0.4)
			nota+=2;
		else if(balanco.getDividaBrutaSobrePatrimonioLiq() <= 0.8)
			nota+=1;
		
		return nota;
	}
	
	public Boolean isLucroCrescenteTresMeses(Balanco balanco, List<Balanco> balancos) {
		Set<String> setTrimestres 	     = getSetTrimestresAnteriores(balanco.getData(), new HashSet<String>(), 2);
		List<Balanco> balancosAnteriores = balancos.stream()
					.filter(b -> verificaDatasBalancosAnteriores(b, setTrimestres))
					.collect(Collectors.toList());
		
		boolean isLucroCrescenteUm   = false;
		boolean isLucroCrescenteDois = false;
		boolean isLucroCrescenteTres = false;
		
		if(balanco.getEvolucaoLucroLiquidoMeses() == null)
			return null;
		else
			isLucroCrescenteUm = balanco.getEvolucaoLucroLiquidoMeses() > 0;
			
		if(balancosAnteriores.get(0) == null || balancosAnteriores.get(0).getEvolucaoLucroLiquidoMeses() == null)
			return null;
		else
			isLucroCrescenteDois = balancosAnteriores.get(0).getEvolucaoLucroLiquidoMeses() > 0;
			
		if(balancosAnteriores.get(1) == null || balancosAnteriores.get(1).getEvolucaoLucroLiquidoMeses() == null)
			return null;
		else
			isLucroCrescenteTres = balancosAnteriores.get(1).getEvolucaoLucroLiquidoMeses() > 0;

		return isLucroCrescenteUm && isLucroCrescenteDois && isLucroCrescenteTres;
	}
	
	public Boolean isLucroCrescenteTresAnos(Balanco balanco, List<Balanco> balancos) {
		Set<String> setAnosAnteriores    = getSetAnosAnteriores(balanco.getData(), new HashSet<String>());
		List<Balanco> balancosAnosAnteriores = balancos.stream()
					.filter(b -> verificaDatasBalancosAnteriores(b, setAnosAnteriores))
					.collect(Collectors.toList());
		
		Collections.sort(balancosAnosAnteriores);
		
		boolean isLucroCrescenteUm   = false;
		boolean isLucroCrescenteDois = false;
		
		if(balanco.getLucroLiquidoAnual()  == null || balancosAnosAnteriores.size() != 2)
			return null;
		
		if(balancosAnosAnteriores.get(1).getLucroLiquidoAnual()  == null)
			return null;
		else
			isLucroCrescenteUm = balanco.getLucroLiquidoAnual() > balancosAnosAnteriores.get(1).getLucroLiquidoAnual();
		
		if(balancosAnosAnteriores.get(0) == null || balancosAnosAnteriores.get(0).getLucroLiquidoAnual() == null)
			return null;
		else
			isLucroCrescenteDois = balancosAnosAnteriores.get(1).getLucroLiquidoAnual() > balancosAnosAnteriores.get(0).getLucroLiquidoAnual();
			
		return isLucroCrescenteUm && isLucroCrescenteDois;
	}
	
	public Double getEvolucaoLucroLiquidoAnual(Balanco balanco, List<Balanco> balancosAnteriores) throws ParseException {
		
		Balanco balancoTrimestralAnterior = getBalancoTrimestralAnoAnterior(balanco, balancosAnteriores);
		  
		if(balancoTrimestralAnterior == null || balancoTrimestralAnterior.getLucroLiquidoAnual() == null)
			return null;
		
		if(balancoTrimestralAnterior.getLucroLiquidoAnual() == 0)
			balancoTrimestralAnterior.setLucroLiquidoAnual(1L);
		
		double diferencaLucroLiquidoAno = balanco.getLucroLiquidoAnual() - balancoTrimestralAnterior.getLucroLiquidoAnual();
		double evolucaoLucroLiquidoAno	= diferencaLucroLiquidoAno / balancoTrimestralAnterior.getLucroLiquidoAnual();
		
		return NumberFormat.getInstance().parse(new DecimalFormat("#.####").format(evolucaoLucroLiquidoAno)).doubleValue();
	}
	
	public Double getEvolucaoLucroLiquidoTrimestral(Balanco balanco, List<Balanco> balancosAnteriores) throws ParseException {

		Balanco balancoTrimestralAnterior = getBalancoTrimestralAnoAnterior(balanco, balancosAnteriores);
		
		if(balancoTrimestralAnterior == null)
			return null;
		
		if(balancoTrimestralAnterior.getLucroLiquidoTrimestral() == 0)
			balancoTrimestralAnterior.setLucroLiquidoTrimestral(1L);
		
		double diferencaLucroLiquidoTri = balanco.getLucroLiquidoTrimestral() - balancoTrimestralAnterior.getLucroLiquidoTrimestral();
		double evolucaoLucroLiquidoTri	= diferencaLucroLiquidoTri / balancoTrimestralAnterior.getLucroLiquidoTrimestral();
		
		return NumberFormat.getInstance().parse(new DecimalFormat("#.####").format(evolucaoLucroLiquidoTri)).doubleValue();
	}
	
	public Long getLucroLiquidoAnual(Balanco balanco, List<Balanco> balancosAnteriores) {
		Set<String> setTrimestres = getSetTrimestresAnteriores(balanco.getData(), new HashSet<String>(), 3);
		List<Long> lucrosLiquidoTri = balancosAnteriores.stream()
					.filter(b -> verificaDatasBalancosAnteriores(b, setTrimestres))
					.map(b -> b.getLucroLiquidoTrimestral())
					.collect(Collectors.toList());
		 
		return lucrosLiquidoTri != null && lucrosLiquidoTri.size() == 3 ? lucrosLiquidoTri.stream().reduce(0L, Long::sum) + balanco.getLucroLiquidoTrimestral() : null;
	}
	
	private boolean isDadosBalancoValidos(Balanco balanco) {
		return balanco.getIsLucroCrescenteTresAnos() != null && balanco.getIsLucroCrescenteTresMeses() != null ? true : false;
	}
	
	private boolean verificaDatasBalancosAnteriores(Balanco balanco, Set<String> listTrimestres) {
		String resultado = listTrimestres.stream()
					.filter(t -> teste(t, balanco))
					.findAny()
					.orElse(null);
		
		return resultado != null ? true : false;
	}
	
	private Boolean teste(String t, Balanco balanco) {
		return t.split("/")[0].equals(String.valueOf(balanco.getData().getYear())) && t.split("/")[1].equals(String.valueOf(balanco.getData().getMonthValue()));
	}
	
	private Set<String> getSetAnosAnteriores(LocalDate data, Set<String> setAno){
		int mes = data.getMonthValue();
		int ano = data.getYear() - 1;
		
		setAno.add(ano + "/" + mes);
		
		if(setAno.size() < 2) {
			LocalDate anoAnterior = LocalDate.of(ano, mes, 30);
			setAno.addAll(getSetAnosAnteriores(anoAnterior, setAno));
		}
		
		return setAno;
	}
	
	private Set<String> getSetTrimestresAnteriores(LocalDate data, Set<String> setTrimestres, int numeroTrimestres){
		int mes = data.getMonthValue() - 3;
		int ano = data.getYear();
		 
		if(mes <= 0) {
			mes = 12;
			ano = ano - 1;
		}
		
		setTrimestres.add(ano + "/" + mes);
		
		if(setTrimestres.size() < numeroTrimestres) {
			LocalDate trimestreAnterior = LocalDate.of(ano, mes, 30);
			setTrimestres.addAll(getSetTrimestresAnteriores(trimestreAnterior, setTrimestres, numeroTrimestres));
		}
		
		return setTrimestres;
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
