package com.tjgo.brauer.repository.helper.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tjgo.brauer.model.Cliente;
import com.tjgo.brauer.repository.filter.ClienteFilter;

public interface ClientesQueries {
	
	public Page<Cliente> filtrar(ClienteFilter filtro, Pageable pageable);

	Cliente buscarComCidadeEstado(Long codigo);
	
}
