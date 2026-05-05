package com.avaran.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BackendApplication.class);
		app.addInitializers(context -> {
			Map<String, Object> properties = buildDatasourceProperties();

			if (!properties.isEmpty()) {
				context.getEnvironment().getPropertySources().addFirst(
					new MapPropertySource("render-database", properties)
				);
			}
		});
		app.run(args);
	}

	private static Map<String, Object> buildDatasourceProperties() {
		Map<String, Object> properties = new HashMap<>();

		String databaseUrl = firstNonBlank(
			System.getenv("SPRING_DATASOURCE_URL"),
			System.getenv("DB_URL"),
			System.getenv("DATABASE_URL")
		);

		if (databaseUrl != null) {
			DatabaseSettings settings = parseDatabaseUrl(databaseUrl);
			properties.put("spring.datasource.url", settings.url());

			if (settings.username() != null) {
				properties.put("spring.datasource.username", settings.username());
			}

			if (settings.password() != null) {
				properties.put("spring.datasource.password", settings.password());
			}
		}

		putIfPresent(properties, "spring.datasource.username", System.getenv("DB_USERNAME"));
		putIfPresent(properties, "spring.datasource.password", System.getenv("DB_PASSWORD"));

		return properties;
	}

	private static DatabaseSettings parseDatabaseUrl(String databaseUrl) {
		if (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://")) {
			URI uri = URI.create(databaseUrl);
			String[] userInfo = uri.getUserInfo() == null ? new String[0] : uri.getUserInfo().split(":", 2);
			String username = userInfo.length > 0 ? userInfo[0] : null;
			String password = userInfo.length > 1 ? userInfo[1] : null;
			int port = uri.getPort() == -1 ? 5432 : uri.getPort();
			String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + port + uri.getPath();

			return new DatabaseSettings(jdbcUrl, username, password);
		}

		return new DatabaseSettings(databaseUrl, null, null);
	}

	private static void putIfPresent(Map<String, Object> properties, String key, String value) {
		if (value != null && !value.isBlank()) {
			properties.put(key, value);
		}
	}

	private static String firstNonBlank(String... values) {
		for (String value : values) {
			if (value != null && !value.isBlank()) {
				return value;
			}
		}

		return null;
	}

	private record DatabaseSettings(String url, String username, String password) {
	}
}
