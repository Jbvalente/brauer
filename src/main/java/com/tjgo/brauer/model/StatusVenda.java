package com.tjgo.brauer.model;

public enum StatusVenda {
	ORCAMENTO("Orcamento"),
	EMITIDA("Emitida"),
	CANCELADA("Cancelada");
	
	private String descricao;

	private StatusVenda(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
}
