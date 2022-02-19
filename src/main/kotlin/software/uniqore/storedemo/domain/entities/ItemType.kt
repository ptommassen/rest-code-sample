package software.uniqore.storedemo.domain.entities

@JvmInline
value class ItemTypeId(val id: Long)

data class ItemType(val id: ItemTypeId, val name: String)