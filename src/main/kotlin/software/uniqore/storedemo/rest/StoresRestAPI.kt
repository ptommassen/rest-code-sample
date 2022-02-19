package software.uniqore.storedemo.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import software.uniqore.storedemo.rest.models.StoreModel
import software.uniqore.storedemo.rest.services.StoresService

@RestController
@RequestMapping("/stores")
class StoresRestAPI {

    @Autowired
    private lateinit var storesService: StoresService

    @GetMapping
    fun getStore(): List<StoreModel> {
        return storesService.getStores()
    }
}