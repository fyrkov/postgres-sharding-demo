package io.github.fyrkov.postgres_sharding_demo.repository

import io.github.fyrkov.postgres_sharding_demo.domain.Transaction
import io.github.fyrkov.postgres_sharding_demo.domain.TransactionId
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Repository
class TransactionRepository(
    @Qualifier("dslShard1") private val dsl1: DSLContext,
    @Qualifier("dslShard2") private val dsl2: DSLContext,
) {
    private fun dsl(accountId: UUID): DSLContext {
        val idx = (accountId.hashCode() and Int.MAX_VALUE) % 2
        return if (idx == 0) dsl1 else dsl2
    }

    fun insert(tx: Transaction): Transaction {
        dsl(tx.id.accountId).execute(
            """
      insert into transactions(account_id, tx_id, tx_type, amount)
      values (?, ?, ?, ?)
      """.trimIndent(),
            tx.id.accountId,
            tx.id.txId,
            tx.txType,
            tx.amount,
        )
        return tx
    }

    fun findById(id: TransactionId): Transaction? =
        dsl(id.accountId)
            .fetchOne(
                """
        select account_id, tx_id, tx_type, amount, created_at
        from transactions
        where account_id = ? and tx_id = ?
        """.trimIndent(),
                id.accountId,
                id.txId
            )
            ?.let { r ->
                Transaction(
                    id = TransactionId(
                        accountId = r.get("account_id", UUID::class.java),
                        txId = r.get("tx_id", UUID::class.java),
                    ),
                    txType = r.get("tx_type", String::class.java),
                    amount = r.get("amount", BigDecimal::class.java),
                    createdAt = r.get("created_at", Instant::class.java),
                )
            }

    fun findAllByAccountId(accountId: UUID): List<Transaction> =
        dsl(accountId)
            .fetch(
                """
        select account_id, tx_id, tx_type, amount, created_at
        from transactions
        where account_id = ?
        order by created_at desc, tx_id
        """.trimIndent(),
                accountId
            )
            .map { r ->
                Transaction(
                    id = TransactionId(
                        accountId = r.get("account_id", UUID::class.java),
                        txId = r.get("tx_id", UUID::class.java),
                    ),
                    txType = r.get("tx_type", String::class.java),
                    amount = r.get("amount", BigDecimal::class.java),
                    createdAt = r.get("created_at", Instant::class.java),
                )
            }

    fun deleteById(id: TransactionId): Int =
        dsl(id.accountId).execute(
            "delete from transactions where account_id = ? and tx_id = ?",
            id.accountId,
            id.txId
        )
}