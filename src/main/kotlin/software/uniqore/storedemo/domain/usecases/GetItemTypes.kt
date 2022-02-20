package software.uniqore.storedemo.domain.usecases

import org.springframework.stereotype.Component
import software.uniqore.storedemo.data.repositories.ItemRepository

@Component
class GetItemTypes(private val itemRepository: ItemRepository) {

    suspend operator fun invoke() = itemRepository.getItemTypes()
}