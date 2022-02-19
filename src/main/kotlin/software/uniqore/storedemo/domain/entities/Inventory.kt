package software.uniqore.storedemo.domain.entities

data class Inventory(val store: Store, val perItemList: List<PerItemType>) {
    data class PerItemType(val itemType: ItemType, val available: Int, val reserved: Int) {
        val total = available + reserved
    }
}