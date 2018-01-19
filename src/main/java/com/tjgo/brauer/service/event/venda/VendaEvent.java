package com.tjgo.brauer.service.event.venda;

import com.tjgo.brauer.model.Venda;

public class VendaEvent  {

	private Venda venda;

	public VendaEvent(Venda venda) {
		this.venda = venda;
	}

	public Venda getVenda() {
		return venda;
	}
	
}
