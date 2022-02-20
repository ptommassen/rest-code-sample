package software.uniqore.storedemo.domain.usecases

import org.springframework.stereotype.Component
import software.uniqore.storedemo.data.repositories.InventoryRepository
import software.uniqore.storedemo.data.repositories.ItemRepository
import software.uniqore.storedemo.data.repositories.StoreRepository
import software.uniqore.storedemo.domain.entities.Inventory
import software.uniqore.storedemo.domain.entities.ItemTypeId
import software.uniqore.storedemo.domain.entities.StoreId
import software.uniqore.storedemo.domain.exceptions.ItemNotFoundException
import software.uniqore.storedemo.domain.exceptions.StoreNotFoundException

@Component
class UpdateStock(
    private val itemRepository: ItemRepository,
    private val storeRepository: StoreRepository,
    private val inventoryRepository: InventoryRepository
) {

    suspend operator fun invoke(storeId: StoreId, itemId: ItemTypeId, amount: Int): Result<Inventory.PerItemType> {
        val store = storeRepository.getStoreById(storeId) ?: return Result.failure(StoreNotFoundException())
        val item = itemRepository.getItemTypeById(itemId) ?: return Result.failure(ItemNotFoundException())

        return inventoryRepository.updateInventory(store, item, amount)
    }

}