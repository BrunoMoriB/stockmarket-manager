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

import com.bolsavalores.models.exceptions.TokenException;
import com.bolsavalores.resources.Resources;
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
        if (Resources.ehRecursoPublico(request)) {
            filterChain.doFilter(request, response);
            return;
        }            
		try {
            String tokenHeader = request.getHeader(AUTHORIZATION_HEADER);           
            if (tokenHeader == null || !tokenHeader.startsWith(BEARER_PREFIX))
                throw new TokenException();
            Token token = tokenService.decodificaToken(tokenHeader.replace(BEARER_PREFIX, ""));            
            request.setAttribute(REQUEST_ATTRIBUTE_NAME, token);              
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(token.getIdUsuario(), null,
                Arrays.asList(token.getPapelUsuario())));
            setUsuarioInMDC(token.getIdUsuario());
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
        	LOG.error("Token inválido. ", e.getMessage());
		} finally {            
            MDC.remove(MDC_ID_USUARIO);
        }
	}
	
	private void setUsuarioInMDC(Long idUsuario) {         
        MDC.put(MDC_ID_USUARIO, new StringBuilder(" [id-usuario: ").append(idUsuario).append("]"));
    }

}
