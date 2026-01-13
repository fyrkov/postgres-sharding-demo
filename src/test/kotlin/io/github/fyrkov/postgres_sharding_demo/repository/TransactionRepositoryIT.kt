package io.github.fyrkov.postgres_sharding_demo.repository

import io.github.fyrkov.postgres_sharding_demo.TestcontainersConfiguration
import io.github.fyrkov.postgres_sharding_demo.domain.Transaction
import io.github.fyrkov.postgres_sharding_demo.domain.TransactionId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.math.BigDecimal
import java.util.*

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class TransactionRepositoryIT {

    @Autowired
    lateinit var transactionRepository: TransactionRepository

    @Test
    fun `should read all transactions`() {
        // Given
        val accountId = UUID.randomUUID()
        val tx1 = Transaction(
            id = TransactionId(accountId, UUID.randomUUID()),
            txType = "DEPOSIT",
            amount = BigDecimal("100.00")
        )
        val tx2 = Transaction(
            id = TransactionId(accountId, UUID.randomUUID()),
            txType = "WITHDRAWAL",
            amount = BigDecimal("50.00")
        )


    }
}