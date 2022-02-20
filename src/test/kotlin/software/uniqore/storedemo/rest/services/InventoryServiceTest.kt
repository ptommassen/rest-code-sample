package software.uniqore.storedemo.rest.services

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import software.uniqore.storedemo.domain.entities.Inventory
import software.uniqore.storedemo.domain.entities.ItemType
import software.uniqore.storedemo.domain.entities.ItemTypeId
import software.uniqore.storedemo.domain.entities.Store
import software.uniqore.storedemo.domain.entities.StoreId
import software.uniqore.storedemo.domain.usecases.GetStoreInventory
import software.uniqore.storedemo.domain.usecases.StoreNotFoundException
import software.uniqore.storedemo.rest.models.InventoryModel

internal class InventoryServiceTest {
    private val mockUseCase = mockk<GetStoreInventory>()
    private val service = InventoryService(mockUseCase)

    @Test
    fun `getting the inventory of a store should work`() {
        val inventory = Inventory(
            store = Store(id = StoreId(STORE_ID), name = "De Winkel"),
            perItemList = listOf(
                Inventory.PerItemType(
                    itemType = ItemType(id = ItemTypeId(4), name = "Schroefjes"),
                    available = 5, reserved = 2
                )

            )
        )

        val expected = InventoryModel(
            lines = listOf(InventoryModel.PerItem(id = 4, name = "Schroefjes", available = 5, reserved = 2, total = 7))
        )

        coEvery { mockUseCase.invoke(StoreId(STORE_ID)) } returns Result.success(inventory)

        val result = service.getInventoryForStore(STORE_ID)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `requesting the inventory of a nonexistent store should throw`() {
        coEvery { mockUseCase.invoke(StoreId(STORE_ID)) } returns Result.failure(StoreNotFoundException())

        assertThrows<StoreNotFoundException> { service.getInventoryForStore(STORE_ID) }
    }

    companion object {
        const val STORE_ID = 1L
    }
}