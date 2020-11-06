package com.bolsavalores.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsavalores.models.Provento;

public interface ProventoRepository  extends JpaRepository<Provento, Long>{

	@Query("SELECT p FROM Provento p WHERE p.acao.id = ?1 ORDER BY p.dataEx DESC ")
	public List<Provento> findByAcaoId(long idAcao);
}
