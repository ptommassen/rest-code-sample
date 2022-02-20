package software.uniqore.storedemo.domain.usecases

import org.springframework.stereotype.Component
import software.uniqore.storedemo.data.repositories.InventoryRepository
import software.uniqore.storedemo.data.repositories.StoreRepository
import software.uniqore.storedemo.domain.entities.Inventory
import software.uniqore.storedemo.domain.entities.StoreId

class StoreNotFoundException : Exception()

@Component
class GetStoreInventory(
    private val storeRepository: StoreRepository,
    private val inventoryRepository: InventoryRepository
) {

    suspend operator fun invoke(storeId: StoreId): Result<Inventory> {
        val store = storeRepository.getStoreById(storeId) ?: return Result.failure(StoreNotFoundException())
        val inventory = inventoryRepository.getInventoryForStore(store)
        return Result.success(inventory)
    }
}