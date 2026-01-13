package io.github.fyrkov.postgres_sharding_demo

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<PostgresShardingDemoApplication>().with(TestcontainersConfiguration::class).run(*args)
}
