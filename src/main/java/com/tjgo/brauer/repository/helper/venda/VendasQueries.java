package com.tjgo.brauer.repository.helper.venda;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tjgo.brauer.dto.VendaMes;
import com.tjgo.brauer.dto.VendaOrigem;
import com.tjgo.brauer.model.Venda;
import com.tjgo.brauer.repository.filter.VendaFilter;

public interface VendasQueries {
	
	public Page<Venda> filtrar(VendaFilter filtro, Pageable pageable);
	
	public Venda buscarComItens(Long codigo);
	
	public BigDecimal valorTotalAno();
	public BigDecimal valorTotalMes();
	public BigDecimal valorTicketMedioAno();
	
	public List<VendaMes> totalPorMes();
	public List<VendaOrigem> totalPorOrigem();
	
}
