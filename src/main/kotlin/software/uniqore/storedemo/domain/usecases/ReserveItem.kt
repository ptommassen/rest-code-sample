package software.uniqore.storedemo.domain.usecases

import org.springframework.stereotype.Component
import software.uniqore.storedemo.data.repositories.ClockRepository
import software.uniqore.storedemo.data.repositories.ItemRepository
import software.uniqore.storedemo.data.repositories.ReservationRepository
import software.uniqore.storedemo.data.repositories.StoreRepository
import software.uniqore.storedemo.domain.entities.Customer
import software.uniqore.storedemo.domain.entities.ItemTypeId
import software.uniqore.storedemo.domain.entities.StoreId
import software.uniqore.storedemo.domain.exceptions.ItemNotFoundException
import software.uniqore.storedemo.domain.exceptions.OutOfStockException
import software.uniqore.storedemo.domain.exceptions.StoreNotFoundException

@Component
class ReserveItem(
    private val reservationRepository: ReservationRepository,
    private val storeRepository: StoreRepository,
    private val itemRepository: ItemRepository,
    private val clockRepository: ClockRepository
) {
    suspend operator fun invoke(storeId: StoreId, itemId: ItemTypeId, customerName: String): Result<Boolean> {
        val store = storeRepository.getStoreById(storeId) ?: return Result.failure(StoreNotFoundException())
        val item = itemRepository.getItemTypeById(itemId) ?: return Result.failure(ItemNotFoundException())
        val result = reservationRepository.reserveItem(
            item = item,
            store = store,
            customer = Customer(customerName),
            expiresAt = clockRepository.getCurrentDateTime().plusMinutes(RESERVATION_TIME_IN_MINUTES)
        )
        return if (result) Result.success(true) else Result.failure(OutOfStockException())

    }

    private val RESERVATION_TIME_IN_MINUTES = 5L
}