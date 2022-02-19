package software.uniqore.storedemo.data.repositories

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import software.uniqore.storedemo.data.datasource.InventoryLine
import software.uniqore.storedemo.data.datasource.ReservationLine
import software.uniqore.storedemo.data.datasource.StoreDemoDataSource
import software.uniqore.storedemo.domain.entities.Inventory
import software.uniqore.storedemo.domain.entities.ItemTypeId
import software.uniqore.storedemo.domain.entities.Store
import software.uniqore.storedemo.domain.entities.StoreId
import java.time.LocalDateTime
import software.uniqore.storedemo.data.datasource.ItemType as DataItemType
import software.uniqore.storedemo.domain.entities.ItemType as DomainItemType

@OptIn(ExperimentalCoroutinesApi::class)
internal class InventoryRepositoryTest {

    private val mockDataStore = mockk<StoreDemoDataSource> {
        coEvery { getItemType(ITEM_TYPE_ID1) } returns DataItemType(ITEM_TYPE_ID1, ITEM_TYPE_NAME1)
        coEvery { getItemType(ITEM_TYPE_ID2) } returns DataItemType(ITEM_TYPE_ID2, ITEM_TYPE_NAME2)
        coEvery { getReservationsForItemInStore(any(), any()) } returns emptyList()
    }
    private val mockClockRepository = mockk<ClockRepository> {
        every { getCurrentDateTime() } answers { LocalDateTime.now() }
    }

    private val inventoryRepository = InventoryRepository(
        dataSource = mockDataStore,
        clockRepository = mockClockRepository
    )

    @Test
    fun `getting the inventory of a store should return a fully filled Inventory-object`() = runBlockingTest {
        val amount1 = 42
        val amount2 = 21

        coEvery { mockDataStore.getStoreInventory(STORE_ID) } returns listOf(
            InventoryLine(
                itemTypeId = ITEM_TYPE_ID1,
                amount = amount1
            ),
            InventoryLine(
                itemTypeId = ITEM_TYPE_ID2,
                amount = amount2
            )
        )

        val result = inventoryRepository.getInventoryForStore(STORE_OBJECT)

        val expectedInventoryLine1 = Inventory.PerItemType(
            DomainItemType(id = ItemTypeId(ITEM_TYPE_ID1), name = ITEM_TYPE_NAME1),
            available = amount1,
            reserved = 0
        )
        val expectedInventoryLine2 = Inventory.PerItemType(
            DomainItemType(id = ItemTypeId(ITEM_TYPE_ID2), name = ITEM_TYPE_NAME2),
            available = amount2,
            reserved = 0
        )

        assertThat(result.store).isEqualTo(STORE_OBJECT)
        assertThat(result.perItemList.size).isEqualTo(2)
        assertThat(result.perItemList).contains(expectedInventoryLine1)
        assertThat(result.perItemList).contains(expectedInventoryLine2)
    }

    @Test
    fun `getting an inventory should tally valid reservations`() = runBlockingTest {
        val now = LocalDateTime.now()
        val validExpirationTime = now.plusMinutes(3)
        val invalidExpirationTime = now.minusMinutes(3)

        val amount = 4
        coEvery { mockDataStore.getStoreInventory(STORE_ID) } returns listOf(
            InventoryLine(
                itemTypeId = ITEM_TYPE_ID1,
                amount = amount
            )
        )

        coEvery {
            mockDataStore.getReservationsForItemInStore(
                itemTypeId = ITEM_TYPE_ID1,
                storeId = STORE_ID
            )
        } returns listOf(
            ReservationLine(ITEM_TYPE_ID1, STORE_ID, "Bertje", validExpirationTime),
            ReservationLine(ITEM_TYPE_ID1, STORE_ID, "Henk", invalidExpirationTime)
        )

        val result = inventoryRepository.getInventoryForStore(STORE_OBJECT)
        assertThat(result.perItemList[0].available).isEqualTo(amount - 1)
        assertThat(result.perItemList[0].reserved).isEqualTo(1)
        assertThat(result.perItemList[0].total).isEqualTo(amount)
    }

    companion object {
        const val ITEM_TYPE_ID1 = 1L
        const val ITEM_TYPE_ID2 = 2L
        const val ITEM_TYPE_NAME1 = "Hamers"
        const val ITEM_TYPE_NAME2 = "Sikkels"

        const val STORE_ID = 1337L
        val STORE_OBJECT = Store(id = StoreId(STORE_ID), name = "Dagobert's IJzerwaren Franchise")
    }
}