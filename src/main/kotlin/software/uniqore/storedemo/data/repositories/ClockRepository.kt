package software.uniqore.storedemo.data.repositories

import java.time.LocalDateTime

/**
 * Returns the current time; basically only extracted to simplify testing
 */
class ClockRepository {
    fun getCurrentDateTime() = LocalDateTime.now()
}