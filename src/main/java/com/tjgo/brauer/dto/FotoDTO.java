package com.tjgo.brauer.dto;

/*objeto auxiliar responsável por tranfegar informações
objeto especial - DTO - Data Transfer Object*/
public class FotoDTO {

	private String nome;
	private String tipoArquivo;
	
	public FotoDTO(String nome, String tipoArquivo) {
		this.nome = nome;
		this.tipoArquivo = tipoArquivo;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}
	
}
