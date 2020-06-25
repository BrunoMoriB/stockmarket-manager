package com.bolsavalores.services.impl;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bolsavalores.models.Usuario;
import com.bolsavalores.models.exceptions.StockmarketException;
import com.bolsavalores.repositories.UsuarioRepository;
import com.bolsavalores.security.Token;
import com.bolsavalores.services.TokenService;
import com.bolsavalores.services.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService{

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UsuarioRepository UsuarioRepository;
	
	@Autowired	
	TokenService tokenService;
	
	@Override
	public Token autentica(String email, String senha) throws AuthenticationException, StockmarketException {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, senha));
		Usuario usuario = UsuarioRepository.findByEmail(email);
		return tokenService.geraToken(usuario); 
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		if (Validador.formatoEmailEhValido(username)) {
        Usuario usuario = UsuarioRepository.findByEmail(username);
        return new User(usuario.getEmail(), usuario.getSenha(), Collections.emptyList());
//        }
	}

}
