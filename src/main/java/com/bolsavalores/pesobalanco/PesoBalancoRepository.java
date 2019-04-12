package com.bolsavalores.pesobalanco;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PesoBalancoRepository extends JpaRepository<PesoBalanco, PesoBalancoIdentity>{
	public PesoBalanco findByPesoBalancoIdentity(PesoBalancoIdentity id);
}
