package software.uniqore.storedemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StoreDemoApplication

fun main(args: Array<String>) {
    runApplication<StoreDemoApplication>(*args)
}
