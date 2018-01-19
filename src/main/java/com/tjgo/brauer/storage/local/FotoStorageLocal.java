package com.tjgo.brauer.storage.local;

import static java.nio.file.FileSystems.getDefault;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.tjgo.brauer.storage.FotoStorage;

@Component
public class FotoStorageLocal implements FotoStorage {
	
	private static final Logger logger = LoggerFactory.getLogger(FotoStorageLocal.class);
	private static final String THUMBNAIL_PREFIX = "thumbnail.";
	private Path local;
	private Path localTemporario;
	
	public FotoStorageLocal() {
		this(getDefault().getPath(System.getenv("HOME"), ".brauerfotos"));
	}
	
	public FotoStorageLocal (Path path){
		this.local = path;
		criarPastas();
	}

	@Override
	public String salvarTemporariamente(MultipartFile[] arquivos) {
		/*System.out.println(">>>> Salvando a foto temporariamente....<<<<");*/
		String novoNome = null;
		if (arquivos != null && arquivos.length > 0){
			MultipartFile arq = arquivos[0];
			novoNome = renomearArquivo(arq.getOriginalFilename());
			try {
				arq.transferTo(new File(this.localTemporario.toAbsolutePath().toString() + getDefault().getSeparator() + novoNome ));
			} catch ( IOException e) {
				throw new RuntimeException("Erro salvando a foto na pasta temporária!", e);
			} 
		}
		return novoNome;
	}
	
	@Override
	public byte[] recuperarFotoTemporaria(String nome) {
		
		try {
			/*System.out.println(">>>>nome no recuperar: "+this.localTemporario.resolve(nome));*/
			return Files.readAllBytes(this.localTemporario.resolve(nome));
		} catch (IOException e) {
			throw new RuntimeException("Erro lendo a foto temporária! "+this.localTemporario + nome, e);
		}
		
	}
	
	@Override
	public void salvar(String foto) {
		try {
			Files.move(this.localTemporario.resolve(foto), this.local.resolve(foto));
		} catch (IOException e) {
			throw new RuntimeException("Erro movendo a foto para destino final!", e);
		}
		
		try {
			Thumbnails.of(this.local.resolve(foto).toString()).size(40,68).toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		} catch (IOException e) {
			throw new RuntimeException("Erro no redimensionamento da imagem!", e);
		}
	}
	
	@Override
	public byte[] recuperar(String nome) {
		try {
			return Files.readAllBytes(this.local.resolve(nome));
		} catch (IOException e) {
			throw new RuntimeException("Erro lendo a foto! "+this.local + nome, e);
		}
	}
	
	@Override
	public byte[] recuperarThumbnail(String fotoCerveja) {
		return recuperar(THUMBNAIL_PREFIX + fotoCerveja);
	}
	
	@Override
	public void excluir(String foto) {
		try {
			Files.deleteIfExists(this.local.resolve(foto));
			Files.deleteIfExists(this.local.resolve(THUMBNAIL_PREFIX + foto));
		} catch (IOException e) {
			logger.warn(String.format("Erro apagando foto '%s'. Mensage: %s", foto, e.getMessage()));
		}
	}
	
	private void criarPastas() {
		
		try {
			Files.createDirectories(this.local);
			this.localTemporario = getDefault().getPath(this.local.toString(), "temp");
			Files.createDirectories(this.localTemporario);
			
			if (logger.isDebugEnabled()){
				logger.debug("Pastas criadas para salvar foto!");
				logger.debug("Pasta default: "+this.local.toAbsolutePath());
				logger.debug("Pasta temporaria: "+this.localTemporario.toAbsolutePath() );
			}
		} catch (IOException e) {
			throw new RuntimeException("Erro criando pasta para salvar fotos!", e);
		}
	}

	private String renomearArquivo(String nomeOriginal){
		
		String novoNome = UUID.randomUUID().toString()+"_"+nomeOriginal;
		if (logger.isDebugEnabled()){
			logger.debug(String.format("Nome original: %s, novo nome:  %s", nomeOriginal, novoNome));
		}
		return novoNome;
	}

	
	
}
