package io.github.fyrkov.postgres_sharding_demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration
import org.springframework.boot.jooq.autoconfigure.JooqAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
	exclude = [
		JooqAutoConfiguration::class // because default Jooq autoconfig assumes a single datasource
	]
)
class PostgresShardingDemoApplication

fun main(args: Array<String>) {
	runApplication<PostgresShardingDemoApplication>(*args)
}
