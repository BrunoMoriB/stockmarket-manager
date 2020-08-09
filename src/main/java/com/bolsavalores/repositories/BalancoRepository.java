package com.bolsavalores.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsavalores.models.Balanco;


public interface BalancoRepository extends JpaRepository<Balanco, Long>{

	public Balanco findById(long id);
	
	@Query("SELECT b from Balanco b WHERE b.data = (SELECT MAX(b2.data) FROM Balanco b2 WHERE b2.empresa.id = b.empresa.id AND b2.dailyUpdated = false) AND b.dailyUpdated = false and b.empresa.id = ?1")
	public Balanco findLastBalancoByEmpresaId(long empresaId);

	@Query("select b from Balanco b inner join Empresa e where e.id = ?1 ORDER BY b.data")
	public List<Balanco> findByEmpresaId(long empresaId);
}
