package com.bolsavalores.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsavalores.entities.Balanco;


public interface BalancoRepository extends JpaRepository<Balanco, Long>{

	public Balanco findById(long id);
	
	@Query("SELECT b FROM Balanco b WHERE b.acao.id = ?1 ORDER BY b.data  ")
	public List<Balanco> findByAcaoId(long acaoId);
}
