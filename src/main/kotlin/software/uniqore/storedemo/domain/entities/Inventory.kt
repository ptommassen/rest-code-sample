package software.uniqore.storedemo.domain.entities

data class InventoryPerItemType(val itemType: ItemType, val available: Int, val reserved: Int) {
    val total = available + reserved
}

data class Inventory(val store: Store, val perItemList: List<InventoryPerItemType>)