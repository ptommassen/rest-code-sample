package software.uniqore.storedemo.rest.services

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import software.uniqore.storedemo.domain.entities.ItemTypeId
import software.uniqore.storedemo.domain.entities.StoreId
import software.uniqore.storedemo.domain.usecases.ReserveItem

@Service
class ReservationService(private val reserveItemUseCase: ReserveItem) {
    fun reserveItemAtStore(storeId: Long, itemId: Long, customerName: String) = runBlocking {
        reserveItemUseCase.invoke(StoreId(storeId), ItemTypeId(itemId), customerName).getOrThrow()
    }
}