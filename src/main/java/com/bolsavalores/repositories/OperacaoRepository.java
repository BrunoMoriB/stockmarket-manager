package com.bolsavalores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsavalores.entities.Operacao;

public interface OperacaoRepository extends JpaRepository<Operacao, Long>{

	public Operacao findById(long id);
}
