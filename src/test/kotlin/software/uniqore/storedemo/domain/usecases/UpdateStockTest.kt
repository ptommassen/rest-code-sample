package software.uniqore.storedemo.domain.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import software.uniqore.storedemo.data.repositories.InventoryRepository
import software.uniqore.storedemo.data.repositories.ItemRepository
import software.uniqore.storedemo.data.repositories.StoreRepository
import software.uniqore.storedemo.domain.entities.Inventory
import software.uniqore.storedemo.domain.entities.ItemType
import software.uniqore.storedemo.domain.entities.ItemTypeId
import software.uniqore.storedemo.domain.entities.Store
import software.uniqore.storedemo.domain.entities.StoreId
import software.uniqore.storedemo.domain.exceptions.ItemNotFoundException
import software.uniqore.storedemo.domain.exceptions.OutOfStockException
import software.uniqore.storedemo.domain.exceptions.StoreNotFoundException

@OptIn(ExperimentalCoroutinesApi::class)
internal class UpdateStockTest {
    private val mockStoreRepository = mockk<StoreRepository> {
        coEvery { getStoreById(any()) } returns null
        coEvery { getStoreById(STORE_ID) } returns STORE
    }
    private val mockItemRepository = mockk<ItemRepository> {
        coEvery { getItemTypeById(any()) } returns null
        coEvery { getItemTypeById(ITEM_ID) } returns ITEM
    }
    private val mockInventoryRepository = mockk<InventoryRepository>()
    private val useCase = UpdateStock(mockItemRepository, mockStoreRepository, mockInventoryRepository)

    @Test
    fun `updating the inventory of an nonexistent store should fail`() = runBlockingTest {
        val nonExistentStore = StoreId(23321L)

        val result = useCase(nonExistentStore, ITEM_ID, 4)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(StoreNotFoundException::class.java)
    }

    @Test
    fun `updating the inventory of an nonexistent item should fail`() = runBlockingTest {
        val nonExistentItem = ItemTypeId(23321L)

        val result = useCase(STORE_ID, nonExistentItem, 4)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(ItemNotFoundException::class.java)
    }

    @Test
    fun `updating the inventory with valid input should work`() = runBlockingTest {
        val newAmount = 5
        val updatedLine = Inventory.PerItemType(ITEM, 5, 0)
        coEvery { mockInventoryRepository.updateInventory(STORE, ITEM, newAmount) } returns Result.success(updatedLine)

        val result = useCase(STORE_ID, ITEM_ID, newAmount)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(updatedLine)
    }

    @Test
    fun `updating the inventory so that there are fewer items than reservations should fail`() = runBlockingTest {
        val newAmount = 5
        coEvery { mockInventoryRepository.updateInventory(STORE, ITEM, newAmount) } returns Result.failure(
            OutOfStockException()
        )

        val result = useCase(STORE_ID, ITEM_ID, newAmount)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(OutOfStockException::class.java)
    }

    companion object {
        val ITEM_ID = ItemTypeId(1)
        val ITEM = ItemType(id = ITEM_ID, name = "Kettingzaag")
        val STORE_ID = StoreId(1337)
        val STORE = Store(id = STORE_ID, name = "De Winkel")
    }
}