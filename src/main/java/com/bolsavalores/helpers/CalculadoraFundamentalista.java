package com.bolsavalores.helpers;

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

import org.springframework.stereotype.Component;

import com.bolsavalores.entities.Balanco;

@Component
public class CalculadoraFundamentalista {
	
	public Double getPrecoSobreLucro(double cotacao, long qtdPapeis, Long lucroLiquidoAnual) throws ParseException{
		if(lucroLiquidoAnual == null || lucroLiquidoAnual == 0L)
			return null;
		
		double qtdPapeisDouble 	       = qtdPapeis;
		double lucroLiquidoAnualDouble = lucroLiquidoAnual;
		
		double lucroPorAcao    = lucroLiquidoAnualDouble / qtdPapeisDouble;
		double precoSobreLucro = cotacao / lucroPorAcao;
		return NumberFormat.getInstance().parse(new DecimalFormat("##.##").format(precoSobreLucro)).doubleValue(); 
	}
	
	public Double getPrecoSobreValorPatrimonial(double cotacao, long qtdPapeis, long patrimonioLiquido) throws ParseException {
		double qtdPapeisDouble 	       = qtdPapeis;
		double patrimonioLiquidoDouble = patrimonioLiquido;
		
		double valorPatrimonialPorAcao    = patrimonioLiquidoDouble / qtdPapeisDouble;
		double precoSobreValorPatrimonial = cotacao / valorPatrimonialPorAcao;
		return NumberFormat.getInstance().parse(new DecimalFormat("##.##").format(precoSobreValorPatrimonial)).doubleValue(); 
	}
	
	public Double getRoe(Long lucroLiquidoAnual, long patrimonioLiquido) throws ParseException {
		if(lucroLiquidoAnual == null || lucroLiquidoAnual == 0L)
			return null;
	
		double lucroLiquidoAnualDouble = lucroLiquidoAnual;
		double patrimonioLiquidoDouble = patrimonioLiquido;
		
		double roe = lucroLiquidoAnualDouble / patrimonioLiquidoDouble;
		return NumberFormat.getInstance().parse(new DecimalFormat("##.##").format(roe)).doubleValue(); 
	}
	
	public double getDividaBrutaSobrePatrimonioLiquido(long dividaBruta, long patrimonioLiquido) throws ParseException{
		double dividaBrutaDouble	   = dividaBruta;
		double patrimonioLiquidoDouble = patrimonioLiquido;
		
		double dividaBrutaSobrePatrimonioLiquido = dividaBrutaDouble / patrimonioLiquidoDouble ;
		return NumberFormat.getInstance().parse(new DecimalFormat("##.##").format(dividaBrutaSobrePatrimonioLiquido)).doubleValue(); 
	}
	
	public long getDividaLiquida(long dividaBruta, long caixaDisponivel) {
		long dividaLiquida = dividaBruta - caixaDisponivel;
		return dividaLiquida;
	}
	
	public Double getEvolucaoDividaLiquidaAnual(Balanco balanco, List<Balanco> balancosAnteriores) throws ParseException {
		Balanco balancoTrimestralAnterior = getBalancoTrimestralAnoAnterior(balanco, balancosAnteriores);
		  
		if(balancoTrimestralAnterior == null || balancoTrimestralAnterior.getLucroLiquidoAnual() == null || balanco.getLucroLiquidoAnual() == null)
			return null;
		
		if(balancoTrimestralAnterior.getMultiplosFundamentalistas().getDividaliquida() == 0)
			balancoTrimestralAnterior.getMultiplosFundamentalistas().setDividaliquida(1L);
		
		double diferencaDividaLiquida = balanco.getMultiplosFundamentalistas().getDividaliquida() - balancoTrimestralAnterior.getMultiplosFundamentalistas().getDividaliquida();
		double evolucaoDividaLiquida  = diferencaDividaLiquida / balancoTrimestralAnterior.getMultiplosFundamentalistas().getDividaliquida();
		
		return NumberFormat.getInstance().parse(new DecimalFormat("##.####").format(evolucaoDividaLiquida)).doubleValue();
	}
	
