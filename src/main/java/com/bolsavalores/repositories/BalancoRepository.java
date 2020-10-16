package com.bolsavalores.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsavalores.models.Balanco;


public interface BalancoRepository extends JpaRepository<Balanco, Long>{

	public Balanco findById(long id);
	
	@Query("SELECT b FROM Balanco b INNER JOIN b.empresa e WHERE e.id = ?1 AND b.dailyUpdated = false ORDER BY b.ano DESC, b.trimestre DESC")
	public List<Balanco> findNotDailyUpdatedByEmpresaId(long empresaId);

	@Query("SELECT b FROM Balanco b INNER JOIN b.empresa e WHERE e.id = ?1 ORDER BY b.ano, b.trimestre")
	public List<Balanco> findByEmpresaId(long empresaId);
	
	@Query("SELECT b FROM Balanco b INNER JOIN b.empresa e INNER JOIN e.acoes a WHERE a.id = ?1 ORDER BY b.ano, b.trimestre")
	public List<Balanco> findByAcaoId(long acaoId);
	
	@Query("SELECT b FROM Balanco b WHERE b.dailyUpdated = true ") 
	public List<Balanco> findBalancosDailyUpdated();
}
