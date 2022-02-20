package software.uniqore.storedemo.domain.usecases

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import software.uniqore.storedemo.data.repositories.ItemRepository

@Component
class CreateItemType(private val itemRepository: ItemRepository) {
    suspend operator fun invoke(name: String) = runBlocking {
        itemRepository.createItemType(name)
    }

}