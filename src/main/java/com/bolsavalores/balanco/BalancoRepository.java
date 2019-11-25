package com.bolsavalores.balanco;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BalancoRepository extends JpaRepository<Balanco, Long>{

	public Balanco findById(long id);
	
	@Query("SELECT b FROM Balanco b WHERE b.acao.id = ?1 ")
	public List<Balanco> findByAcaoId(long acaoId);
}
