package com.bolsavalores.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.bolsavalores.models.enums.PapelUsuarioEnum;
import com.google.common.collect.ImmutableList;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final RequestMatcher PUBLIC_ACCESS = new OrRequestMatcher(new RequestMatcher[] {
		new AntPathRequestMatcher("/acao/**", String.valueOf(HttpMethod.GET)),
		new AntPathRequestMatcher("/balanco/**", String.valueOf(HttpMethod.GET)),
		new AntPathRequestMatcher("/setor/**", String.valueOf(HttpMethod.GET)),
		new AntPathRequestMatcher("/usuario/**", String.valueOf(HttpMethod.POST))
	});

	@Autowired
	private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private TokenAuthFilter tokenAuthFilter;
	
	@Autowired
	private UserDetailsService userDetailService;
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public Argon2PasswordEncoder getArgon2Encoder() {
		return new Argon2PasswordEncoder(16, 32, 1, 4096, 3);
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().requestMatchers(new RequestMatcher[] {PUBLIC_ACCESS});
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		 http.cors().and().csrf().disable()
		 	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		 	.authorizeRequests().antMatchers(HttpMethod.POST, "/acao/**").hasAuthority(PapelUsuarioEnum.ADMINISTRADOR.name()).and()
		 	.authorizeRequests().antMatchers(HttpMethod.DELETE, "/acao/**").hasAuthority(PapelUsuarioEnum.ADMINISTRADOR.name()).and()
		 	.authorizeRequests().antMatchers(HttpMethod.POST, "/balanco/**").hasAuthority(PapelUsuarioEnum.ADMINISTRADOR.name()).and()
		 	.authorizeRequests().antMatchers(HttpMethod.DELETE, "/balanco/**").hasAuthority(PapelUsuarioEnum.ADMINISTRADOR.name()).and()
		 	.authorizeRequests().antMatchers(HttpMethod.POST, "/setor/**").hasAuthority(PapelUsuarioEnum.ADMINISTRADOR.name()).and()
		 	.authorizeRequests().antMatchers(HttpMethod.DELETE, "/setor/**").hasAuthority(PapelUsuarioEnum.ADMINISTRADOR.name()).and()
			.authorizeRequests().antMatchers(HttpMethod.GET, "/usuario/**").hasAuthority(PapelUsuarioEnum.ADMINISTRADOR.name())
	        .anyRequest().authenticated().and()
	        .httpBasic().authenticationEntryPoint(jwtAuthenticationEntryPoint);
		
		http.addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(getArgon2Encoder());
	}
}
