package com.bolsavalores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsavalores.models.Units;

public interface UnitsRepository extends JpaRepository<Units, Long>{

	@Query("SELECT u FROM Units u INNER JOIN u.acao a WHERE UPPER(a.codigo) = UPPER( ?1 )") 
	public Units findByCodigoAcao(String codigo); 
}
