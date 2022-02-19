package software.uniqore.storedemo.rest.services

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import software.uniqore.storedemo.domain.usecases.GetStores
import software.uniqore.storedemo.rest.models.StoreModel

@Service
class StoresService(private val getStoresUseCase: GetStores) {

    fun getStores(): List<StoreModel> =
        runBlocking {
            getStoresUseCase().map {
                StoreModel(id = it.id.id, name = it.name)
            }
        }

}