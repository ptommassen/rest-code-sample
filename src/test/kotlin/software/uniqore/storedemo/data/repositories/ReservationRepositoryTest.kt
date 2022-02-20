package software.uniqore.storedemo.data.repositories

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import software.uniqore.storedemo.data.datasource.ReservationLine
import software.uniqore.storedemo.data.datasource.StoreDemoDataSource
import software.uniqore.storedemo.domain.entities.Customer
import software.uniqore.storedemo.domain.entities.ItemType
import software.uniqore.storedemo.domain.entities.ItemTypeId
import software.uniqore.storedemo.domain.entities.Store
import software.uniqore.storedemo.domain.entities.StoreId
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
internal class ReservationRepositoryTest {

    private val mockDataSource = mockk<StoreDemoDataSource> {
        coEvery { putReservation(any(), any(), any(), any()) } just runs
        coEvery { getReservationsForItemInStore(any(), any()) } returns emptyList()
        coEvery { getStoreInventoryStock(any(), any()) } returns 1
    }
    private val mockClockRepository = mockk<ClockRepository> {
        coEvery { getCurrentDateTime() } answers { LocalDateTime.now() }
    }
    private val reservationRepository = ReservationRepository(
        clockRepository = mockClockRepository,
        dataSource = mockDataSource
    )

    @Test
    fun `adding a reservation should do just that`() = runBlockingTest {
        val result = reservationRepository.reserveItem(ITEM_TYPE, STORE, CUSTOMER, DEFAULT_TIME)

        assertThat(result).isTrue()

        coVerify {
            mockDataSource.putReservation(ITEM_TYPE.id.id, STORE.id.id, CUSTOMER.name, DEFAULT_TIME)
        }
    }

    @Test
    fun `adding a reservation to an item without stock should fail`() = runBlockingTest {
        coEvery { mockDataSource.getStoreInventoryStock(STORE.id.id, ITEM_TYPE.id.id) } returns 0

        val result = reservationRepository.reserveItem(ITEM_TYPE, STORE, CUSTOMER, DEFAULT_TIME)

        assertThat(result).isFalse()
    }

    @Test
    fun `adding a reservation to an item with only reserved stock should fail`() = runBlockingTest {
        coEvery { mockDataSource.getStoreInventoryStock(STORE.id.id, ITEM_TYPE.id.id) } returns 1
        coEvery { mockDataSource.getReservationsForItemInStore(ITEM_TYPE.id.id, STORE.id.id) } returns listOf(
            ReservationLine(ITEM_TYPE.id.id, STORE.id.id, "ZIM!", DEFAULT_TIME.plusMinutes(2))
        )

        val result = reservationRepository.reserveItem(ITEM_TYPE, STORE, CUSTOMER, DEFAULT_TIME)

        assertThat(result).isFalse()
    }

    @Test
    fun `adding a reservation to an item with some reserved stock should succeed`() = runBlockingTest {
        coEvery { mockDataSource.getStoreInventoryStock(STORE.id.id, ITEM_TYPE.id.id) } returns 2
        coEvery { mockDataSource.getReservationsForItemInStore(ITEM_TYPE.id.id, STORE.id.id) } returns listOf(
            ReservationLine(ITEM_TYPE.id.id, STORE.id.id, "ZIM!", DEFAULT_TIME.plusMinutes(2))
        )
        coEvery { mockDataSource.getReservationsForItemInStore(ITEM_TYPE.id.id, STORE.id.id) } returns listOf(
            ReservationLine(ITEM_TYPE.id.id, STORE.id.id, "ZIM!", DEFAULT_TIME.minusMinutes(2))
        )
        val result = reservationRepository.reserveItem(ITEM_TYPE, STORE, CUSTOMER, DEFAULT_TIME)

        assertThat(result).isTrue()
    }

    companion object {
        val ITEM_TYPE = ItemType(id = ItemTypeId(1337), name = "Linkshandige schroefjes")
        val STORE = Store(id = StoreId(42), name = "De Schroevengigant")
        val CUSTOMER = Customer(name = "ZIM!!")
        val DEFAULT_TIME = LocalDateTime.now().plusMinutes(5)
    }

}