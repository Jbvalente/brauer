package com.tjgo.brauer.storage;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.tjgo.brauer.dto.FotoDTO;

public class FotoStorageRunnable implements Runnable {

	private MultipartFile[] arquivos;
	private DeferredResult<FotoDTO> resultado;
	private FotoStorage fotoStorage;

	public FotoStorageRunnable(MultipartFile[] arquivos, DeferredResult<FotoDTO> resultado, FotoStorage fotoStorage) {
		this.arquivos = arquivos;
		this.resultado = resultado;
		this.fotoStorage = fotoStorage;
	}

	@Override
	public void run() {
		
		System.out.println(">>>> arquivos: "+ arquivos.length+" tamanho: "+arquivos[0].getSize()+"<<<<");
		
		//Salvar a foto no sistema de arquivos...
		String nomeFoto = this.fotoStorage.salvarTemporariamente(arquivos);
		String tipoArquivo = arquivos[0].getContentType();
		resultado.setResult(new FotoDTO(nomeFoto, tipoArquivo));
	}

}
