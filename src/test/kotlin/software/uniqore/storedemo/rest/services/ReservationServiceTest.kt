package software.uniqore.storedemo.rest.services

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import software.uniqore.storedemo.domain.entities.ItemTypeId
import software.uniqore.storedemo.domain.entities.StoreId
import software.uniqore.storedemo.domain.usecases.OutOfStockException
import software.uniqore.storedemo.domain.usecases.ReserveItem

internal class ReservationServiceTest {
    private val mockUseCase = mockk<ReserveItem>()
    private val service = ReservationService(reserveItemUseCase = mockUseCase)

    @Test
    fun `reserving an available item should work`() {
        coEvery { mockUseCase.invoke(StoreId(STORE_ID), ItemTypeId(ITEM_ID), CUSTOMER_NAME) } returns Result.success(
            true
        )

        service.reserveItemAtStore(STORE_ID, ITEM_ID, CUSTOMER_NAME)

        coVerify {
            mockUseCase.invoke(StoreId(STORE_ID), ItemTypeId(ITEM_ID), CUSTOMER_NAME)
        }
    }

    @Test
    fun `failing to reserve an item should throw an exception`() {
        coEvery { mockUseCase.invoke(StoreId(STORE_ID), ItemTypeId(ITEM_ID), CUSTOMER_NAME) } returns Result.failure(
            OutOfStockException()
        )

        assertThrows<OutOfStockException> { service.reserveItemAtStore(STORE_ID, ITEM_ID, CUSTOMER_NAME) }
    }

    companion object {
        const val ITEM_ID = 1L
        const val STORE_ID = 2L
        const val CUSTOMER_NAME = "ZIM!"
    }
}