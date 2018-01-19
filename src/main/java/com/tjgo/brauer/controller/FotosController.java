package com.tjgo.brauer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.tjgo.brauer.dto.FotoDTO;
import com.tjgo.brauer.storage.FotoStorage;
import com.tjgo.brauer.storage.FotoStorageRunnable;

@RestController
@RequestMapping("/fotos")
public class FotosController {
	
	@Autowired
	private FotoStorage fotoStorage;
	
	@PostMapping
	public DeferredResult<FotoDTO> upload(@RequestParam ("files[]") MultipartFile [] arquivos){
			DeferredResult<FotoDTO>resultado = new DeferredResult<>();
			
			Thread thread = new Thread(new FotoStorageRunnable(arquivos, resultado, fotoStorage));
			thread.start();
			
			/*System.out.println(">>>> arquivos: "+ arquivos.length+" tamanho: "+arquivos[0].getSize()+"<<<<");*/
			return resultado;
	}
	
	@GetMapping(value="/temp/{nome:.*}")
	public byte[] recuperarFotoTemporaria(@PathVariable String nome){
		/*System.out.println(">>>> nome: " + nome);*/
		return fotoStorage.recuperarFotoTemporaria(nome);
	}
	
	@GetMapping(value="/{nome:.*}")
	public byte[] recuperar(@PathVariable String nome){
		return fotoStorage.recuperar(nome);
	}

}



