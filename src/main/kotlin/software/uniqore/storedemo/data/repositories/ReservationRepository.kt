package software.uniqore.storedemo.data.repositories

import org.springframework.stereotype.Component
import software.uniqore.storedemo.data.datasource.StoreDemoDataSource
import software.uniqore.storedemo.domain.entities.Customer
import software.uniqore.storedemo.domain.entities.ItemType
import software.uniqore.storedemo.domain.entities.Store
import java.time.LocalDateTime

@Component
class ReservationRepository(private val clockRepository: ClockRepository, private val dataSource: StoreDemoDataSource) {

    /**
     * Tries to reserve an item
     *
     * TODO: it's currently possible to over-reserve an item due to concurrency stuff; this should be fixed at the datasource-level
     * (e.g. abuse SQL transactions or use CAS or something), but we're currently doing that at repository level, which isn't
     * ideal due to lack of locks and such. Should be fine within the scope of this demo, though :)
     *
     * @param item the item to reserve
     * @param store the store to reserve the item at
     * @param customer the customer to reserve the item for
     * @returns true if the reservation was successful, false if not (because they are out of stock)
     */
    suspend fun reserveItem(item: ItemType, store: Store, customer: Customer, expiresAt: LocalDateTime): Boolean {
        val now = clockRepository.getCurrentDateTime()
        val validReservations =
            dataSource.getReservationsForItemInStore(itemTypeId = item.id.id, storeId = store.id.id).count {
                it.expirationTime.isAfter(now)
            }
        val stock = dataSource.getStoreInventoryStock(store.id.id, item.id.id)
        return if (stock > validReservations) {
            dataSource.putReservation(
                itemTypeId = item.id.id,
                storeId = store.id.id,
                customer = customer.name,
                expirationTime = expiresAt
            )
            true
        } else {
            false
        }
    }
}
