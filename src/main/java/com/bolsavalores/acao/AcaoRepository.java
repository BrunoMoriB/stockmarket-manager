package com.bolsavalores.acao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AcaoRepository extends JpaRepository<Acao, Long>{
	
	public Acao findById(long id);
}
