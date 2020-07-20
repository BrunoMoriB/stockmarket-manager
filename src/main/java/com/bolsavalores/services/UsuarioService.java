package com.bolsavalores.services;

import org.springframework.security.core.AuthenticationException;

import com.bolsavalores.models.exceptions.StockmarketException;
import com.bolsavalores.security.Token;

public interface UsuarioService {
	public Token autentica(String email, String senha) throws AuthenticationException, StockmarketException ;
}
