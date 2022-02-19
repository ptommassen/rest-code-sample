package software.uniqore.storedemo.domain.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import software.uniqore.storedemo.data.repositories.StoreRepository
import software.uniqore.storedemo.domain.entities.Store
import software.uniqore.storedemo.domain.entities.StoreId

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetStoresTest {
    private val mockStoreRepository = mockk<StoreRepository>()
    private val getStoreUseCase = GetStores(mockStoreRepository)

    @Test
    fun `GetStores use case should retrieve all stores`() = runBlockingTest {
        val stores = listOf(
            Store(id = StoreId(1), name = "Store 1"),
            Store(id = StoreId(1), name = "Store 2")
        )

        coEvery { mockStoreRepository.getStores() } returns stores

        val result = getStoreUseCase()

        assertThat(result).isEqualTo(stores)
    }

}