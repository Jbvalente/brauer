package com.tjgo.brauer.service.event.venda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.tjgo.brauer.model.Cerveja;
import com.tjgo.brauer.model.ItemVenda;
import com.tjgo.brauer.repository.Cervejas;

@Component
public class VendaListener {
	
	@Autowired
	private Cervejas cervejas;
	
	@EventListener
	public void vendaEmitida(VendaEvent vendaEvent){
		vendaEvent.getVenda();
		
		for(ItemVenda item : vendaEvent.getVenda().getItens()) {
			Cerveja cerveja = cervejas.getOne(item.getCerveja().getCodigo());
			cerveja.setQuantidadeEstoque(cerveja.getQuantidadeEstoque() - item.getQuantidade());
			cervejas.save(cerveja);
		}
	}
}
