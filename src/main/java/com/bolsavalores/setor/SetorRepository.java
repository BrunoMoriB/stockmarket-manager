package com.bolsavalores.setor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SetorRepository extends JpaRepository<Setor, Long>{

	public Setor findById(long id);
}
