package io.github.fyrkov.postgres_sharding_demo.controller

import io.github.fyrkov.postgres_sharding_demo.AbstractIntegrationTest
import io.github.fyrkov.postgres_sharding_demo.repository.AccountRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class AccountControllerIntegrationTest(
    @Autowired val accountController: AccountController,
    @Autowired val accountRepository: AccountRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `should create account`() {
        val result = accountController.createAccount()

        assertNotNull(result.accountId)
        assertNotNull(accountRepository.findById(result.accountId))
    }

    @Test
    fun `should list accounts`() {
        // Given
        val account = accountController.createAccount()

        // When
        val accounts = accountController.listAccounts()

        // Then
        assertTrue(accounts.isNotEmpty())
        assertTrue(accounts.any { it.accountId == account.accountId })
    }
}
