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


}

