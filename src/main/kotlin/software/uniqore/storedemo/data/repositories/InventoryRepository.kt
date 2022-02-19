package software.uniqore.storedemo.data.repositories

import software.uniqore.storedemo.data.datasource.StoreDemoDataSource
import software.uniqore.storedemo.domain.entities.Inventory
import software.uniqore.storedemo.domain.entities.ItemType
import software.uniqore.storedemo.domain.entities.ItemTypeId
import software.uniqore.storedemo.domain.entities.Store

class InventoryRepository(private val dataSource: StoreDemoDataSource, private val clockRepository: ClockRepository) {

    suspend fun getInventoryForStore(store: Store): Inventory {
        val inventoryLines = dataSource.getStoreInventory(store.id.id)
        val currentTime = clockRepository.getCurrentDateTime()
        val result = Inventory(
            store = store,
            perItemList = inventoryLines.map { line ->
                // TODO: for the current data-implementation it doesn't matter, but when using an actual DB we can greatly improve performance by
                // extracting these data source queries and batching them
                val itemType =
                    dataSource.getItemType(line.itemTypeId)?.let { ItemType(ItemTypeId(it.itemTypeId), it.name) }
                        ?: ItemType(ItemTypeId(line.itemTypeId), "UNKNOWN")
                val reservations = dataSource.getReservationsForItemInStore(line.itemTypeId, store.id.id)
                val validReservationCount =
                    reservations.count { reservation -> reservation.expirationTime.isAfter(currentTime) }
                Inventory.PerItemType(
                    itemType = itemType,
                    available = line.amount - validReservationCount,
                    reserved = validReservationCount
                )
            }
        )
        return result
    }

}