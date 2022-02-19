package software.uniqore.storedemo.domain.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import software.uniqore.storedemo.data.repositories.InventoryRepository
import software.uniqore.storedemo.data.repositories.StoreRepository
import software.uniqore.storedemo.domain.entities.Inventory
import software.uniqore.storedemo.domain.entities.Store
import software.uniqore.storedemo.domain.entities.StoreId

internal class GetStoreInventoryTest {
    private val mockStoreRepository = mockk<StoreRepository>()
    private val mockInventoryRepository = mockk<InventoryRepository>()
    private val getStoreInventory = GetStoreInventory(
        storeRepository = mockStoreRepository,
        inventoryRepository = mockInventoryRepository
    )

    @Test
    fun `getting the inventory of an existing store should return said store's inventory`() {
        val storeId: StoreId = 1
        val store = Store(storeId, "Test Store")
        val inventory = Inventory(store, emptyList())

        every { mockStoreRepository.getStoreById(storeId) } returns store
        every { mockInventoryRepository.getInventoryForStore(store) } returns inventory

        val result = getStoreInventory(storeId)

        assertTrue(result.isSuccess)
        assertThat(result.getOrNull()).isEqualTo(inventory)
    }

    @Test
    fun `trying to get the inventory of a non-existing store should return a failure`() {
        val storeId: StoreId = 1
        every { mockStoreRepository.getStoreById(storeId) } returns null

        val result = getStoreInventory(storeId)

        assertTrue(result.isFailure)
        assertThat(result.exceptionOrNull()).isInstanceOf(StoreNotFoundException::class.java)
    }
}