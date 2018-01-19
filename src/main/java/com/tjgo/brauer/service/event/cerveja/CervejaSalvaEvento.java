package com.tjgo.brauer.service.event.cerveja;

import org.springframework.util.StringUtils;

import com.tjgo.brauer.model.Cerveja;

public class CervejaSalvaEvento {
	
	private Cerveja cerveja;
	
	public CervejaSalvaEvento(Cerveja cerveja) {
		this.cerveja = cerveja;
	}

	public Cerveja getCerveja() {
		return cerveja;
	}
	
	public boolean temFoto(){
		return !StringUtils.isEmpty(cerveja.getFoto());
	}
	
	public boolean isNovaFoto(){
		return cerveja.isNovaFoto();
	}
}
