package com.tjgo.brauer.repository.helper.cerveja;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tjgo.brauer.dto.CervejaDTO;
import com.tjgo.brauer.dto.ValorItensEstoque;
import com.tjgo.brauer.model.Cerveja;
import com.tjgo.brauer.repository.filter.CervejaFilter;

public interface CervejasQueries {

	public Page<Cerveja> filtrar(CervejaFilter filtro, Pageable pageable);
	
	public List<CervejaDTO> porSkuOuNome(String skuOuNome);
	
	public ValorItensEstoque valorItensEstoque();
	
}