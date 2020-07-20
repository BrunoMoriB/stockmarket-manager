package com.bolsavalores.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bolsavalores.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	public Usuario findByEmail(String email);
}
