package software.uniqore.storedemo.rest.models

data class InventoryModel(val lines: List<PerItem>) {
    data class PerItem(
        val id: Long,
        val name: String,
        val available: Int,
        val reserved: Int,
        val total: Int
    )
}