package software.uniqore.storedemo.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import software.uniqore.storedemo.rest.models.ReservationModel
import software.uniqore.storedemo.rest.services.ReservationService

@RestController
@RequestMapping("/reservation")
class ReservationRestAPI {

    @Autowired
    private lateinit var reservationService: ReservationService

    @PostMapping
    fun postReserveItem(@RequestBody reservation: ReservationModel) {
        reservationService.reserveItemAtStore(
            storeId = reservation.storeId,
            itemId = reservation.itemTypeId,
            customerName = reservation.customerName
        )
    }
}