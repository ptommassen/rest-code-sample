package software.uniqore.storedemo.rest

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import software.uniqore.storedemo.domain.exceptions.ItemNotFoundException
import software.uniqore.storedemo.domain.exceptions.OutOfStockException
import software.uniqore.storedemo.domain.exceptions.StoreNotFoundException

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [StoreNotFoundException::class])
    protected fun handleStoreNotFound(
        ex: StoreNotFoundException?, request: WebRequest?
    ): ResponseEntity<Any> {
        val bodyOfResponse = "Store not found!"
        return handleExceptionInternal(
            ex!!, bodyOfResponse,
            HttpHeaders(), HttpStatus.BAD_REQUEST, request!!
        )
    }

    @ExceptionHandler(value = [ItemNotFoundException::class])
    protected fun handleStoreNotFound(
        ex: ItemNotFoundException?, request: WebRequest?
    ): ResponseEntity<Any> {
        val bodyOfResponse = "Item not found!"
        return handleExceptionInternal(
            ex!!, bodyOfResponse,
            HttpHeaders(), HttpStatus.BAD_REQUEST, request!!
        )
    }

    @ExceptionHandler(value = [OutOfStockException::class])
    protected fun handleStoreNotFound(
        ex: OutOfStockException?, request: WebRequest?
    ): ResponseEntity<Any> {
        val bodyOfResponse = "No more available items!"
        return handleExceptionInternal(
            ex!!, bodyOfResponse,
            HttpHeaders(), HttpStatus.NOT_FOUND, request!!
        )
    }


}