package com.bolsavalores.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bolsavalores.models.Usuario;
import com.bolsavalores.models.exceptions.TokenException;
import com.bolsavalores.security.Token;
import com.bolsavalores.security.TokenConfig;
import com.bolsavalores.services.TokenService;

@Service
public class TokenServiceImpl implements TokenService{
	
    private static final Logger LOG = LoggerFactory.getLogger(TokenServiceImpl.class);
    private static final String EMAIL_CLAIM = "email";
    
    @Autowired
    private TokenConfig tokenConfig;
    
    protected JWTVerifier verificadorDeTokens;

    @PostConstruct
    protected void configurar() {
        verificadorDeTokens = JWT.require(getAlgoritmo())
            .withIssuer(tokenConfig.getJwtIssuer())
            .build();
    }

	@Override
	public Token geraToken(Usuario usuario) throws TokenException {
		try {
	        String hash = JWT.create()
	            .withIssuer(tokenConfig.getJwtIssuer())
	            .withSubject(String.valueOf(usuario.getId()))
	            .withExpiresAt(dataDeExpiracao())
	            .withClaim(EMAIL_CLAIM, usuario.getEmail())
	            .sign(getAlgoritmo());
	        
	        return new Token(hash, usuario.getEmail(), usuario.getId());
	    } catch (JWTCreationException e) {
	        LOG.error("Erro ao gerar um token JWT", e);
	        throw new TokenException("Erro ao gerar um token JWT. " + e.getMessage());
	    }
	}
	
	@Override
	public Token decodificaToken(String token) throws TokenException {
	    try {
            DecodedJWT tokenDecodificado = verificadorDeTokens.verify(token);
            return new Token(token, tokenDecodificado.getClaim(EMAIL_CLAIM).asString(), Long.valueOf(tokenDecodificado.getSubject()));
        } catch (JWTVerificationException exception){
            throw new TokenException();
        }
	}
	
	 private Date dataDeExpiracao() {
        return Date.from(LocalDateTime.now().plusDays(tokenConfig.getDiasDeVida()).atZone(ZoneId.systemDefault()).toInstant());
    }
	 
    private Algorithm getAlgoritmo() {
        return Algorithm.HMAC256(tokenConfig.getJwtSegredo());
    }
}
