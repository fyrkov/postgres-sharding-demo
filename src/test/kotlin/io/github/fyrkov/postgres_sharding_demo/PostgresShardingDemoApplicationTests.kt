package io.github.fyrkov.postgres_sharding_demo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class PostgresShardingDemoApplicationTests {

	@Test
	fun contextLoads() {
	}

}
