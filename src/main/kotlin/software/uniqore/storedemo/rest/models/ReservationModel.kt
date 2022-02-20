package software.uniqore.storedemo.rest.models

data class ReservationModel(
    val storeId: Long,
    val itemTypeId: Long,
    val customerName: String
)