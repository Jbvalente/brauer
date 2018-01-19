package com.tjgo.brauer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tjgo.brauer.model.Cidade;
import com.tjgo.brauer.model.Estado;
import com.tjgo.brauer.repository.helper.cidade.CidadesQueries;

public interface Cidades extends JpaRepository<Cidade, Long>, CidadesQueries {
	
	public List<Cidade> findByEstadoCodigo(Long codigoEstado);
	
	public Optional<Cidade> findByNomeAndEstado(String nome, Estado estado);
	
}

