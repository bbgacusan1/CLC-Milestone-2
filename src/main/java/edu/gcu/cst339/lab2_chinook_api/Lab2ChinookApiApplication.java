package edu.gcu.cst339.lab2_chinook_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Chinook CRUD API.
 *
 * <p>This Spring Boot application exposes REST endpoints for the tables of the
 * Chinook music-store database (albums, artists, customers, employees, genres,
 * invoices, invoice lines, media types, playlists, playlist tracks and tracks).
 * Each table lives in its own package containing an entity, a DTO, a repository,
 * a service and a controller.</p>
 *
 * <p>Database connection details are not stored in the source code. They are read
 * from the {@code DB_URL}, {@code DB_USERNAME} and {@code DB_PASSWORD} environment
 * variables, and the HTTP port is read from {@code PORT} (default 8080), so the
 * same build can run locally or in the cloud deployment.</p>
 */
@SpringBootApplication
public class Lab2ChinookApiApplication {

	/**
	 * Starts the embedded web server and the Spring application context.
	 *
	 * @param args command-line arguments passed through to Spring Boot
	 */
	public static void main(String[] args) {
		SpringApplication.run(Lab2ChinookApiApplication.class, args);
	}

}
