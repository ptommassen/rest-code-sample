package software.uniqore.storedemo.rest.services

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import software.uniqore.storedemo.domain.entities.ItemTypeId
import software.uniqore.storedemo.domain.entities.StoreId
import software.uniqore.storedemo.domain.usecases.CreateItemType
import software.uniqore.storedemo.domain.usecases.GetStoreInventory
import software.uniqore.storedemo.domain.usecases.UpdateStock
import software.uniqore.storedemo.rest.models.InventoryModel
import software.uniqore.storedemo.rest.models.UpdateInventoryLineModel

@Service
class InventoryService(
    private val getStoreInventory: GetStoreInventory,
    private val updateStock: UpdateStock,
    private val createItemType: CreateItemType
) {

    fun getInventoryForStore(storeId: Long) = runBlocking {
        getStoreInventory(StoreId(storeId)).map { inventory ->
            val lines = inventory.perItemList.map {
                InventoryModel.PerItem(
                    id = it.itemType.id.id,
                    name = it.itemType.name,
                    available = it.available,
                    reserved = it.reserved,
                    total = it.total
                )
            }
            InventoryModel(lines)
        }.getOrThrow()
    }

    fun putInventoryLine(storeId: Long, line: UpdateInventoryLineModel): InventoryModel.PerItem = runBlocking {
        val id = line.id ?: createItemType(line.name!!).id.id
        updateStock(
            storeId = StoreId(storeId),
            itemId = ItemTypeId(id),
            amount = line.total
        ).map { InventoryModel.PerItem(it.itemType.id.id, it.itemType.name, it.available, it.reserved, it.total) }
            .getOrThrow()
    }

}