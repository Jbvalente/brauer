package com.tjgo.brauer.repository.helper.cidade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tjgo.brauer.model.Cidade;
import com.tjgo.brauer.repository.filter.CidadeFilter;

public interface CidadesQueries {
	
	public Page<Cidade> filtrar(CidadeFilter filtro, Pageable pageable);
	
	public Cidade buscarComEstado(Long codigo);
	
}
