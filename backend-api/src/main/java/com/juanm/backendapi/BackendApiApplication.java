package com.juanm.backendapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class BackendApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner testDatabaseConnection(DataSource dataSource) {
		return args -> {
			try (Connection connection = dataSource.getConnection()) {
				System.out.println("¡CONEXIÓN EXITOSA A MARIADB!");
				System.out.println("Base de datos: " + connection.getCatalog());
				System.out.println("Driver: " + connection.getMetaData().getDriverName());
			} catch (Exception e) {
				System.err.println("ERROR AL CONECTAR A LA BASE DE DATOS: " + e.getMessage());
			}
		};
	}
}
