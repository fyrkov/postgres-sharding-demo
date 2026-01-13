package io.github.fyrkov.postgres_sharding_demo.repository

import io.github.fyrkov.postgres_sharding_demo.domain.Account
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class AccountRepository(
    @Qualifier("dslShard1") private val dsl1: DSLContext,
    @Qualifier("dslShard2") private val dsl2: DSLContext,
) {
    fun save(account: Account): Account {
        dsl(account.accountId).execute(
            "insert into accounts(account_id) values (?)",
            account
        )
        return account
    }

    fun findById(accountId: UUID): Account? =
        dsl(accountId)
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

    fun deleteById(accountId: UUID): Int =
        dsl(accountId).execute(
            "delete from accounts where account_id = ?",
            accountId
        )

    private fun dsl(accountId: UUID): DSLContext {
        val idx = (accountId.hashCode() and Int.MAX_VALUE) % 2
        return if (idx == 0) dsl1 else dsl2
    }
}
