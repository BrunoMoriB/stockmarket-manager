package com.bolsavalores.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="balanco")
public class Balanco implements Serializable, Comparable<Balanco> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Balanco() {}

	public Balanco(Empresa empresa) {
		this.empresa = empresa;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@ManyToOne()
	@JoinColumn(name="id_empresa")
	private Empresa empresa;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="balanco")
	private List<MultiplosFundamentalistas> multiplosFundamentalistas;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="balanco")
	private DesempenhoFinanceiro desempenhoFinanceiro;
	
	private LocalDate data;
	
	@Column(name="lucroliq_trimestral")
	private Long lucroLiquidoTrimestral;
	
	@Column(name="lucroliq_anual")
	private Long lucroLiquidoAnual;
	
	@Column(name="patrimonioliquido")
	private Long patrimonioLiquido;
	
	@Column(name="dividabruta")
	private Long dividaBruta;
	
	@Column(name="caixadisponivel")
	private Long caixaDisponivel;
	
	@Column(name="isdailyupdated")
	private boolean dailyUpdated;
	
	private String trimestre;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Long getLucroLiquidoTrimestral() {
		return lucroLiquidoTrimestral;
	}

	public void setLucroLiquidoTrimestral(Long lucroLiquidoTrimestral) {
		this.lucroLiquidoTrimestral = lucroLiquidoTrimestral;
	}

	public Long getLucroLiquidoAnual() {
		return lucroLiquidoAnual;
	}

	public void setLucroLiquidoAnual(Long lucroLiquidoAnual) {
		this.lucroLiquidoAnual = lucroLiquidoAnual;
	}

	public String getTrimestre() {
		return trimestre;
	}

	public void setTrimestre(String trimestre) {
		this.trimestre = trimestre;
	}

	public DesempenhoFinanceiro getDesempenhoFinanceiro() {
		return desempenhoFinanceiro;
	}

	public void setDesempenhoFinanceiro(DesempenhoFinanceiro desempenhoFinanceiro) {
		this.desempenhoFinanceiro = desempenhoFinanceiro;
	}

	public Long getPatrimonioLiquido() {
		return patrimonioLiquido;
	}

	public void setPatrimonioLiquido(Long patrimonioLiquido) {
		this.patrimonioLiquido = patrimonioLiquido;
	}

	public Long getDividaBruta() {
		return dividaBruta;
	}

	public void setDividaBruta(Long dividaBruta) {
		this.dividaBruta = dividaBruta;
	}

	public Long getCaixaDisponivel() {
		return caixaDisponivel;
	}

	public void setCaixaDisponivel(Long caixaDisponivel) {
		this.caixaDisponivel = caixaDisponivel;
	}

	public boolean isDailyUpdated() {
		return dailyUpdated;
	}

	public void setDailyUpdated(boolean isDailyUpdated) {
		this.dailyUpdated = isDailyUpdated;
	}

	public Empresa getEmpresa() {
		return empresa;
	}
	
	public List<MultiplosFundamentalistas> getMultiplosFundamentalistas() {
		return multiplosFundamentalistas;
	}
	
	public MultiplosFundamentalistas getMultiplosFundamentalistasByAcaoId(long acaoId) {
		return this.multiplosFundamentalistas.stream().filter(mf -> mf.getAcao() != null && mf.getAcao().getId() == acaoId).findFirst().orElse(null);
	}

	public void setMultiplosFundamentalistas(List<MultiplosFundamentalistas> multiplosFundamentalistas) {
		this.multiplosFundamentalistas = multiplosFundamentalistas;
	}
	
	@Override
	public int compareTo(Balanco outroBalanco) {
		if(this.dailyUpdated)
			return 1;
		else if(outroBalanco.isDailyUpdated())
			return -1;
		else		
			return this.data.compareTo(outroBalanco.getData());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Balanco))
			return false;
		Balanco other = (Balanco) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	/*public static class Comparators {
		 public static Comparator<Balanco> NOTA = new Comparator<Balanco>() {
	            @Override
	            public int compare(Balanco b1, Balanco b2) {
	                return b2.nota - b1.nota;
	            }
	        };
	}*/
}
