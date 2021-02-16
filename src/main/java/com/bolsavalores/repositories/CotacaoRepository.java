package com.bolsavalores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsavalores.models.Cotacao;

public interface CotacaoRepository extends JpaRepository<Cotacao, Long>{
	
	@Query("SELECT c FROM Cotacao c INNER JOIN c.acao a WHERE a.id = ?1 AND c.ano = ?2 AND c.trimestre = ?3 AND c.dailyUpdated = false")
	public Cotacao findCotacaoByAcaoIdAndAnoAndPeriodo(long acaoId, int ano, int trimestre);
}
