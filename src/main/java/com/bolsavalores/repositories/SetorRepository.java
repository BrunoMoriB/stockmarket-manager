package com.bolsavalores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsavalores.models.Setor;

public interface SetorRepository extends JpaRepository<Setor, Long>{

	public Setor findById(long id);
}
