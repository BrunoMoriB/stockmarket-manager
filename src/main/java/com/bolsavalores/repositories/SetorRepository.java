package com.bolsavalores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsavalores.entities.Setor;

public interface SetorRepository extends JpaRepository<Setor, Long>{

	public Setor findById(long id);
}
