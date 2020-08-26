package com.bolsavalores.services;

import java.util.Arrays;

import com.bolsavalores.global.Erros;
import com.bolsavalores.models.Usuario;
import com.bolsavalores.models.exceptions.JaExisteException;
import com.bolsavalores.models.exceptions.StockmarketException;
import com.bolsavalores.repositories.UsuarioRepository;
import com.bolsavalores.security.Token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

	private static final Logger LOG = LoggerFactory.getLogger(UsuarioService.class);
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired	
	TokenService tokenService;

	public void cadastrarUsuario(Usuario usuario) throws StockmarketException {
		try {
			usuario = usuarioRepository.save(usuario);
			LOG.info("Usuário criado com sucesso: " + usuario.getEmail());
		} catch (DataIntegrityViolationException ex) {
			if (Erros.ehViolacaoDeRegistroUnico(ex)) {
				LOG.info("Tentativa de cadastro com e-mail já existente " + usuario.getEmail());
				throw new JaExisteException("E-mail já cadastrado");
			}
			LOG.error("Error desconhecido no momento de cadastrar o email " + usuario.getEmail(), ex);		
			throw new StockmarketException();
		}		
	}
	
	public Token autentica(String email, String senha) throws StockmarketException {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, senha));
		Usuario usuario = usuarioRepository.findByEmail(email);
		Token token = tokenService.geraToken(usuario); 
		LOG.info("Autenticação realizada com sucesso: " + email);
		return token;
	}

	public Usuario getUsuarioPeloEmail(String email) {
		Usuario usuario = usuarioRepository.findByEmail(email);
		LOG.info("Usuário obtido pelo email: " + email);
		return usuario;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		if (Validador.formatoEmailEhValido(username)) {
		Usuario usuario = usuarioRepository.findByEmail(username);
		if (usuario == null) {
			LOG.info("Falha ao tentar autenticar o usuário " + username);
			throw new UsernameNotFoundException(username);
		}			
        return new User(usuario.getEmail(), usuario.getSenha(), Arrays.asList(usuario.getPapel()));
//        }
	}
}
