package software.uniqore.storedemo.data.datasource

import java.time.LocalDateTime

/**
 * Interface to the main data source; due to the limited scope of the application we're assuming all the data is coming
 * from the same source, but this can easily be split up if e.g. the inventory data is stored in a different place than
 * the store data.
 *
 * Current assumption is that the actual data source is going to be dumb and can basically only work with a bunch of lists;
 * while it's probably going to be more performant to have e.g. an SQL(-like) database just enumerate the amount of
 * valid reservations on an item when retrieving an inventory, that would immediately limit the choices of backing data source
 * (so my stupid initial in-memory representation would need to become more complicated than I'm willing to spend time on)
 * and move logic out of the Kotlin code which makes it more annoying to test.
 */
interface StoreDemoDataSource {
    suspend fun getStoreById(storeId: Long): StoreData?
    suspend fun getStoreInventory(storeId: Long): List<InventoryLine>
    suspend fun getItemType(itemTypeId: Long): ItemType
    suspend fun getReservationsForItemInStore(itemTypeId: Long, storeId: Long): List<ReservationLine>
}


// TODO: move DTO's to their own package
data class StoreData(val storeId: Long, val name: String)
data class InventoryLine(val itemTypeId: Long, val amount: Int)
data class ItemType(val itemTypeId: Long, val name: String)
data class ReservationLine(
    val itemTypeId: Long,
    val storeId: Long,
    val customer: String,
    val expirationTime: LocalDateTime
)