package com.bolsavalores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsavalores.entities.Ordem;

public interface OrdemRepository extends JpaRepository<Ordem, Long>{

	public Ordem findById(long id);
}
