package io.github.fyrkov.postgres_sharding_demo

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.postgresql.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	companion object {
		private val image = DockerImageName.parse("postgres:17")

		@JvmStatic
		val shard1: PostgreSQLContainer = PostgreSQLContainer(image)
			.withDatabaseName("postgres")
			.withUsername("postgres")
			.withPassword("secret")

		@JvmStatic
		val shard2: PostgreSQLContainer = PostgreSQLContainer(image)
			.withDatabaseName("postgres")
			.withUsername("postgres")
			.withPassword("secret")

		init {
			shard1.start()
			shard2.start()
		}

		@JvmStatic
		@DynamicPropertySource
		fun registerProps(registry: DynamicPropertyRegistry) {
			registry.add("app.shard1.datasource.url") { shard1.jdbcUrl }
			registry.add("app.shard1.datasource.username") { shard1.username }
			registry.add("app.shard1.datasource.password") { shard1.password }

			registry.add("app.shard2.datasource.url") { shard2.jdbcUrl }
			registry.add("app.shard2.datasource.username") { shard2.username }
			registry.add("app.shard2.datasource.password") { shard2.password }
		}
	}
}
