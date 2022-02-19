package software.uniqore.storedemo.domain.entities

import java.time.LocalDateTime

data class Reservation(
    val customer: Customer,
    val itemType: ItemType,
    val store: Store,
    val expirationTime: LocalDateTime
)
