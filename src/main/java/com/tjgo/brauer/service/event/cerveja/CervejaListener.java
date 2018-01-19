package com.tjgo.brauer.service.event.cerveja;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.tjgo.brauer.storage.FotoStorage;

@Component
public class CervejaListener {
	
	@Autowired
	private FotoStorage fotoStorage;
	
	@EventListener(condition="#evento.temFoto() and #evento.novaFoto")
	public void cervejaSalva(CervejaSalvaEvento evento){
		fotoStorage.salvar(evento.getCerveja().getFoto());
	}
}
