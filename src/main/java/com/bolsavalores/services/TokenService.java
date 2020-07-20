package com.bolsavalores.services;

import com.bolsavalores.models.Usuario;
import com.bolsavalores.models.exceptions.TokenException;
import com.bolsavalores.models.exceptions.UsuarioNotFoundException;
import com.bolsavalores.security.Token;

public interface TokenService {
	
	public Token geraToken(Usuario usuario) throws TokenException;
	public Token decodificaToken(String token) throws TokenException, UsuarioNotFoundException;
	
}
