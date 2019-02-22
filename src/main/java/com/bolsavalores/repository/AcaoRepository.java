package com.bolsavalores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bolsavalores.models.Acao;

public interface AcaoRepository extends JpaRepository<Acao, Long>{
	
	public Acao findById(long id);
}
