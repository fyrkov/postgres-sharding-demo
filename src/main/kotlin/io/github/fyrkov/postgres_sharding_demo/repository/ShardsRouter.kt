package io.github.fyrkov.postgres_sharding_demo.repository

import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.*

@Component
class ShardsRouter(
    @Qualifier("dslShard1") private val dsl1: DSLContext,
    @Qualifier("dslShard2") private val dsl2: DSLContext,
) {
    fun getShard(accountId: UUID): DSLContext =
        if (accountId.hashCode() % 2 == 0) dsl1 else dsl2

    fun withShard(accountId: UUID, block: (DSLContext) -> Unit) {
        if (accountId.hashCode() % 2 == 0) block(dsl1) else block(dsl2)
    }
}