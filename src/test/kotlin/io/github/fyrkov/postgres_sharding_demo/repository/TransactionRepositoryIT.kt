package io.github.fyrkov.postgres_sharding_demo.repository

import io.github.fyrkov.postgres_sharding_demo.AbstractIT
import io.github.fyrkov.postgres_sharding_demo.domain.Account
import io.github.fyrkov.postgres_sharding_demo.domain.Transaction
import io.github.fyrkov.postgres_sharding_demo.domain.TransactionId
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.math.BigDecimal
import java.util.*

@SpringBootTest
class TransactionRepositoryIT : AbstractIT() {

    @Autowired
    lateinit var transactionRepository: TransactionRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Test
    fun `should store transactions`() {
        // Given
        val accountId = UUID.randomUUID()
        accountRepository.save(Account(accountId))

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

        // When
        transactionRepository.insert(tx1)
        transactionRepository.insert(tx2)

        // Then
        val allTransactions = transactionRepository.findAllByAccountId(accountId)
        assertTrue(allTransactions.any { it.id == tx1.id })
        assertTrue(allTransactions.any { it.id == tx2.id })
    }
}