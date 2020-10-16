package com.bolsavalores.helpers;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
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
	private static final String TRIMESTRE_SEPARATOR = "T";
	
	public Double getPrecoSobreLucro(double cotacao, long qtdPapeis, Long lucroLiquidoAnual) throws ParseException{
		if(lucroLiquidoAnual == null || lucroLiquidoAnual == 0L)
			return null;
		
		double qtdPapeisDouble 	       = qtdPapeis;
		double lucroLiquidoAnualDouble = lucroLiquidoAnual;
		
		double lucroPorAcao    = lucroLiquidoAnualDouble / qtdPapeisDouble;
		double precoSobreLucro = cotacao / lucroPorAcao;
		return NumberFormat.getInstance().parse(new DecimalFormat("##.##").format(precoSobreLucro)).doubleValue(); 
	}
	
	public Double getPrecoSobreValorPatrimonial(double cotacao, long qtdPapeis, Long patrimonioLiquido) throws ParseException {
		if(patrimonioLiquido == null || patrimonioLiquido == 0L)
			return null;
			
		double qtdPapeisDouble 	       = qtdPapeis;
		double patrimonioLiquidoDouble = patrimonioLiquido;
		
		double valorPatrimonialPorAcao    = patrimonioLiquidoDouble / qtdPapeisDouble;
		double precoSobreValorPatrimonial = cotacao / valorPatrimonialPorAcao;
		return NumberFormat.getInstance().parse(new DecimalFormat("##.##").format(precoSobreValorPatrimonial)).doubleValue(); 
	}
	
	public Double getRoe(Long lucroLiquidoAnual, Long patrimonioLiquido) throws ParseException {
		if(lucroLiquidoAnual == null || lucroLiquidoAnual == 0L || patrimonioLiquido == null || patrimonioLiquido == 0L)
			return null;
	
		double lucroLiquidoAnualDouble = lucroLiquidoAnual;
		double patrimonioLiquidoDouble = patrimonioLiquido;
		
		double roe = lucroLiquidoAnualDouble / patrimonioLiquidoDouble;
		return NumberFormat.getInstance().parse(new DecimalFormat("##.##").format(roe)).doubleValue(); 
	}
	
	public Double getDividaBrutaSobrePatrimonioLiquido(long dividaBruta, long patrimonioLiquido) throws ParseException{
		if(patrimonioLiquido == 0L)
			return null;
		
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
	
	public Double getMediaPrecoSobreLucro(Balanco balanco, Double precoSobreLucroCorrente, List<Balanco> balancos, long acaoId) throws ParseException {
		if(precoSobreLucroCorrente == null)
			return null;
		
		Set<String> setTrimestres = getTrimestresAnteriores(balanco.getTrimestre(), balanco.getAno(), 7, new HashSet<String>());
		List<Balanco> balancosAnteriores = balancos.stream()
				.filter(b -> verificaDatasBalancosAnteriores(b, setTrimestres) && b.getMultiplosFundamentalistasByAcaoId(acaoId).getPrecoSobreLucro() != null)
				.collect(Collectors.toList());
		
		if(balancosAnteriores == null || balancosAnteriores.size() != 7)
			return null;
		
		Double precoSobreLucro = balancosAnteriores.stream().mapToDouble(b -> b.getMultiplosFundamentalistasByAcaoId(acaoId).getPrecoSobreLucro()).sum();
		precoSobreLucro+=precoSobreLucroCorrente;
		precoSobreLucro/=8;
		
		return NumberFormat.getInstance().parse(new DecimalFormat("##.##").format(precoSobreLucro)).doubleValue();
	}
	
	public Double getMediaPrecoSobreValorPatrimonial(Balanco balanco, Double precoSobreValorPatrimonialCorrente, List<Balanco> balancos, long acaoId) throws ParseException {
		if(precoSobreValorPatrimonialCorrente == null)
			return null;
		
		Set<String> setTrimestres = getTrimestresAnteriores(balanco.getTrimestre(), balanco.getAno(), 7, new HashSet<String>());
		List<Balanco> balancosAnteriores = balancos.stream()
				.filter(b -> verificaDatasBalancosAnteriores(b, setTrimestres) && b.getMultiplosFundamentalistasByAcaoId(acaoId).getPrecoSobreValorPatrimonial() != null)
				.collect(Collectors.toList());
		
		if(balancosAnteriores == null || balancosAnteriores.size() != 7)
			return null;
		
		Double precoSobreValorPatrimonial = balancosAnteriores.stream().mapToDouble(b -> b.getMultiplosFundamentalistasByAcaoId(acaoId).getPrecoSobreValorPatrimonial()).sum();
		precoSobreValorPatrimonial+=precoSobreValorPatrimonialCorrente;
		precoSobreValorPatrimonial/=8;
		
		return NumberFormat.getInstance().parse(new DecimalFormat("#.##").format(precoSobreValorPatrimonial)).doubleValue();
	}
	
	public Avaliacao getNota(Balanco balanco, MultiplosFundamentalistas multiplos) {
		int nota = 0;
		String justificativa = "A empresa apresenta: \r\n";
		
		if(!isDadosBalancoValidos(multiplos, balanco.getDesempenhoFinanceiro())) {
			justificativa="Este balanço não possui informações suficientes para darmos uma Nota.";
			return new Avaliacao(nota, justificativa);
		}
		
		if(!validaRequisitosMinimos(multiplos, balanco.getDesempenhoFinanceiro())) {
			justificativa="Este balanço não possui os requisítos mínimos necessários. \r\n - ROE acima de 10%; \r\n - Dívida equilibrada não ultrapassando 80% do valor do Patrimônio; \r\n - Lucro Líquido crescente nos últimos três anos; ";
			return new Avaliacao(nota, justificativa);
		}
		
		if(multiplos.getPrecoSobreLucro() <= 10) {
			justificativa+="- Relação entre Preço(cotação) e Lucro Líquido anual excelente, não ultrapassando a faixa de 10 (+2 pts); \r\n";
			nota+=2;
		}else if(multiplos.getPrecoSobreLucro() <= 13) {
			justificativa+="- Boa relação entre Preço(cotação) e Lucro Líquido anual, não ultrapassando a faixa de 13 (+1 pts); \r\n";
			nota+=1;
		}
		
		if(multiplos.getPrecoSobreValorPatrimonial() <= 1.6) {
			justificativa+="- Uma excelente relação entre Preço(cotação) e Patrimônio Líquido, não ultrapassando a faixa de 1.5 (+2 pts); \r\n";
			nota+=2;
		}else if(multiplos.getPrecoSobreValorPatrimonial() <= 2.3) {
			justificativa+="- Uma boa relação entre Preço(cotação) e Patrimônio Líquido, não ultrapassando a faixa de 2.3 (+1 pts); \r\n";
			nota+=1;
		}
		
		if(multiplos.getRoe() >= 0.2) {
			justificativa+="- Retorno sobre Patrimônio Líquido(ROE) muito bom, acima de 20% (+2 pts); \r\n";
			nota+=2;
		}else if(multiplos.getRoe() >= 0.15) {
			justificativa+="- Um belo Retorno sobre Patrimônio Líquido(ROE), acima de 15% (+1 pts); \r\n";
			nota+=1;
		}
		
		if(multiplos.getDividaBrutaSobrePatrimonioLiquido() <= 0.3) {
			justificativa+="- Relação entre a Dívida bruta perante o Patrimônio Líquido excelente, ficando abaixo de 30% (+2 pts); \r\n";
			nota+=2;
		} else if(multiplos.getDividaBrutaSobrePatrimonioLiquido() <= 0.6) {
			justificativa+="- Boa relação entre a Dívida bruta perante o Patrimônio Líquido, abaixo de 60% (+1 pts); \r\n";
			nota+=1;
		}
		
		if(multiplos.getCaixaDisponivelSobreDividaBruta() >= 2) {
			justificativa+="- Excelente proporção do Caixa disponível perante a Dívida bruta, acima de 2 (+2 pts); \r\n";
			nota+=2;
		}else if(multiplos.getCaixaDisponivelSobreDividaBruta() >= 1) {
			justificativa+="- Proporção do Caixa disponível perante a Dívida bruta boa, acima de 1 (+1 pts); \r\n";
			nota+=1;
		}
		
		return new Avaliacao(nota, justificativa);
	}
	
	public boolean validaRequisitosMinimos(MultiplosFundamentalistas multiplos, DesempenhoFinanceiro desempenho) {
		if(multiplos.getRoe() < 0.1)
			return false;
		
		if(multiplos.getDividaBrutaSobrePatrimonioLiquido() > 0.8)
			return false;
		
		if(!desempenho.getHasCrescimentoLucroLiquidoTresAnos())
			return false;
					
		return true;
	}
	
	public Boolean hasLucroCrescenteTresAnos(Balanco balanco, List<Balanco> balancos) {
		Set<String> setAnosAnteriores = getAnosAnteriores(balanco.getTrimestre(), balanco.getAno(), new HashSet<String>());
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
		Set<String> setTrimestres = getTrimestresAnteriores(balanco.getTrimestre(), balanco.getAno(), 3, new HashSet<String>());
		List<Long> lucrosLiquidoTri = balancosAnteriores.stream()
					.filter(b -> verificaDatasBalancosAnteriores(b, setTrimestres))
					.map(b -> b.getLucroLiquidoTrimestral())
					.collect(Collectors.toList());
		  
		return lucrosLiquidoTri != null && lucrosLiquidoTri.size() == 3 ? lucrosLiquidoTri.stream().reduce(0L, Long::sum) + balanco.getLucroLiquidoTrimestral() : null;
	}
	
	public boolean isDadosBalancoValidos(MultiplosFundamentalistas multiplos, DesempenhoFinanceiro desempenho) {
		return multiplos != null && desempenho != null &&
			   multiplos.getRoe() != null && multiplos.getDividaBrutaSobrePatrimonioLiquido() != null &&
			   multiplos.getPrecoSobreLucro() != null && multiplos.getPrecoSobreValorPatrimonial() != null &&
			   multiplos.getCaixaDisponivelSobreDividaBruta() != null &&
			   desempenho.getHasCrescimentoLucroLiquidoTresAnos() != null ?
					   true : false;
	}
	
	private boolean verificaDatasBalancosAnteriores(Balanco balanco, Set<String> listTrimestres) {
		String resultado = listTrimestres.stream()
					.filter(t -> t.split(TRIMESTRE_SEPARATOR)[0].equals(String.valueOf(balanco.getTrimestre())) && t.split(TRIMESTRE_SEPARATOR)[1].equals(String.valueOf(balanco.getAno())))
					.findAny()
					.orElse(null);
		
		return resultado != null ? true : false;
	}
	
	private Set<String> getAnosAnteriores(int trimestre, int ano, Set<String> setAno){
		ano-=1;
		
		setAno.add(trimestre + TRIMESTRE_SEPARATOR + ano);
		
		if(setAno.size() < NUMERO_ANOS_LUCROANUAL)
			setAno.addAll(getAnosAnteriores(trimestre, ano, setAno));
		
		return setAno;
	}
	
	private Set<String> getTrimestresAnteriores(int trimestre, int ano, int numeroTrimestres, Set<String> setTrimestres){
		trimestre-=1;
		
		if(trimestre <= 0) {
			trimestre = 4;
			ano-=1;
		}
		
		setTrimestres.add(trimestre + "T" + ano);
		
		if(setTrimestres.size() < numeroTrimestres) 
			setTrimestres.addAll(getTrimestresAnteriores(trimestre, ano, numeroTrimestres, setTrimestres));
		
		return setTrimestres;
	}
	
	private Balanco getBalancoTrimestralAnoAnterior(Balanco balanco, List<Balanco> balancosAnteriores) {
		Balanco balancoTrimestralAnoAnterior = balancosAnteriores.stream()
					.filter(b -> verificaDataBalancoAnterior(b, balanco.getTrimestre(), balanco.getAno()))
					.findAny()
					.orElse(null);
		
		return balancoTrimestralAnoAnterior;
	}
	
	private boolean verificaDataBalancoAnterior(Balanco balanco, int trimestre, int ano) {
		int anoAnterior	= ano - 1;
		return trimestre == balanco.getTrimestre() && balanco.getAno() == anoAnterior ? true : false;
	}
}
