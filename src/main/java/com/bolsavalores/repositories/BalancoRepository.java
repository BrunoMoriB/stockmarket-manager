package com.bolsavalores.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsavalores.models.Balanco;


public interface BalancoRepository extends JpaRepository<Balanco, Long>{

	public Balanco findById(long id);
	
	@Query("SELECT b FROM Balanco b WHERE b.acao.id = ?1 ORDER BY b.data  ")
	public List<Balanco> findByAcaoId(long acaoId);
	
	@Query("SELECT b from Balanco b WHERE b.data = (SELECT MAX(b2.data) FROM Balanco b2 WHERE b2.acao.id = b.acao.id AND b2.dailyUpdated = false) AND b.dailyUpdated = false and b.acao.id = ?1")
	public Balanco findLastBalancoByAcaoId(long acaoId);
	
	@Query("SELECT b FROM Balanco b WHERE b.dailyUpdated = true ") 
	public List<Balanco> findBalancosDailyUpdated();
}
