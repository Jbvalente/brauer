package com.tjgo.brauer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tjgo.brauer.model.Estilo;
import com.tjgo.brauer.repository.helper.estilo.EstilosQueries;

@Repository
public interface Estilos extends JpaRepository<Estilo, Long>, EstilosQueries {
	
	public Optional<Estilo> findByNomeIgnoreCase(String nome);

}
