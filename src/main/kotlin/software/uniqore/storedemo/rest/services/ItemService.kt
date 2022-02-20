package software.uniqore.storedemo.rest.services

import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import software.uniqore.storedemo.domain.usecases.CreateItemType
import software.uniqore.storedemo.domain.usecases.GetItemTypes
import software.uniqore.storedemo.rest.models.ItemTypeModel

@Service
class ItemService(private val getItemTypesUseCase: GetItemTypes, private val createItemTypeUseCase: CreateItemType) {

    fun getItemTypes() = runBlocking {
        getItemTypesUseCase().map { ItemTypeModel(it.id.id, it.name) }
    }

    fun createItemType(newItem: ItemTypeModel) = runBlocking {
        createItemTypeUseCase(newItem.name).let { ItemTypeModel(it.id.id, it.name) }
    }
}