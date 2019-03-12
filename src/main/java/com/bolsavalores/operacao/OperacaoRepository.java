package com.bolsavalores.operacao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OperacaoRepository extends JpaRepository<Operacao, Long>{

	public Operacao findById(long id);
}
