package com.tjgo.brauer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tjgo.brauer.model.Cerveja;
import com.tjgo.brauer.repository.helper.cerveja.CervejasQueries;

@Repository
public interface Cervejas extends JpaRepository<Cerveja, Long>, CervejasQueries {
		public Optional<Cerveja> findBySkuIgnoreCase (String sku);

		public Optional<Cerveja> findByEstilo(Long codigo);
}
