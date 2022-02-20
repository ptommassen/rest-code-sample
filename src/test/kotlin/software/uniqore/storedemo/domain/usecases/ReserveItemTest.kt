package software.uniqore.storedemo.domain.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import software.uniqore.storedemo.data.repositories.ClockRepository
import software.uniqore.storedemo.data.repositories.ItemRepository
import software.uniqore.storedemo.data.repositories.ReservationRepository
import software.uniqore.storedemo.data.repositories.StoreRepository
import software.uniqore.storedemo.domain.entities.Customer
import software.uniqore.storedemo.domain.entities.ItemType
import software.uniqore.storedemo.domain.entities.ItemTypeId
import software.uniqore.storedemo.domain.entities.Store
import software.uniqore.storedemo.domain.entities.StoreId
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
internal class ReserveItemTest {
    private val mockReservationRepository = mockk<ReservationRepository>()
    private val mockStoreRepository = mockk<StoreRepository> {
        coEvery { getStoreById(STORE_ID) } returns STORE
    }
    private val mockItemRepository = mockk<ItemRepository> {
        coEvery { getItemTypeById(ITEM_ID) } returns ITEM
    }
    private val mockClockRepository = mockk<ClockRepository> {
        every { getCurrentDateTime() } returns NOW
    }
    private val reserveItemUseCase = ReserveItem(
        reservationRepository = mockReservationRepository,
        storeRepository = mockStoreRepository,
        itemRepository = mockItemRepository,
        clockRepository = mockClockRepository
    )

    @Test
    fun `reserving an item should work`() = runBlockingTest {
        val CUSTOMER_NAME = "Bertje"
        val EXPECTED_EXPIRATION_TIME = NOW.plusMinutes(5L)
        coEvery { mockReservationRepository.reserveItem(ITEM, STORE, any(), any()) } returns true

        val result = reserveItemUseCase(STORE_ID, ITEM_ID, CUSTOMER_NAME)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isTrue()

        coVerify {
            mockReservationRepository.reserveItem(ITEM, STORE, Customer(CUSTOMER_NAME), EXPECTED_EXPIRATION_TIME)
        }
    }

    @Test
    fun `reserving an item in a nonexistent store should fail`() = runBlockingTest {
        val INVALID_STORE_ID = StoreId(4232L)
        coEvery { mockStoreRepository.getStoreById(INVALID_STORE_ID) } returns null

        val result = reserveItemUseCase(INVALID_STORE_ID, ITEM_ID, "Henk")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(StoreNotFoundException::class.java)
    }

    @Test
    fun `reserving a non-existent item should fail`() = runBlockingTest {
        val INVALID_ITEM_ID = ItemTypeId(4232L)
        coEvery { mockItemRepository.getItemTypeById(INVALID_ITEM_ID) } returns null

        val result = reserveItemUseCase(STORE_ID, INVALID_ITEM_ID, "Ernie")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(ItemNotFoundException::class.java)
    }

    @Test
    fun `reserving an out-of-stock item should fail`() = runBlockingTest {
        coEvery { mockReservationRepository.reserveItem(ITEM, STORE, any(), any()) } returns false

        val result = reserveItemUseCase(STORE_ID, ITEM_ID, "Grover")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(OutOfStockException::class.java)
    }


    companion object {
        val ITEM_ID = ItemTypeId(1)
        val ITEM = ItemType(id = ITEM_ID, name = "Kettingzaag")
        val STORE_ID = StoreId(1337)
        val STORE = Store(id = STORE_ID, name = "De Winkel")
        val NOW = LocalDateTime.now()

    }
}