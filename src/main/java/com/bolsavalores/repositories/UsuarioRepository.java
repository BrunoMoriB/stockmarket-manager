package com.bolsavalores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsavalores.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	//TODO: Add a @cacheable 
	public Usuario findByEmail(String email);
}
