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

import com.bolsavalores.models.Balanco;
import com.bolsavalores.models.DesempenhoFinanceiro;
import com.bolsavalores.models.MultiplosFundamentalistas;

@Component
public class CalculadoraFundamentalista {
	
	private static final int NUMERO_ANOS_LUCROANUAL = 2;
	
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
	
	public Double getCaixaDisponivelSobreDividaBruta(long caixaDisponivel, long dividaBruta) throws ParseException{
		if(dividaBruta == 0)
			dividaBruta = 1L;
		
		double caixaDisponivelDouble = caixaDisponivel;
		double dividaBrutaDouble     = dividaBruta;
		
		double caixaDisponivelSobreDividaBruta = caixaDisponivelDouble / dividaBrutaDouble;
		return NumberFormat.getInstance().parse(new DecimalFormat("##.##").format(caixaDisponivelSobreDividaBruta)).doubleValue(); 
	}
	
	/*public long getDividaLiquida(long dividaBruta, long caixaDisponivel) {
		long dividaLiquida = dividaBruta - caixaDisponivel;
		return dividaLiquida;
	}*/
	
	/*public Double getEvolucaoDividaLiquidaAnual(Balanco balanco, List<Balanco> balancosAnteriores) throws ParseException {
		Balanco balancoTrimestralAnterior = getBalancoTrimestralAnoAnterior(balanco, balancosAnteriores);
		  
		if(balancoTrimestralAnterior == null || balancoTrimestralAnterior.getLucroLiquidoAnual() == null || balanco.getLucroLiquidoAnual() == null)
			return null;
		
		if(balancoTrimestralAnterior.getMultiplosFundamentalistas().getDividaliquida() == 0)
			balancoTrimestralAnterior.getMultiplosFundamentalistas().setDividaliquida(1L);
		
		double diferencaDividaLiquida = balanco.getMultiplosFundamentalistas().getDividaliquida() - balancoTrimestralAnterior.getMultiplosFundamentalistas().getDividaliquida();
		double evolucaoDividaLiquida  = diferencaDividaLiquida / balancoTrimestralAnterior.getMultiplosFundamentalistas().getDividaliquida();
		
		return NumberFormat.getInstance().parse(new DecimalFormat("##.####").format(evolucaoDividaLiquida)).doubleValue();
	}*/
	
	public Double getMediaPrecoSobreLucro(Balanco balanco, Double precoSobreLucroCorrente,List<Balanco> balancos) throws ParseException {
		if(precoSobreLucroCorrente == null)
			return null;
		
		Set<String> setTrimestres	     = getSetTrimestresAnteriores(balanco.getData(), new HashSet<String>(), 7);
		List<Balanco> balancosAnteriores = balancos.stream()
				.filter(b -> verificaDatasBalancosAnteriores(b, setTrimestres) && b.getMultiplosFundamentalistas().getPrecoSobreLucro() != null)
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
		String justificativa = "A empresa aprensenta: \r\n";
		
		if(!isDadosBalancoValidos(balanco.getMultiplosFundamentalistas(), balanco.getDesempenhoFinanceiro())) {
			balanco.setJustificativaNota("Este balanço não possui informações suficientes para darmos uma Nota.");
			return nota;
		}
		
		if(!validaRequisitosMinimos(balanco.getMultiplosFundamentalistas(), balanco.getDesempenhoFinanceiro())) {
			balanco.setJustificativaNota("Este balanço não possui os requisítos mínimos necessários. \r\n - ROE acima de 10%; \r\n - Dívida equilibrada não ultrapassando 80% do valor do Patrimônio; \r\n - Lucro Líquido crescente nos últimos três anos; ");
			return nota;
		}
		
		if(balanco.getMultiplosFundamentalistas().getPrecoSobreLucro() <= 10) {
			justificativa+="- Relação entre Preço(cotação) e Lucro Líquido anual excelente, não ultrapassando a faixa de 10 (+2 pts); \r\n";
			nota+=2;
		}else if(balanco.getMultiplosFundamentalistas().getPrecoSobreLucro() <= 13) {
			justificativa+="- Boa relação entre Preço(cotação) e Lucro Líquido anual, não ultrapassando a faixa de 13 (+1 pts); \r\n";
			nota+=1;
		}
		
		if(balanco.getMultiplosFundamentalistas().getPrecoSobreValorPatrimonial() <= 1.6) {
			justificativa+="- Uma excelente relação entre Preço(cotação) e Patrimônio Líquido, não ultrapassando a faixa de 1.5 (+2 pts); \r\n";
			nota+=2;
		}else if(balanco.getMultiplosFundamentalistas().getPrecoSobreValorPatrimonial() <= 2.3) {
			justificativa+="- Uma boa relação entre Preço(cotação) e Patrimônio Líquido, não ultrapassando a faixa de 2.3 (+1 pts); \r\n";
			nota+=1;
		}
		
		if(balanco.getMultiplosFundamentalistas().getRoe() >= 0.2) {
			justificativa+="- Retorno sobre Patrimônio Líquido(ROE) muito bom, acima de 20% (+2 pts); \r\n";
			nota+=2;
		}else if(balanco.getMultiplosFundamentalistas().getRoe() >= 0.15) {
			justificativa+="- Um belo Retorno sobre Patrimônio Líquido(ROE), acima de 15% (+1 pts); \r\n";
			nota+=1;
		}
		
		if(balanco.getMultiplosFundamentalistas().getDividaBrutaSobrePatrimonioLiquido() <= 0.3) {
			justificativa+="- Relação entre a Dívida bruta perante o Patrimônio Líquido excelente, ficando abaixo de 30% (+2 pts); \r\n";
			nota+=2;
		} else if(balanco.getMultiplosFundamentalistas().getDividaBrutaSobrePatrimonioLiquido() <= 0.6) {
			justificativa+="- Boa relação entre a Dívida bruta perante o Patrimônio Líquido, abaixo de 60% (+1 pts); \r\n";
			nota+=1;
		}
		
		if(balanco.getMultiplosFundamentalistas().getCaixaDisponivelSobreDividaBruta() >= 2) {
			justificativa+="- Excelente proporção do Caixa disponível perante a Dívida bruta, acima de 2 (+2 pts); \r\n";
			nota+=2;
		}else if(balanco.getMultiplosFundamentalistas().getCaixaDisponivelSobreDividaBruta() >= 1) {
			justificativa+="- Proporção do Caixa disponível perante a Dívida bruta boa, acima de 1 (+1 pts); \r\n";
			nota+=1;
		}
		
		balanco.setJustificativaNota(justificativa);
		
		return nota;
	}
	
