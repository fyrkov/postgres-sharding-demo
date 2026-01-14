package io.github.fyrkov.postgres_sharding_demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration
import org.springframework.boot.jooq.autoconfigure.JooqAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
	exclude = [
		JooqAutoConfiguration::class,
		FlywayAutoConfiguration::class,
		DataSourceAutoConfiguration::class,
	]
)
class PostgresShardingDemoApplication

fun main(args: Array<String>) {
	runApplication<PostgresShardingDemoApplication>(*args)
}
