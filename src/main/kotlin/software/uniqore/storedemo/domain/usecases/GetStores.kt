package software.uniqore.storedemo.domain.usecases

import org.springframework.stereotype.Component
import software.uniqore.storedemo.data.repositories.StoreRepository
import software.uniqore.storedemo.domain.entities.Store

@Component
class GetStores(private val storeRepository: StoreRepository) {
    suspend operator fun invoke(): List<Store> = storeRepository.getStores()

}