package com.greenloop.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the User Service microservice.
 * <p>
 * Manages user profiles, interests, and event history for the GreenLoop
 * platform.
 * Provides RESTful endpoints for user CRUD operations and personalized
 * recommendations.
 * </p>
 *
 * <p>
 * <strong>Service Port:</strong> 8082
 * </p>
 * <p>
 * <strong>Database Schema:</strong> user_service (Supabase PostgreSQL)
 * </p>
 *
 * @author GreenLoop Team
 * @version 1.0
 * @since 2025
 */
@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
