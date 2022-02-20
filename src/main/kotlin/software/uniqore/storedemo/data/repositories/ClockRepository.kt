package software.uniqore.storedemo.data.repositories

import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * Returns the current time; basically only extracted to simplify testing
 */
@Component
class ClockRepository {
    fun getCurrentDateTime() = LocalDateTime.now()
}