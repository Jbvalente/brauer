package com.tjgo.brauer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tjgo.brauer.model.Venda;
import com.tjgo.brauer.repository.helper.venda.VendasQueries;

public interface Vendas extends JpaRepository<Venda, Long>, VendasQueries {

	
	
}
