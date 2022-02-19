package software.uniqore.storedemo.rest.services

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Test
import software.uniqore.storedemo.domain.entities.Store
import software.uniqore.storedemo.domain.entities.StoreId
import software.uniqore.storedemo.domain.usecases.GetStores
import software.uniqore.storedemo.rest.models.StoreModel

internal class StoresServiceTest {

    private val mockGetStoresUseCase = mockk<GetStores>()
    private val storesService = StoresService(mockGetStoresUseCase)

    @Test
    fun `getStores should return stores`() {
        val storeId = 1337L
        val storeName = "De Schroefjesspecialist"

        coEvery { mockGetStoresUseCase.invoke() } returns listOf(Store(id = StoreId(storeId), name = storeName))

        val expected = listOf(StoreModel(id = storeId, name = storeName))

        val result = storesService.getStores()
        assertThat(result).isEqualTo(expected)
    }

}