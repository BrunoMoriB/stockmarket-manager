package com.bolsavalores.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bolsavalores.helpers.JsonConverter;
import com.bolsavalores.models.Usuario;
import com.bolsavalores.models.exceptions.StockmarketException;
import com.bolsavalores.repositories.UsuarioRepository;
import com.bolsavalores.security.Token;
import com.bolsavalores.services.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;

@CrossOrigin 
@RestController
@RequestMapping(value="/usuario")
public class UsuarioResource {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioResource.class);
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	JsonConverter jsonConverter;
	
	@PostMapping()
	public ResponseEntity<String> salvaUsuario(@RequestBody Usuario usuario) {
		try {
			usuario = usuarioRepository.save(usuario);
			LOG.info("Usuário criando com sucesso: " + usuario.getEmail());
			return ResponseEntity.ok(jsonConverter.toJson(usuario));
		}catch(JsonProcessingException e) {
			LOG.error("Não foi possível salvar o Usuário. " + e.getMessage());
			return new ResponseEntity<String>("Não foi possível salvar o Usuário. ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/autentica")
	public ResponseEntity<String> autentica(@RequestParam("email") String email, @RequestParam("senha") String senha){
		try {
			Token token = usuarioService.autentica(email, senha);
			LOG.info("Autenticação realizada com sucesso: " + token.getUsuario().getEmail());
			return ResponseEntity.ok(jsonConverter.toJson(token));
		}catch(AuthenticationException e) {
			LOG.error("Não foi possível realizar a Autenticação, email ou senha inválidos! " + e.getMessage());
			return new ResponseEntity<String>("Não foi possível realizar a Autenticação, email ou senha inválidos! ", HttpStatus.BAD_REQUEST);
		} catch (JsonProcessingException | StockmarketException e) {
			LOG.error("Não foi possível realizar a Autenticação. " + e.getMessage());
			return new ResponseEntity<String>("Não foi possível realizar a Autenticação. ", HttpStatus.BAD_REQUEST);
		}
	}
}
