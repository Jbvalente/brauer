package com.tjgo.brauer.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FotoStorage {
	
	public String salvarTemporariamente(MultipartFile[] arquivos);

	public byte[] recuperarFotoTemporaria(String nome);

	public void salvar(String foto);

	public byte[] recuperar(String foto);

	public byte[] recuperarThumbnail(String fotoCerveja);

	public void excluir(String foto);

}
