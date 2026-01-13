package io.github.fyrkov.postgres_sharding_demo.domain

import java.time.Instant
import java.util.UUID

data class Account(
    val accountId: UUID,
    val createdAt: Instant? = null,
)