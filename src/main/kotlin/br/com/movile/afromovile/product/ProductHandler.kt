package br.com.movile.afromovile.product

import br.com.movile.afromovile.model.ErrorMessage
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.json

@Component
class ProductHandler(private val service: ProductService) {

    suspend fun findAll(request: ServerRequest): ServerResponse = ServerResponse.ok().bodyAndAwait(service.findAll())

    suspend fun save(request: ServerRequest): ServerResponse {
        val body = request.awaitBody(ProductDto::class)
        val product = service.save(body)

        return ServerResponse.status(HttpStatus.CREATED).bodyValueAndAwait(product)
    }

    suspend fun findById(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLongOrNull()

        return if (id == null) {
            ServerResponse.badRequest().json().bodyValueAndAwait(ErrorMessage("`Id` is mandatory"))
        } else {
            val product = service.findById(id)
            if (product == null) ServerResponse.notFound().buildAndAwait()
            else ServerResponse.ok().json().bodyValueAndAwait(product)
        }
    }

    suspend fun delete(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLongOrNull()

        return if (id == null) {
            ServerResponse.badRequest().json().bodyValueAndAwait(ErrorMessage("`Id` is mandatory"))
        } else {
            if (service.delete(id)) ServerResponse.noContent().buildAndAwait()
            else ServerResponse.status(HttpStatus.NOT_FOUND).json().bodyValueAndAwait(ErrorMessage("Resource $id not found"))
        }
    }

}