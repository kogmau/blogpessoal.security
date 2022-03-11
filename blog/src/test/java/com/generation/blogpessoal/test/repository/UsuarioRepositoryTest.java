package com.generation.blogpessoal.test.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.blogpessoal.model.UsuarioModel;
import com.generation.blogpessoal.repositorio.UsuarioRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
 
	@BeforeAll

	void start() {

		usuarioRepository.save(new UsuarioModel(0L, "ovo frito", "gema27",
				"https://i.imgur.com/FETvs2O.jpg", "ovo@frito.com"));

		usuarioRepository.save(
				new UsuarioModel(0L, "william marques", "motoca", "https://i.imgur.com/FETvs2O.jpg", "wmg@gmail.com"));

		usuarioRepository.save(new UsuarioModel(0L, "gosth wire", "fantasma",
				"https://i.imgur.com/FETvs2O.jpg", "ghost@wire.com"));
	
	
	}
	@Test
	@DisplayName("Retorna 1 usuário")
	public void deveRetornarUmUsuario() {
		
		Optional<UsuarioModel> usuario = usuarioRepository.findByUsuario("ghost@wire.com");
		assertTrue(usuario.get().getUsuario().equals("ghost@wire.com"));
		}
	
	@Test
	@DisplayName("Retorna 3 usuarios")
	public void deveRetornarTresUsuarios() {
		
		List<UsuarioModel> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("ovo");
		assertEquals (3, listaDeUsuarios.size());
		
		assertTrue(listaDeUsuarios.get(0).getNome().equals("ovo"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("william marques"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("ghost wire"));
	}
	
	@AfterAll
	public void end() {
		usuarioRepository.deleteAll();
	}
	
}
