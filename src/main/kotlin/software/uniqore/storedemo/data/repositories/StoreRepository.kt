package software.uniqore.storedemo.data.repositories

import org.springframework.stereotype.Component
import software.uniqore.storedemo.data.datasource.StoreData
import software.uniqore.storedemo.data.datasource.StoreDemoDataSource
import software.uniqore.storedemo.domain.entities.Store
import software.uniqore.storedemo.domain.entities.StoreId

@Component
class StoreRepository(private val dataSource: StoreDemoDataSource) {
    suspend fun getStoreById(storeId: StoreId): Store? {
        val storeData = dataSource.getStoreById(storeId.id)
        return storeData.toDomain()
    }

    suspend fun getStores(): List<Store> {
        val stores = dataSource.getStores()
        return stores.mapNotNull { it.toDomain() }
    }

    fun StoreData?.toDomain() = this?.let { Store(id = StoreId(it.storeId), name = it.name) }
}