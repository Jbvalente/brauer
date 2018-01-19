package com.tjgo.brauer.service.exception;

public class NomeCidadeJaCadastradaException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public NomeCidadeJaCadastradaException(String mensagem){
		super(mensagem);
	}

}
