package com.generation.blogpessoal.test.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.UsuarioModel;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;
	
	@Test
	@Order(1)
	@DisplayName("Cadastrar Um Usuário")
	public void deveCriarUmUsuario() {
	
		HttpEntity<UsuarioModel> corpoRequisicao = new HttpEntity<UsuarioModel>(new UsuarioModel());
		
		ResponseEntity<UsuarioModel> corpoResposta = testRestTemplate
				.exchange("/usuario/cadastrar", HttpMethod.POST, corpoRequisicao, UsuarioModel.class);
		
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
		assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
		assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
		
		
		}
	
	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação do Usuário")
	public void naoDeveDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new UsuarioModel(0L, 
				"laercio Alves", "laercio@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));
	
	
	HttpEntity<UsuarioModel> corpoRequisicao = new HttpEntity<UsuarioModel>(new UsuarioModel(0L, 
			"viktor kino", "kino@email.com.br", "13465278", "https://i.imgur.com/T12NIp9.jpg"));
	
	ResponseEntity<UsuarioModel> corpoResposta = testRestTemplate
			.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, UsuarioModel.class);
	
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
	
		}
	@Test
	@Order(3)
	@DisplayName("Atualizar um Usuário")
	public void deveAtualizarUmUsuario() {
		
		Optional<UsuarioModel> usuarioCadastrado = usuarioService.cadastrarUsuario(new UsuarioModel(0L, 
				"Gabriella Felix", "gabriellafelix@email.com.br", "gabriella123", "https://i.imgur.com/yDRVeK7.jpg"));
		
		UsuarioModel usuarioUpdate = new UsuarioModel(usuarioCadastrado.get().getId(), 
				"Gabriella felix", "gabriellafelix@email.com.br", "gabriella123" , "https://i.imgur.com/yDRVeK7.jpg");
	

	HttpEntity<UsuarioModel> corpoRequisicao = new HttpEntity<UsuarioModel>(usuarioUpdate);

	ResponseEntity<UsuarioModel> corpoResposta = testRestTemplate
			.withBasicAuth("root", "root")
			.exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, UsuarioModel.class);
	
	assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
	assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
	assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
	
	}
	
	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuários")
	public void deveMostrarTodosUsuarios() {
		
		usuarioService.cadastrarUsuario(new UsuarioModel(0L, 
				"Francisca Ferreira", "francisca_ferreira@email.com.br", "francisca123", "https://i.imgur.com/5M2p5Wb.jpg"));
			
			usuarioService.cadastrarUsuario(new UsuarioModel(0L, 
				"Felipe Marques", "felipe_marques@email.com.br", "felipe123", "https://i.imgur.com/Sk5SjWE.jpg"));
	
	
			ResponseEntity<String> resposta = testRestTemplate
			.withBasicAuth("root", "root")
			.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
			
			assertEquals(HttpStatus.OK, resposta.getStatusCode());

	}
	
	@Test
	@Order(5)
	@DisplayName("Listar Um Usuário Específico")
	public void deveListarApenasUmUsuario() {
		
		Optional<UsuarioModel> usuarioBusca = usuarioService.cadastrarUsuario(new UsuarioModel(0L, 
				"Felipe Marques", "felipe_marques@email.com.br", "felipe123", "https://i.imgur.com/Sk5SjWE.jpg"));
	
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/" + usuarioBusca.get().getId(), HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
	}
}
