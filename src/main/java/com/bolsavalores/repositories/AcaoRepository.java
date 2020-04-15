package com.bolsavalores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsavalores.entities.Acao;

public interface AcaoRepository extends JpaRepository<Acao, Long>{
	
	public Acao findById(long id);
	
	@Query("SELECT a FROM Acao a WHERE UPPER(a.nome) = ?1 OR UPPER(a.codigo) = ?2 ") 
	public Acao findByNomeOrCodigo(String nome, String codigo); 
}
  