package io.github.fyrkov.postgres_sharding_demo.repository

import io.github.fyrkov.postgres_sharding_demo.domain.Transaction
import io.github.fyrkov.postgres_sharding_demo.domain.TransactionId
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Repository
class TransactionRepository(
    private val shardsRouter: ShardsRouter,
) {
    fun insert(tx: Transaction): Transaction {
        shardsRouter.dslFor(tx.id.accountId).execute(
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

    fun findAllByAccountId(accountId: UUID): List<Transaction> =
        shardsRouter.dslFor(accountId)
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

    fun findById(id: TransactionId): Transaction? = findById(id.accountId, id.txId)

    fun findById(accountId: UUID, txId: UUID): Transaction? =
        shardsRouter.dslFor(accountId)
            .fetchOne(
                """
                select account_id, tx_id, tx_type, amount, created_at
                from transactions
                where account_id = ? and tx_id = ?
                """.trimIndent(),
                accountId,
                txId
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

}