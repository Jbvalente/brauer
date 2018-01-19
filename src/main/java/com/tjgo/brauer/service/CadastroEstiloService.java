package com.tjgo.brauer.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tjgo.brauer.model.Estilo;
import com.tjgo.brauer.repository.Estilos;
import com.tjgo.brauer.service.exception.EstiloUtilizadoNaCervejaException;
import com.tjgo.brauer.service.exception.NomeEstiloJaCadastradoException;

@Service
public class CadastroEstiloService {
	
	@Autowired
	private Estilos estilos;
	
	
	@Transactional
	public Estilo salvar(Estilo estilo){
		
		Optional<Estilo> estiloOptional = estilos.findByNomeIgnoreCase(estilo.getNome());
		if(estiloOptional.isPresent()){
			throw new NomeEstiloJaCadastradoException("Nome do estilo já cadastrado!");
		}
		
		return estilos.saveAndFlush(estilo);
	}
	
	@Transactional
	public void excluir(Estilo estilo) {
		try {
			estilos.delete(estilo);
			estilos.flush();
		} catch (PersistenceException e) {
			throw new EstiloUtilizadoNaCervejaException("O estilo não pode ser excluído, está sendo utilizado na cerveja!");
		}
	}

}
