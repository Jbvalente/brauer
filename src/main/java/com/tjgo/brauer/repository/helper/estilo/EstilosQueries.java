package com.tjgo.brauer.repository.helper.estilo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tjgo.brauer.model.Estilo;
import com.tjgo.brauer.repository.filter.EstiloFilter;

public interface EstilosQueries {

	public Page<Estilo> filtrar (EstiloFilter filtro, Pageable pageable);
	
}
