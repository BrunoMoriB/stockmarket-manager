package com.bolsavalores.security;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bolsavalores.models.Usuario;
import com.bolsavalores.models.exceptions.TokenException;
import com.bolsavalores.models.exceptions.UsuarioNotFoundException;
import com.bolsavalores.services.TokenService;

@Component
public class TokenAuthFilter extends OncePerRequestFilter {

	 private static final Logger LOG = LoggerFactory.getLogger(TokenAuthFilter.class);
	
	private static final String BEARER_PREFIX  = "Bearer ";
	private static final String MDC_ID_USUARIO = "id-usuario";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REQUEST_ATTRIBUTE_NAME = "token";
    
    @Autowired
    TokenService tokenService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
            String tokenHeader = request.getHeader(AUTHORIZATION_HEADER);
           
            if (tokenHeader == null || !tokenHeader.startsWith(BEARER_PREFIX))
                throw new TokenException();
             
            Token token = tokenService.decodificaToken(tokenHeader.replace(BEARER_PREFIX, ""));
            Usuario usuario = token.getUsuario();
            request.setAttribute(REQUEST_ATTRIBUTE_NAME, token);              
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha(), Arrays.asList(new Permissao(usuario.getPapel().name()))));
            setUsuarioInMDC(usuario.getId());
        } catch (TokenException e) {
        	LOG.error("Token inválido. ", e.getMessage());
			return;
        } catch (UsuarioNotFoundException e) {
        	LOG.error("Usuário não localizado. ", e.getMessage());
			return;
		} finally {
            filterChain.doFilter(request, response);
            MDC.remove(MDC_ID_USUARIO);
        }
	}
	
	private void setUsuarioInMDC(Long idUsuario) {         
        MDC.put(MDC_ID_USUARIO, new StringBuilder(" [id-usuario: ").append(idUsuario).append("]"));
    }

}
