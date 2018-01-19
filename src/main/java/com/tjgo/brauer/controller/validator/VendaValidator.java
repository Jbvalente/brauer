package com.tjgo.brauer.controller.validator;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tjgo.brauer.model.Venda;

@Component
public class VendaValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		return Venda.class.isAssignableFrom(arg0);
	}

	@Override
	public void validate(Object target, Errors erros) {
		ValidationUtils.rejectIfEmpty(erros, "cliente.codigo", "", "Selecione um cliente na pesquisa rápida!");
		
		Venda venda = (Venda) target;
		validarSeInformouApenasHorarioEntrega(erros, venda);
		validarSeInformouItens(erros, venda);
		validarValorTotalNegativo(erros, venda);
	}

	private void validarValorTotalNegativo(Errors erros, Venda venda) {
		if (venda.getValorTotal().compareTo(BigDecimal.ZERO) < 0){
			erros.reject("", "Valor total não pode ser negativo!");
		}
	}

	private void validarSeInformouItens(Errors erros, Venda venda) {
		if(venda.getItens().isEmpty()){
			erros.reject("", "Adicione pelo menos uma cerveja a venda!");
		}
	}

	private void validarSeInformouApenasHorarioEntrega(Errors erros, Venda venda) {
		if(venda.getHorarioEntrega() != null && venda.getDataEntrega() == null){
			erros.rejectValue("dataEntrega", "", "Informe a data de entrega para o horário!");
		}
	}

}
