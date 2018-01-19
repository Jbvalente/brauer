package com.tjgo.brauer.service;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tjgo.brauer.model.Cerveja;
import com.tjgo.brauer.repository.Cervejas;
import com.tjgo.brauer.service.event.cerveja.CervejaSalvaEvento;
import com.tjgo.brauer.service.exception.ImpossivelExcluirEntidadeException;
import com.tjgo.brauer.storage.FotoStorage;

@Service
public class CadastroCervejaService {

	@Autowired
	private Cervejas cervejas;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private FotoStorage fotoStorage;
	
	@Transactional
	public void salvar(Cerveja cerveja){
		cervejas.save(cerveja);
		
		publisher.publishEvent(new CervejaSalvaEvento(cerveja));
	}
	
	@Transactional
	public void excluir(Cerveja cerveja){
		
		try {
			String foto = cerveja.getFoto();
			cervejas.delete(cerveja);
			cervejas.flush();
			fotoStorage.excluir(foto);
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidadeException ("Impossível apagar cerveja. Já foi usada em uma venda.");
		}
	}
	
}
