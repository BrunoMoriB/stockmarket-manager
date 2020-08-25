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
import com.bolsavalores.models.enums.PapelUsuarioEnum;
import com.bolsavalores.models.exceptions.StockmarketException;
import com.bolsavalores.models.exceptions.TokenException;
import com.bolsavalores.repositories.UsuarioRepository;
import com.bolsavalores.security.Token;
import com.bolsavalores.security.TokenConfig;

@Service
public class TokenService {
	
	private static final Logger LOG = LoggerFactory.getLogger(TokenService.class);
    private static final String PAPEL_USUARIO_CLAIM = "papelUsuario";
    
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

	public Token geraToken(Usuario usuario) throws StockmarketException {
		try {
	        String hash = JWT.create()
	            .withIssuer(tokenConfig.getJwtIssuer())
	            .withSubject(String.valueOf(usuario.getId()))
	            .withExpiresAt(dataDeExpiracao())
	            .withClaim(PAPEL_USUARIO_CLAIM, usuario.getPapel().name())
	            .sign(getAlgoritmo());
	        
	        return new Token(hash, usuario.getId(), usuario.getPapel());
	    } catch (JWTCreationException e) {
	        LOG.error("Erro ao gerar um token JWT", e);
	        throw new StockmarketException();
	    }
	}
	
	public Token decodificaToken(String tokenHash) throws TokenException {
	    try {
            DecodedJWT tokenDecodificado = verificadorDeTokens.verify(tokenHash);
            return new Token(tokenHash, Long.valueOf(tokenDecodificado.getSubject()), PapelUsuarioEnum.valueOf(tokenDecodificado.getClaim(PAPEL_USUARIO_CLAIM).asString()));
        } catch (JWTVerificationException exception) {
            LOG.error("Erro ao decodificar o token", exception);
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
