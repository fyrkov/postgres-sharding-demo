package io.github.fyrkov.postgres_sharding_demo

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class FlywayConfig {

    @Bean
    fun shard1DataSource(
        @Value("\${postgres.shard1.datasource.url}") url: String,
        @Value("\${postgres.shard1.datasource.username}") username: String,
        @Value("\${postgres.shard1.datasource.password}") password: String,
    ): DataSource = DataSourceBuilder.create().url(url).username(username).password(password).build()

    @Bean
    fun shard2DataSource(
        @Value("\${postgres.shard2.datasource.url}") url: String,
        @Value("\${postgres.shard2.datasource.username}") username: String,
        @Value("\${postgres.shard2.datasource.password}") password: String,
    ): DataSource = DataSourceBuilder.create().url(url).username(username).password(password).build()

    @Bean
    fun migrateShards(
        shard1DataSource: DataSource,
        shard2DataSource: DataSource,
    ) = ApplicationRunner {
        listOf(shard1DataSource, shard2DataSource).forEach { ds ->
            Flyway.configure()
                .dataSource(ds)
                .load()
                .migrate()
        }
    }
}