	private boolean validaRequisitosMinimos(MultiplosFundamentalistas multiplos, DesempenhoFinanceiro desempenho) {
		if(multiplos.getRoe() < 0.1)
			return false;
		
		if(multiplos.getDividaBrutaSobrePatrimonioLiquido() > 0.8)
			return false;
		
		if(!desempenho.getHasCrescimentoLucroLiquidoTresAnos())
			return false;
					
		return true;
	}
	
	public Boolean hasLucroCrescenteTresAnos(Balanco balanco, List<Balanco> balancos) {
		Set<String> setAnosAnteriores        = getSetAnosAnteriores(balanco.getData(), new HashSet<String>());
		List<Balanco> balancosAnosAnteriores = balancos.stream()
					.filter(b -> verificaDatasBalancosAnteriores(b, setAnosAnteriores))
					.collect(Collectors.toList());
		
		Collections.sort(balancosAnosAnteriores);
		
		boolean isLucroCrescenteUm   = false;
		boolean isLucroCrescenteDois = false;
		
		if(balanco.getLucroLiquidoAnual()  == null || balancosAnosAnteriores.size() != NUMERO_ANOS_LUCROANUAL)
			return null;
		
		/*if(balancosAnosAnteriores.get(2).getLucroLiquidoAnual()  == null)
			return null;
		else
			isLucroCrescenteUm = balanco.getLucroLiquidoAnual() > balancosAnosAnteriores.get(2).getLucroLiquidoAnual();
		
		if(balancosAnosAnteriores.get(1).getLucroLiquidoAnual()  == null)
			return null;
		else
			isLucroCrescenteDois = balancosAnosAnteriores.get(2).getLucroLiquidoAnual() > balancosAnosAnteriores.get(1).getLucroLiquidoAnual();
		
		if(balancosAnosAnteriores.get(0) == null || balancosAnosAnteriores.get(0).getLucroLiquidoAnual() == null)
			return null;
		else
			isLucroCrescenteTres = balancosAnosAnteriores.get(1).getLucroLiquidoAnual() > balancosAnosAnteriores.get(0).getLucroLiquidoAnual();*/
		
		if(balancosAnosAnteriores.get(1).getLucroLiquidoAnual()  == null)
			return null;
		else
			isLucroCrescenteUm = balanco.getLucroLiquidoAnual() > balancosAnosAnteriores.get(1).getLucroLiquidoAnual();
		
		if(balancosAnosAnteriores.get(0).getLucroLiquidoAnual()  == null)
			return null;
		else
			isLucroCrescenteDois = balancosAnosAnteriores.get(1).getLucroLiquidoAnual() > balancosAnosAnteriores.get(0).getLucroLiquidoAnual();
		
		return isLucroCrescenteUm && isLucroCrescenteDois;
	}
	
	/*public Boolean hasDividaLiquidaCrescenteTresAnos(Balanco balanco, List<Balanco> balancos, Long dividaLiquidaCorrente) {
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
	}*/
	
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
	
	private boolean isDadosBalancoValidos(MultiplosFundamentalistas multiplos, DesempenhoFinanceiro desempenho) {
		return multiplos != null && desempenho != null &&
			   multiplos.getRoe() != null && multiplos.getDividaBrutaSobrePatrimonioLiquido() != null &&
			   multiplos.getPrecoSobreLucro() != null && multiplos.getPrecoSobreValorPatrimonial() != null &&
			   multiplos.getCaixaDisponivelSobreDividaBruta() != null &&
			   desempenho.getHasCrescimentoLucroLiquidoTresAnos() != null ?
					   true : false;
	}
	
	private boolean verificaDatasBalancosAnteriores(Balanco balanco, Set<String> listTrimestres) {
		String resultado = listTrimestres.stream()
					.filter(t -> t.split("/")[0].equals(String.valueOf(balanco.getData().getYear())) && t.split("/")[1].equals(String.valueOf(balanco.getData().getMonthValue())))
					.findAny()
					.orElse(null);
		
		return resultado != null ? true : false;
	}
	
	private Set<String> getSetAnosAnteriores(LocalDate data, Set<String> setAno){
		int mes = data.getMonthValue();
		int ano = data.getYear() - 1;
		
		setAno.add(ano + "/" + mes);
		
		if(setAno.size() < NUMERO_ANOS_LUCROANUAL) {
			LocalDate anoAnterior = LocalDate.of(ano, mes, 01);
			setAno.addAll(getSetAnosAnteriores(anoAnterior, setAno));
		}
		
		return setAno;
	}
	
	private Set<String> getSetTrimestresAnteriores(LocalDate data, Set<String> setTrimestres, int numeroTrimestres){
		int mes = data.getMonthValue() - 3;
		int ano = data.getYear();
		 
		if(mes <= 0) {
			mes = 11;
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
