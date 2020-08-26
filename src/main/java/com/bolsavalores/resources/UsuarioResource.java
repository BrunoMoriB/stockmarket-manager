package com.bolsavalores.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.bolsavalores.helpers.JsonConverter;
import com.bolsavalores.models.Usuario;
import com.bolsavalores.models.enums.PapelUsuarioEnum;
import com.bolsavalores.models.exceptions.StockmarketException;
import com.bolsavalores.repositories.UsuarioRepository;
import com.bolsavalores.security.Token;
import com.bolsavalores.services.UsuarioService;

@RestController
public class UsuarioResource {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioResource.class);
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	JsonConverter jsonConverter;
	
	@PostMapping(Resources.CADASTRAR_USUARIO)
	@ResponseStatus(code = HttpStatus.CREATED)
	public void salvaUsuario(@RequestBody Usuario usuario) throws StockmarketException {
		usuarioService.cadastrarUsuario(usuario);
	}
	
	@PostMapping(Resources.AUTENTICAR_USUARIO)
	public ResponseEntity<AutenticacaoResponse> autentica(@RequestParam("email") String email, @RequestParam("senha") String senha)
			throws StockmarketException {
		Token token = usuarioService.autentica(email, senha);
		Usuario usuario = usuarioService.getUsuarioPeloEmail(email); //TODO: Alterar para não obter mais a senha do usuário
		return ResponseEntity.ok(new AutenticacaoResponse(token.getHash(), usuario.getApelido(), usuario.getPapel()));		
	}

	@Getter
	@AllArgsConstructor
	static class AutenticacaoResponse {
		private String hashToken;
		private String apelidoUsuario;
		private PapelUsuarioEnum papelUsuario;
	}
}
