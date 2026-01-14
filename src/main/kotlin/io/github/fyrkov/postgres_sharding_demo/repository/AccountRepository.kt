package io.github.fyrkov.postgres_sharding_demo.repository

import io.github.fyrkov.postgres_sharding_demo.domain.Account
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class AccountRepository(
    private val shardsRouter: ShardsRouter,
) {
    fun save(account: Account): Account {
        shardsRouter.getShard(account.accountId).execute(
            "insert into accounts(account_id) values (?)",
            account.accountId
        )
        return account
    }

    fun findById(accountId: UUID): Account? =
        shardsRouter.getShard(accountId)
            .fetchOne(
                "select account_id, created_at from accounts where account_id = ?",
                accountId
            )
            ?.let { r ->
                Account(
                    accountId = r.get("account_id", UUID::class.java),
                    createdAt = r.get("created_at", Instant::class.java),
                )
            }
}
