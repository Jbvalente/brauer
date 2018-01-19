package com.tjgo.brauer.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeracaoDeSenha {
	
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("admin"));
		/*$2a$10$HjRO/oT00.4Q0wpMKjrBW.evxXu6u8h/dcZ1lL5DzxRyZ35WkHIe.*/
	}

}
