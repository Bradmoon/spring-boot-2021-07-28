package com.example.demo.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.Application;

@ExtendWith({ RestDocumentationExtension.class, SpringExtension.class })
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApiDocTest {

	private MockMvc mockMvc;

	private OperationRequestPreprocessor prettyRequest = Preprocessors.preprocessRequest(Preprocessors.prettyPrint());
	private OperationResponsePreprocessor prettyResponse = Preprocessors.preprocessResponse(Preprocessors.prettyPrint());

	@BeforeEach
	public void setUpMockMvc(WebApplicationContext webApplicationContext,
			RestDocumentationContextProvider restDocumentation) {
		
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.alwaysDo(document("{method-name}", prettyRequest, prettyResponse))
				.apply(documentationConfiguration(restDocumentation)
						.uris().withScheme("http").withHost("localhost").withPort(8080))
				.build();
	}
	
	@Test
	public void retrieveAllUsers() throws Exception {
		this.mockMvc.perform(get("/retrieveAllUsers")).andExpect(status().isOk());
	}

	@Test
	public void retrieveUser() throws Exception {
		
		ParameterDescriptor idPathVaribale = RequestDocumentation.parameterWithName("id").description("the users id key");
		PathParametersSnippet pathParameters = RequestDocumentation.pathParameters(idPathVaribale);
		
		this.mockMvc.perform(RestDocumentationRequestBuilders.get("/retrieveUser/{id}", 3))
			.andExpect(status().isOk())
			.andDo(document("{method-name}", prettyRequest, prettyResponse, pathParameters));
	}

	@Test
	public void actuator() throws Exception {
		this.mockMvc.perform(get("/actuator")).andExpect(status().isOk());
	}
	
	@Test
	public void actuatorBeans() throws Exception {
		this.mockMvc.perform(get("/actuator/beans")).andExpect(status().isOk());
	}
	
}
