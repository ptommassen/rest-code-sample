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
import software.uniqore.storedemo.domain.exceptions.StoreNotFoundException
import software.uniqore.storedemo.domain.usecases.CreateItemType
import software.uniqore.storedemo.domain.usecases.GetStoreInventory
import software.uniqore.storedemo.domain.usecases.UpdateStock
import software.uniqore.storedemo.rest.models.InventoryModel
import software.uniqore.storedemo.rest.models.UpdateInventoryLineModel

internal class InventoryServiceTest {
    private val mockGetStoreInventory = mockk<GetStoreInventory>()
    private val mockUpdateStock = mockk<UpdateStock>()
    private val mockCreateItemType = mockk<CreateItemType>()
    private val service = InventoryService(mockGetStoreInventory, mockUpdateStock, mockCreateItemType)

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

        coEvery { mockGetStoreInventory.invoke(StoreId(STORE_ID)) } returns Result.success(inventory)

        val result = service.getInventoryForStore(STORE_ID)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `requesting the inventory of a nonexistent store should throw`() {
        coEvery { mockGetStoreInventory.invoke(StoreId(STORE_ID)) } returns Result.failure(StoreNotFoundException())

        assertThrows<StoreNotFoundException> { service.getInventoryForStore(STORE_ID) }
    }

    @Test
    fun `updating an inventory should work`() {
        val newAmount = 5
        coEvery {
            mockUpdateStock.invoke(
                StoreId(STORE_ID),
                ItemTypeId(ITEM_TYPE_ID),
                newAmount
            )
        } returns Result.success(
            Inventory.PerItemType(ItemType(ItemTypeId(ITEM_TYPE_ID), ITEM_TYPE_NAME), 5, 0)
        )

        val expected = InventoryModel.PerItem(ITEM_TYPE_ID, ITEM_TYPE_NAME, newAmount, 0, newAmount)

        val request = UpdateInventoryLineModel(id = ITEM_TYPE_ID, total = newAmount)
        val result = service.putInventoryLine(STORE_ID, request)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `adding a not-yet-existing item to an inventory should work`() {
        val newAmount = 5
        val itemName = "Schroefjes"

        coEvery {
            mockCreateItemType.invoke(itemName)
        } returns ItemType(ItemTypeId(ITEM_TYPE_ID), itemName)

        coEvery {
            mockUpdateStock.invoke(
                StoreId(STORE_ID),
                ItemTypeId(ITEM_TYPE_ID),
                newAmount
            )
        } returns Result.success(
            Inventory.PerItemType(ItemType(ItemTypeId(ITEM_TYPE_ID), ITEM_TYPE_NAME), 5, 0)
        )

        val expected = InventoryModel.PerItem(ITEM_TYPE_ID, ITEM_TYPE_NAME, newAmount, 0, newAmount)

        val request = UpdateInventoryLineModel(name = itemName, total = newAmount)
        val result = service.putInventoryLine(STORE_ID, request)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `updating a nonexistent inventory should fail`() {
        coEvery { mockUpdateStock.invoke(StoreId(STORE_ID), ItemTypeId(ITEM_TYPE_ID), any()) } returns Result.failure(
            StoreNotFoundException()
        )

        val request = UpdateInventoryLineModel(id = ITEM_TYPE_ID, total = 24)
        assertThrows<StoreNotFoundException> { service.putInventoryLine(STORE_ID, request) }
    }

    companion object {
        const val STORE_ID = 1L
        const val ITEM_TYPE_ID = 2L
        const val ITEM_TYPE_NAME = "Spijkers"
    }
}