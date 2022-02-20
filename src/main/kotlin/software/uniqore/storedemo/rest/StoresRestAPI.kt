package software.uniqore.storedemo.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import software.uniqore.storedemo.rest.models.InventoryModel
import software.uniqore.storedemo.rest.models.StoreModel
import software.uniqore.storedemo.rest.models.UpdateInventoryLineModel
import software.uniqore.storedemo.rest.services.InventoryService
import software.uniqore.storedemo.rest.services.StoresService
import javax.validation.Valid

@RestController
@RequestMapping("/stores")
@Validated
class StoresRestAPI {

    @Autowired
    private lateinit var storesService: StoresService

    @Autowired
    private lateinit var inventoryService: InventoryService

    @GetMapping
    fun getStore(): List<StoreModel> {
        return storesService.getStores()
    }

    @GetMapping("/{storeId}/inventory")
    fun getStoreInventory(@PathVariable storeId: Long): InventoryModel {
        return inventoryService.getInventoryForStore(storeId)
    }

    @PutMapping("/{storeId}/inventory")
    fun putStoreInventory(
        @PathVariable storeId: Long,
        @Valid @RequestBody line: UpdateInventoryLineModel
    ): InventoryModel.PerItem {
        return inventoryService.putInventoryLine(storeId, line)
    }
}