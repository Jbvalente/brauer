package com.tjgo.brauer.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tjgo.brauer.model.Cidade;
import com.tjgo.brauer.repository.Cidades;
import com.tjgo.brauer.service.exception.CidadeUtilizadaPeloClienteException;
import com.tjgo.brauer.service.exception.NomeCidadeJaCadastradaException;

@Service
public class CadastroCidadeService {
	
	@Autowired
	private Cidades cidades;
	
	@Transactional
	public void salvar(Cidade cidade){
		
		Optional<Cidade> cidadeExistente = cidades.findByNomeAndEstado(cidade.getNome(), cidade.getEstado());
		if(cidadeExistente.isPresent()){
			throw new NomeCidadeJaCadastradaException("Nome da cidade já cadastrado!");
		}
		cidades.save(cidade);
	}
	
	@Transactional
	public void excluir(Cidade cidade) {
		try {
			cidades.delete(cidade);
			cidades.flush();
		} catch (PersistenceException e) {
			throw new CidadeUtilizadaPeloClienteException("A cidade não pode ser excluída, está sendo utilizada pelo cliente!");
		}
	}

}


