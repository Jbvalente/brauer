package com.tjgo.brauer.service.exception;

public class SenhaObrigatoriaUsuarioException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public SenhaObrigatoriaUsuarioException(String mensagem){
		super(mensagem);
	}
}
