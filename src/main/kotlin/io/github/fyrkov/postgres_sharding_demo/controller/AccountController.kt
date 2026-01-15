package io.github.fyrkov.postgres_sharding_demo.controller

import io.github.fyrkov.postgres_sharding_demo.domain.Account
import io.github.fyrkov.postgres_sharding_demo.repository.AccountRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val accountRepository: AccountRepository
) {
    @PostMapping
    fun createAccount(): Account {
        val account = Account(
            accountId = UUID.randomUUID()
        )
        return accountRepository.save(account)
    }

    @GetMapping
    fun listAccounts(): List<Account> {
        return accountRepository.findAll()
    }
}