	public Double getMediaPrecoSobreLucro(Balanco balanco, Double precoSobreLucroCorrente,List<Balanco> balancos) throws ParseException {
		if(precoSobreLucroCorrente == null)
			return null;
		
		Set<String> setTrimestres	     = getSetTrimestresAnteriores(balanco.getData(), new HashSet<String>(), 7);
		List<Balanco> balancosAnteriores = balancos.stream()
				.filter(b -> verificaDatasBalancosAnteriores(b, setTrimestres))
				.collect(Collectors.toList());
		
		if(balancosAnteriores == null || balancosAnteriores.size() != 7)
			return null;
		
		Double precoSobreLucro = balancosAnteriores.stream().mapToDouble(b -> b.getMultiplosFundamentalistas().getPrecoSobreLucro()).sum();
		precoSobreLucro+=precoSobreLucroCorrente;
		precoSobreLucro/=8;
		
		return NumberFormat.getInstance().parse(new DecimalFormat("##.##").format(precoSobreLucro)).doubleValue();
	}
	
	public Double getMediaPrecoSobreValorPatrimonial(Balanco balanco, Double precoSobreValorPatrimonialCorrente, List<Balanco> balancos) throws ParseException {
		Set<String> setTrimestres	     = getSetTrimestresAnteriores(balanco.getData(), new HashSet<String>(), 7);
		List<Balanco> balancosAnteriores = balancos.stream()
				.filter(b -> verificaDatasBalancosAnteriores(b, setTrimestres))
				.collect(Collectors.toList());
		
		if(balancosAnteriores == null || balancosAnteriores.size() != 7)
			return null;
		
		Double precoSobreValorPatrimonial = balancosAnteriores.stream().mapToDouble(b -> b.getMultiplosFundamentalistas().getPrecoSobreValorPatrimonial()).sum();
		precoSobreValorPatrimonial+=precoSobreValorPatrimonialCorrente;
		precoSobreValorPatrimonial/=8;
		
		return NumberFormat.getInstance().parse(new DecimalFormat("#.##").format(precoSobreValorPatrimonial)).doubleValue();
	}
	
	public int getNota(Balanco balanco) {
		int nota = 0;
		
		if(!isDadosBalancoValidos(balanco))
			return nota;
		
		if(balanco.getDesempenhoFinanceiro().getHasCrescimentoLucroLiquidoTresTrimestres())
			nota+=2;
		
		if(balanco.getDesempenhoFinanceiro().getHasCrescimentoLucroLiquidoTresAnos())
			nota+=2;
		
		if(balanco.getDesempenhoFinanceiro().getHasCrescimentoDividaLiquidaTresAnos())
			nota-=2;
		
		if(balanco.getMultiplosFundamentalistas().getPrecoSobreLucro() <= 10)
			nota+=2;
		else if(balanco.getMultiplosFundamentalistas().getPrecoSobreLucro() <= 13)
			nota+=1;
		
		if(balanco.getMultiplosFundamentalistas().getPrecoSobreValorPatrimonial() <= 1.6)
			nota+=2;
		else if(balanco.getMultiplosFundamentalistas().getPrecoSobreValorPatrimonial() <= 2.3)
			nota+=1;
		
		if(balanco.getMultiplosFundamentalistas().getRoe() >= 20)
			nota+=2;
		else if(balanco.getMultiplosFundamentalistas().getRoe() >= 10)
			nota+=1;
		
		if(balanco.getMultiplosFundamentalistas().getDividaBrutaSobrePatrimonioLiquido() <= 0.4)
			nota+=2;
		else if(balanco.getMultiplosFundamentalistas().getDividaBrutaSobrePatrimonioLiquido() <= 0.8)
			nota+=1;
		
		if(balanco.getMultiplosFundamentalistas().getDividaliquida() < 0)
			nota+=2;
		else if(balanco.getMultiplosFundamentalistas().getDividaliquida() == 0)
			nota+=1;

		return nota;
	}
	
