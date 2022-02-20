package software.uniqore.storedemo.data.repositories

import org.springframework.stereotype.Component
import software.uniqore.storedemo.data.datasource.StoreDemoDataSource
import software.uniqore.storedemo.domain.entities.ItemType
import software.uniqore.storedemo.domain.entities.ItemTypeId
import software.uniqore.storedemo.data.datasource.ItemType as DataItemType

@Component
class ItemRepository(private val storeDemoDataSource: StoreDemoDataSource) {

    suspend fun getItemTypeById(itemTypeId: ItemTypeId) = storeDemoDataSource.getItemType(itemTypeId.id).toDomain()

    private fun DataItemType?.toDomain() = this?.let { ItemType(id = ItemTypeId(itemTypeId), name = name) }
}