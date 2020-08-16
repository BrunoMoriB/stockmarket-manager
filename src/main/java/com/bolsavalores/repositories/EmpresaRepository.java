package com.bolsavalores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsavalores.models.Empresa;

public interface EmpresaRepository  extends JpaRepository<Empresa, Long>{
	public Empresa findById(long id);
	
	@Query("SELECT e FROM Empresa e WHERE UPPER(e.nomePregao) = ?1") 
	public Empresa findByNome(String nome); 
 
	@Query("SELECT e FROM Empresa e INNER JOIN e.acoes a WHERE a.id = ?1")
	public Empresa findByAcaoId(long empresaId); 
}
