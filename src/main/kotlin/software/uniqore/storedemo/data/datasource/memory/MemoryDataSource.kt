package software.uniqore.storedemo.data.datasource.memory

import software.uniqore.storedemo.data.datasource.InventoryLine
import software.uniqore.storedemo.data.datasource.ItemType
import software.uniqore.storedemo.data.datasource.ReservationLine
import software.uniqore.storedemo.data.datasource.StoreData
import software.uniqore.storedemo.data.datasource.StoreDemoDataSource
import kotlin.random.Random

/**
 * In-memory data source that basically tracks everything by maintaining some Lists and Maps;
 * basically only useful to get everything hooked up without having to worry about an actual database yet.
 */
class MemoryDataSource : StoreDemoDataSource {
    private val stores = mutableMapOf<Long, StoreData>()
    private val itemTypes = mutableMapOf<Long, ItemType>()
    private val storeInventories = mutableMapOf<Long, List<InventoryLine>>()
    private val reservations = mutableMapOf<Pair<Long, Long>, List<ReservationLine>>()

    override suspend fun getStores() = stores.values.toList()

    override suspend fun getStoreById(storeId: Long) = stores[storeId]

    override suspend fun getStoreInventory(storeId: Long) = storeInventories[storeId] ?: emptyList()

    override suspend fun getItemType(itemTypeId: Long) = itemTypes[itemTypeId]

    override suspend fun getReservationsForItemInStore(itemTypeId: Long, storeId: Long) =
        reservations[Pair(itemTypeId, storeId)] ?: emptyList()


    private val testStores = listOf(
        StoreData(1, "Dagobert's IJzerwaren Franchise"),
        StoreData(2, "Goudglans' Schroefjesgigant")
    )

    private val testItems = listOf(
        ItemType(1, "Hamers"),
        ItemType(2, "Zagen"),
        ItemType(3, "Waterpomptangen"),
    )


    private fun loadTestData() {
        testStores.forEach {
            stores[it.storeId] = it
        }

        testItems.forEach {
            itemTypes[it.itemTypeId] = it
        }

        testStores.forEach { store ->
            val lines = testItems.map { item ->
                InventoryLine(item.itemTypeId, Random.nextInt(1, 5))
            }
            storeInventories[store.storeId] = lines
        }
    }

    init {
        loadTestData()
    }
}