package software.uniqore.storedemo.data.datasource.memory

import software.uniqore.storedemo.data.datasource.InventoryLine
import software.uniqore.storedemo.data.datasource.ItemType
import software.uniqore.storedemo.data.datasource.ReservationLine
import software.uniqore.storedemo.data.datasource.StoreData
import software.uniqore.storedemo.data.datasource.StoreDemoDataSource
import java.time.LocalDateTime
import kotlin.random.Random

/**
 * In-memory data source that basically tracks everything by maintaining some Lists and Maps;
 * basically only useful to get everything hooked up without having to worry about an actual database yet.
 */
class MemoryDataSource : StoreDemoDataSource {
    private val stores = mutableMapOf<Long, StoreData>()
    private val itemTypes = mutableMapOf<Long, ItemType>()
    private val storeInventories = mutableMapOf<Long, MutableMap<Long, InventoryLine>>()
    private val reservations = mutableMapOf<Pair<Long, Long>, MutableList<ReservationLine>>()

    override suspend fun getStores() = stores.values.toList()

    override suspend fun getStoreById(storeId: Long) = stores[storeId]

    override suspend fun getStoreInventory(storeId: Long) = storeInventories[storeId]?.values?.toList() ?: emptyList()

    override suspend fun getStoreInventoryStock(storeId: Long, itemId: Long): Int =
        storeInventories[storeId]?.let {
            it[itemId]?.amount
        } ?: 0

    override suspend fun putStoreInventoryStock(storeId: Long, itemId: Long, amount: Int) {
        storeInventories[storeId]?.let {
            val line = it[itemId]?.copy(amount = amount) ?: InventoryLine(itemId, amount)
            it.put(itemId, line)
        }
    }


    override suspend fun getItemType(itemTypeId: Long) = itemTypes[itemTypeId]

    override suspend fun createItemType(name: String): ItemType {
        val maxId = itemTypes.keys.maxOfOrNull { it } ?: 0
        val newItemId = maxId + 1
        val itemType = ItemType(newItemId, name)
        itemTypes[newItemId] = itemType
        return itemType
    }

    override suspend fun getReservationsForItemInStore(itemTypeId: Long, storeId: Long) =
        reservations[Pair(itemTypeId, storeId)] ?: emptyList()

    override suspend fun putReservation(
        itemTypeId: Long,
        storeId: Long,
        customer: String,
        expirationTime: LocalDateTime
    ) {
        val key = Pair(itemTypeId, storeId)
        var current = mutableListOf<ReservationLine>()
        current = reservations.putIfAbsent(key, current) ?: current
        current.add(ReservationLine(itemTypeId, storeId, customer, expirationTime))
    }


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
            val map = mutableMapOf<Long, InventoryLine>()
            testItems.forEach { item ->
                map[item.itemTypeId] = InventoryLine(item.itemTypeId, Random.nextInt(1, 5))
            }
            storeInventories[store.storeId] = map
        }
    }

    init {
        loadTestData()
    }
}