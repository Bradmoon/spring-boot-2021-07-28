package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.Application;
import com.example.demo.model.User;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes=Application.class, webEnvironment=WebEnvironment.RANDOM_PORT)
public class UserRestControllerIntegrationTest {
	
	@Autowired
	private UserRestController userController;
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Test
	public void retrieveAllUsers() {
		List<User> users = userController.retrieveAllUsers();
		assertEquals(10, users.size());
	}
	@Test
	public void retrieveSingleUser() {
		userController.retrieveSingleUser("3");
		User user = userController.retrieveSingleUser("3");
		assertEquals("Clementine Bauch", user.getName());
	}
	
	@Test
	public void restCall_retrieveUser() {
		ResponseEntity<User> result = testRestTemplate.getForEntity("/retrieveUser/3", User.class);
		User actual = result.getBody();
		assertNotNull(actual);
		assertEquals("Clementine Bauch", actual.getName());
	}
	
	
	@Test
	public void restCall_retrieveAllUsers() {
		@SuppressWarnings("rawtypes")
		ResponseEntity<List> actual = testRestTemplate.getForEntity("/retrieveAllUsers", List.class);
		assertNotNull(actual);
		assertEquals(10, actual.getBody().size());
		System.out.println("***EYECATCHER***" + actual.getBody());
		
	}
	@Test
	public void restCall_retrieveAllUsersExchange() {
		@SuppressWarnings("rawtypes")
		ResponseEntity<List> actual = testRestTemplate.getForEntity("/retrieveAllUsersExchange", List.class);
		assertNotNull(actual);
		assertEquals(10, actual.getBody().size());
		System.out.println("***EYECATCHER***" + actual.getBody());
		
	}
	
}
