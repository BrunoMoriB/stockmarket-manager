package com.bolsavalores.services;

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
import com.bolsavalores.models.exceptions.UsuarioNotFoundException;
import com.bolsavalores.repositories.UsuarioRepository;
import com.bolsavalores.security.Token;
import com.bolsavalores.security.TokenConfig;

@Service
public class TokenService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TokenService.class);
    private static final String APELIDO_CLAIM = "apelido";
    private static final String EMAIL_CLAIM = "email";
    
    @Autowired
    private TokenConfig tokenConfig;
    
    @Autowired
    UsuarioRepository usuarioRepository;
    
    protected JWTVerifier verificadorDeTokens;

    @PostConstruct
    protected void configurar() {
        verificadorDeTokens = JWT.require(getAlgoritmo())
            .withIssuer(tokenConfig.getJwtIssuer())
            .build();
    }

	public Token geraToken(Usuario usuario) throws TokenException {
		try {
	        String hash = JWT.create()
	            .withIssuer(tokenConfig.getJwtIssuer())
	            .withSubject(String.valueOf(usuario.getId()))
	            .withExpiresAt(dataDeExpiracao())
	            .withClaim(APELIDO_CLAIM, usuario.getApelido())
	            .withClaim(EMAIL_CLAIM, usuario.getEmail())
	            .sign(getAlgoritmo());
	        
	        return new Token(hash, usuario);
	    } catch (JWTCreationException e) {
	        LOG.error("Erro ao gerar um token JWT", e);
	        throw new TokenException("Erro ao gerar um token JWT. " + e.getMessage());
	    }
	}
	
	public Token decodificaToken(String token) throws TokenException, UsuarioNotFoundException{
	    try {
            DecodedJWT tokenDecodificado = verificadorDeTokens.verify(token);
            Usuario usuario = usuarioRepository.findByEmail(tokenDecodificado.getClaim(EMAIL_CLAIM).asString());
            
            if(usuario == null)
            	throw new UsuarioNotFoundException();
            
            return new Token(token, usuario);
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
