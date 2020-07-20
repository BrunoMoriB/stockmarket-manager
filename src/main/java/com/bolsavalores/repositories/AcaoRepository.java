package com.bolsavalores.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsavalores.models.Acao;

public interface AcaoRepository extends JpaRepository<Acao, Long>{
	
	public Acao findById(long id);
	
	@Query("SELECT a FROM Acao a INNER JOIN Empresa e WHERE UPPER(e.nomePregao) = ?1 OR UPPER(a.codigo) = ?2") 
	public Acao findByNomeOrCodigo(String nome, String codigo); 
}
  