	public Boolean hasLucroCrescenteTresMeses(Balanco balanco, List<Balanco> balancos, Double evolucaoLucroLiquidoTrimestralCorrente) {
		Set<String> setTrimestres 	     = getSetTrimestresAnteriores(balanco.getData(), new HashSet<String>(), 2);
		List<Balanco> balancosAnteriores = balancos.stream()
					.filter(b -> verificaDatasBalancosAnteriores(b, setTrimestres))
					.collect(Collectors.toList());
		
		boolean isLucroCrescenteUm   = false;
		boolean isLucroCrescenteDois = false;
		boolean isLucroCrescenteTres = false; 
		
		if(evolucaoLucroLiquidoTrimestralCorrente == null)
			return null;
		else
			isLucroCrescenteUm = evolucaoLucroLiquidoTrimestralCorrente > 0;
			
		if(balancosAnteriores.size() < 1 || balancosAnteriores.get(0).getDesempenhoFinanceiro().getEvolucaoLucroLiquidoTrimestral() == null)
			return null;
		else
			isLucroCrescenteDois = balancosAnteriores.get(0).getDesempenhoFinanceiro().getEvolucaoLucroLiquidoTrimestral() > 0;
		
		if(balancosAnteriores.size() < 2 || balancosAnteriores.get(1).getDesempenhoFinanceiro().getEvolucaoLucroLiquidoTrimestral() == null)
			return null;
		else
			isLucroCrescenteTres = balancosAnteriores.get(1).getDesempenhoFinanceiro().getEvolucaoLucroLiquidoTrimestral() > 0;
		
		return isLucroCrescenteUm && isLucroCrescenteDois && isLucroCrescenteTres;
	}
	
	public Boolean hasLucroCrescenteTresAnos(Balanco balanco, List<Balanco> balancos) {
		Set<String> setAnosAnteriores        = getSetAnosAnteriores(balanco.getData(), new HashSet<String>());
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
	
	public Boolean hasDividaLiquidaCrescenteTresAnos(Balanco balanco, List<Balanco> balancos, Long dividaLiquidaCorrente) {
		Set<String> setAnosAnteriores        = getSetAnosAnteriores(balanco.getData(), new HashSet<String>());
		List<Balanco> balancosAnosAnteriores = balancos.stream()
					.filter(b -> verificaDatasBalancosAnteriores(b, setAnosAnteriores))
					.collect(Collectors.toList());
		
		Collections.sort(balancosAnosAnteriores);
		
		boolean hasDividaLiquidaCrescenteUm   = false;
		boolean hasDividaLiquidaCrescenteDois = false;
		
		if(dividaLiquidaCorrente  == null || balancosAnosAnteriores.size() != 2)
			return null;
		
		if(balancosAnosAnteriores.get(1).getMultiplosFundamentalistas().getDividaliquida()  == null)
			return null;
		else
			hasDividaLiquidaCrescenteUm = dividaLiquidaCorrente > balancosAnosAnteriores.get(1).getMultiplosFundamentalistas().getDividaliquida();
		
		if(balancosAnosAnteriores.get(0) == null || balancosAnosAnteriores.get(0).getMultiplosFundamentalistas().getDividaliquida() == null)
			return null;
		else
			hasDividaLiquidaCrescenteDois = balancosAnosAnteriores.get(1).getMultiplosFundamentalistas().getDividaliquida() > balancosAnosAnteriores.get(0).getMultiplosFundamentalistas().getDividaliquida();
			
		return hasDividaLiquidaCrescenteUm && hasDividaLiquidaCrescenteDois;
	}
	
	public Double getEvolucaoLucroLiquidoAnual(Balanco balanco, List<Balanco> balancosAnteriores) throws ParseException {
		
		Balanco balancoTrimestralAnterior = getBalancoTrimestralAnoAnterior(balanco, balancosAnteriores);
		  
		if(balancoTrimestralAnterior == null || balancoTrimestralAnterior.getLucroLiquidoAnual() == null || balanco.getLucroLiquidoAnual() == null)
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
		return balanco.getDesempenhoFinanceiro() != null && 
			   balanco.getDesempenhoFinanceiro().getHasCrescimentoLucroLiquidoTresTrimestres() != null && 
			   balanco.getDesempenhoFinanceiro().getHasCrescimentoLucroLiquidoTresAnos() != null &&
			   balanco.getDesempenhoFinanceiro().getHasCrescimentoDividaLiquidaTresAnos() != null ? 
					   true : false;
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
			LocalDate anoAnterior = LocalDate.of(ano, mes, 01);
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
			LocalDate trimestreAnterior = LocalDate.of(ano, mes, 01);
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
