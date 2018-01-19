package com.tjgo.brauer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tjgo.brauer.model.Usuario;
import com.tjgo.brauer.repository.helper.usuario.UsuariosQueries;

public interface Usuarios extends JpaRepository <Usuario, Long>, UsuariosQueries {

	public Optional<Usuario> findByEmail(String email);

	public List<Usuario> findByCodigoIn(Long[] codigos);
	
}
