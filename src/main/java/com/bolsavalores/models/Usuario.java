package com.bolsavalores.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import com.bolsavalores.models.enums.PapelUsuarioEnum;
import com.bolsavalores.models.exceptions.InvalidPapelUsuarioException;

@Entity
@Table(name="usuario")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String apelido;
	@Column(unique = true)
	private String email;
	private String senha;
	private String papel;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getApelido() {
		return apelido;
	}
	public void setApelido(String apelido) {
		this.apelido = apelido;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = getSenhaCriptografada(senha);
	}
	public PapelUsuarioEnum getPapel() {
		return PapelUsuarioEnum.valueOf(papel);
	}
	public void setPapel(String papel) throws InvalidPapelUsuarioException{
		PapelUsuarioEnum papelUsuarioEnum = PapelUsuarioEnum.valueOf(papel);
		
		if(papelUsuarioEnum == null)
			throw new InvalidPapelUsuarioException();
		
		this.papel = papelUsuarioEnum.name();
	}
	
	private String getSenhaCriptografada(String senha) {
		Argon2PasswordEncoder argonEncoder = new Argon2PasswordEncoder(16, 32, 1, 4096, 3);
		return argonEncoder.encode(senha);
	}
}
