package com.taskflow_api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TaskflowApiApplication {

	public static void main(String[] args) {
		// Cargar .env solo si existe (ignora si no lo encuentra, como en Railway)
		Dotenv.configure()
				.ignoreIfMissing()
				.systemProperties()
				.load();
		SpringApplication.run(TaskflowApiApplication.class, args);
	}

}
