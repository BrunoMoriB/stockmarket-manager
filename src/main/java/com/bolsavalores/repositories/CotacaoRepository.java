package com.bolsavalores.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsavalores.models.Cotacao;

public interface CotacaoRepository extends JpaRepository<Cotacao, Long>{
	
	@Query("SELECT c FROM Cotacao c INNER JOIN c.acao a WHERE a.id = ?1 AND c.data = ?2")
	public Cotacao findCotacaoByAcaoIdAndData(long acaoId, LocalDate data);
}