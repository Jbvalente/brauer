package com.tjgo.brauer.model;

public enum Sabor {
	
	ADOCICADA("Adocicada"),
	AMARGA("Amarga"),
	FORTE("Forte"),
	FRUTADA("Frutada"),
	CONDIMENTADA("Condimentada"),
	SUAVE("Suave");
	
	private String descricao;
	
	Sabor(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao (){
		return descricao;
	}

}
