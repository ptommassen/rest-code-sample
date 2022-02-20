package software.uniqore.storedemo.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import software.uniqore.storedemo.rest.models.ItemTypeModel
import software.uniqore.storedemo.rest.services.ItemService

@RestController
@RequestMapping("/items")
@Validated
class ItemRestAPI {

    @Autowired
    private lateinit var itemService: ItemService

    @GetMapping
    fun getItemTypes(): List<ItemTypeModel> {
        return itemService.getItemTypes()
    }
}