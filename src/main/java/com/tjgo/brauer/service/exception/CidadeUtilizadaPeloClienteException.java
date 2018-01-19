package com.tjgo.brauer.service.exception;

public class CidadeUtilizadaPeloClienteException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public CidadeUtilizadaPeloClienteException(String mensagem) {
		super(mensagem);
	}
}
