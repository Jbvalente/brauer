package com.tjgo.brauer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tjgo.brauer.model.Cliente;
import com.tjgo.brauer.repository.helper.cliente.ClientesQueries;

public interface Clientes extends JpaRepository<Cliente, Long>, ClientesQueries {

	public Optional<Cliente> findByCpfCnpj(String cpfCnpj);

	public List<Cliente> findByNomeStartingWithIgnoreCase(String nome);

}
