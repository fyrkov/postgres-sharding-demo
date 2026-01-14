package io.github.fyrkov.postgres_sharding_demo.repository

import io.github.fyrkov.postgres_sharding_demo.AbstractIntegrationTest
import io.github.fyrkov.postgres_sharding_demo.domain.Account
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class AccountRepositoryIntegrationTest(
    @Autowired var accountRepository: AccountRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `should store account`() {
        // Given
        val accountId = UUID.randomUUID()
        val account = Account(
            accountId = accountId,
            createdAt = null
        )

        // When
        accountRepository.save(account)

        // Then
        assertNotNull(accountRepository.findById(accountId))
    }
}