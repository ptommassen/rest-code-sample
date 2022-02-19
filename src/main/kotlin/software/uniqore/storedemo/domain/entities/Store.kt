package software.uniqore.storedemo.domain.entities

@JvmInline
value class StoreId(val id: Long)

data class Store(val id: StoreId, val name: String)