package software.uniqore.storedemo.rest.services

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import software.uniqore.storedemo.domain.entities.StoreId
import software.uniqore.storedemo.domain.usecases.GetStoreInventory
import software.uniqore.storedemo.rest.models.InventoryModel

@Service
class InventoryService(private val getStoreInventory: GetStoreInventory) {

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

}