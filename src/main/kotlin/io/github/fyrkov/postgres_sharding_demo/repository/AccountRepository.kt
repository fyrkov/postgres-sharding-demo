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
        shardsRouter.shardFor(account.accountId).execute(
            "insert into accounts(account_id) values (?)",
            account.accountId
        )
        return account
    }

    fun findById(accountId: UUID): Account? =
        shardsRouter.shardFor(accountId)
            .fetchOne(
                "select * from accounts where account_id = ?",
                accountId
            )
            ?.let { r ->
                Account(
                    accountId = r.get("account_id", UUID::class.java),
                    createdAt = r.get("created_at", Instant::class.java),
                )
            }

    fun findAll(): List<Account> =
        shardsRouter.allShards().flatMap { dsl ->
            dsl.fetch("select * from accounts")
                .map { r ->
                    Account(
                        accountId = r.get("account_id", UUID::class.java),
                        createdAt = r.get("created_at", Instant::class.java),
                    )
                }
        }
}
