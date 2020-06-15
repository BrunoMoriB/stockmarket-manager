package com.bolsavalores.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsavalores.helpers.JsonConverter;
import com.bolsavalores.models.Usuario;
import com.bolsavalores.repositories.UsuarioRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

@CrossOrigin 
@RestController
@RequestMapping(value="/usuario")
public class UsuarioResource {

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	JsonConverter jsonConverter;
	
	@PostMapping()
	public ResponseEntity<String> salvaUsuario(@RequestBody Usuario usuario) {
		try {
			System.out.println("SENHA: " + usuario.getSenha());
			usuario = usuarioRepository.save(usuario);
			return ResponseEntity.ok(jsonConverter.toJson(usuario));
		}catch(JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<String>("Não foi possível salvar o Usuário. ", HttpStatus.BAD_REQUEST);
		}
	}
}
