package com.tjgo.brauer.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tjgo.brauer.model.Cliente;
import com.tjgo.brauer.repository.Clientes;
import com.tjgo.brauer.service.exception.ClienteUtilizadoNaVendaException;
import com.tjgo.brauer.service.exception.CpfCnpjClienteJaCadastradoException;

@Service
public class CadastroClienteService {
	
	@Autowired
	private Clientes clientes;
	
	@Transactional
	public void salvar (Cliente cliente){
		Optional <Cliente> clienteExistente = clientes.findByCpfCnpj(cliente.getCpfCnpjSemFormatacao());
		if (clienteExistente.isPresent()  && cliente.isNovo()){
			throw new CpfCnpjClienteJaCadastradoException("CPF / CNPJ já cadastrado!");
		}
		clientes.save(cliente);
	}

	@Transactional
	public void excluir(Cliente cliente) {
		try {
			clientes.delete(cliente);
			clientes.flush();
		} catch (PersistenceException e) {
			throw new ClienteUtilizadoNaVendaException("O cliente não pode ser excluído, está sendo utilizada pela venda!");
		}
		
	}
}
