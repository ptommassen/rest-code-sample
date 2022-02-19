package software.uniqore.storedemo.data.repositories

import software.uniqore.storedemo.data.datasource.StoreDemoDataSource
import software.uniqore.storedemo.domain.entities.Store
import software.uniqore.storedemo.domain.entities.StoreId

class StoreRepository(private val dataSource: StoreDemoDataSource) {
    suspend fun getStoreById(storeId: StoreId): Store? {
        val storeData = dataSource.getStoreById(storeId.id)
        return storeData?.let {
            Store(id = storeId, name = it.name)
        }
    }
}