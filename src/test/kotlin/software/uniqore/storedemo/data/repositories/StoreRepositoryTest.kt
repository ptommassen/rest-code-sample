package software.uniqore.storedemo.data.repositories

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import software.uniqore.storedemo.data.datasource.StoreData
import software.uniqore.storedemo.data.datasource.StoreDemoDataSource
import software.uniqore.storedemo.domain.entities.Store
import software.uniqore.storedemo.domain.entities.StoreId

@OptIn(ExperimentalCoroutinesApi::class)
internal class StoreRepositoryTest {
    private val mockDataSource = mockk<StoreDemoDataSource>()
    private val storeRepository = StoreRepository(dataSource = mockDataSource)

    @Test
    fun `retrieving an existing store should return it`() = runBlockingTest {
        val storeName = "Test Store"
        val storeId = 1337L

        coEvery { mockDataSource.getStoreById(storeId) } returns StoreData(storeId, storeName)

        val result = storeRepository.getStoreById(StoreId(storeId))

        assertNotNull(result)
        assertThat(result!!.name).isEqualTo(storeName)
    }

    @Test
    fun `retrieving all stores should return them`() = runBlockingTest {
        val storeName1 = "Test Store 1"
        val storeName2 = "Test Store 2"
        val storeId1 = 1L
        val storeId2 = 2L

        coEvery { mockDataSource.getStores() } returns listOf(
            StoreData(storeId1, storeName1),
            StoreData(storeId2, storeName2),
        )

        val result = storeRepository.getStores()

        assertThat(result.size).isEqualTo(2)
        assertThat(result).contains(Store(StoreId(storeId1), storeName1))
        assertThat(result).contains(Store(StoreId(storeId2), storeName2))
    }


}

