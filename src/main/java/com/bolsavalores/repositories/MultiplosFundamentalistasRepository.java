package com.bolsavalores.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bolsavalores.models.MultiplosFundamentalistas;

public interface MultiplosFundamentalistasRepository  extends JpaRepository<MultiplosFundamentalistas, Long>{

	@Query("SELECT mf FROM MultiplosFundamentalistas mf INNER JOIN mf.balanco b WHERE b.dailyUpdated = true ORDER BY mf.nota DESC")
	public List<MultiplosFundamentalistas> findMultiplosFundamentalistasDailyUpdated();
}
