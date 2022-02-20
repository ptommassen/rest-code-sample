package software.uniqore.storedemo.rest.models

import software.uniqore.storedemo.rest.validators.IdOrNameNeeded
import javax.validation.constraints.Min

data class InventoryModel(val lines: List<PerItem>) {
    data class PerItem(
        val id: Long,
        val name: String,
        val available: Int,
        val reserved: Int,
        val total: Int
    )
}

@IdOrNameNeeded
data class UpdateInventoryLineModel(val id: Long? = null, val name: String? = null, @field:Min(0) val total: Int)