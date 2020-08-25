package com.bolsavalores.resources;

import javax.servlet.http.HttpServletRequest;

import com.bolsavalores.models.enums.PapelUsuarioEnum;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface Resources {
    String CADASTRAR_USUARIO = "/api/usuarios";
    String AUTENTICAR_USUARIO = "/api/usuarios/autenticar";
    String ACOES = "/api/acao";
    String BALANCOS = "/api/balanco";
    String SETORES = "/api/setor";
    String EMPRESAS = "/api/empresas";

    public static boolean ehRecursoPublico(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return (uri.equals(AUTENTICAR_USUARIO) || uri.equals(CADASTRAR_USUARIO))
            && request.getMethod().equals(HttpMethod.POST.name());
    }

    public static void configurarSegurancaRecursos(HttpSecurity http) throws Exception {
        http.csrf().disable()
			.authorizeRequests()			
			.antMatchers(HttpMethod.POST, CADASTRAR_USUARIO).permitAll()
			.antMatchers(HttpMethod.POST, AUTENTICAR_USUARIO).permitAll()
			.anyRequest().authenticated()
			.antMatchers(HttpMethod.POST, ACOES + "/**").hasAuthority(PapelUsuarioEnum.ADMINISTRADOR.name())
			.antMatchers(HttpMethod.DELETE, ACOES + "/**").hasAuthority(PapelUsuarioEnum.ADMINISTRADOR.name())
			.antMatchers(HttpMethod.POST, BALANCOS + "/**").hasAuthority(PapelUsuarioEnum.ADMINISTRADOR.name())
			.antMatchers(HttpMethod.DELETE, BALANCOS + "/**").hasAuthority(PapelUsuarioEnum.ADMINISTRADOR.name())
			.antMatchers(HttpMethod.POST, SETORES + "/**").hasAuthority(PapelUsuarioEnum.ADMINISTRADOR.name())
			.antMatchers(HttpMethod.DELETE, SETORES + "/**").hasAuthority(PapelUsuarioEnum.ADMINISTRADOR.name());
    }

}