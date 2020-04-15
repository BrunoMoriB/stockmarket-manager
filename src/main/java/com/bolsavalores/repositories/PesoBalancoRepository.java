package com.bolsavalores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsavalores.entities.PesoBalanco;
import com.bolsavalores.entities.PesoBalancoIdentity;

public interface PesoBalancoRepository extends JpaRepository<PesoBalanco, PesoBalancoIdentity>{
	public PesoBalanco findByPesoBalancoIdentity(PesoBalancoIdentity id);
}
