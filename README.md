# Demo project for Postgres Sharding

This repository is a small Postgres + Spring Boot demo of “manual sharding” setup for PostgreSQL.

## What this POC demonstrates

- 2 independent PostgreSQL databases (“shards”)
- same schema is deployed to both shards via Flyway
- application-level routing: the app selects shard1 vs shard2 based on the shard key

## Data model

- `accounts(account_id uuid primary key, created_at ...)`
- `transactions(account_id uuid, tx_id uuid, ...)` have composite PK: `account_id, tx_id` and belongs to an `account` (same shard)

The shard key is `account_id`, so every transaction lookup within the same account is single-shard.

## Routing

A deterministic function `account_id -> shard_index` is used to route queries to shards.
For 2 shards selection: even/odd based on UUID hash

## What is not in scope of this POC

This is intentionally out of scope for the POC:

- cross-shard queries / joins
- distributed multi-shard transactions
- resharding / moving data between shards
- global uniqueness constraints across shards
- high availability (replicas / Patroni)

## Other sharding options (beyond this POC)

### 1) Shard map (lookup table)
Instead of `hash(account_id) % N`, store:

`account_id -> shard_id`

Pros:
- resharding possible (move one account at a time)

Cons:
- extra lookup (usually solved with cache)

### 2) Consistent hashing
Avoids shard map lookup and minimizes reshuffling when adding/removing shards.

Pros:
- no DB lookup
- fewer keys move on shard count change

Cons:
- more implementation complexity than modulo

### 3) Citus (distributed Postgres)
Citus is a Postgres extension that provides coordinator + worker nodes and routes data automatically.

Pros:
- “one logical database” feel
- automatic routing

Cons:
- operational complexity
- not available on AWS RDS/Aurora Postgres by default


## How to run locally

### Dependencies
* JDK >= 21
* Docker

Start DBs:
```bash
docker compose up -d
```
Fixed ports 15433, 15434 are used which must be available! 

Start the app:
```
./gradlew bootRun
```
