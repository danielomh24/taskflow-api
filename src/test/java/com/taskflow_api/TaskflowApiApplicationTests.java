package com.taskflow_api;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaskflowApiApplicationTests {

	@BeforeAll
	static void setup() {
		// Carga las variables del .env antes de que Spring levante el contexto de prueba
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);
	}

	@Test
	void contextLoads() {
	}

}
