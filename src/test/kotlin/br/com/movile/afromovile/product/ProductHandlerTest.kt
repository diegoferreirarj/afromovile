package br.com.movile.afromovile.product

import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.kotlin.core.publisher.toMono

@ExtendWith(MockKExtension::class)
internal class ProductHandlerTest {

    @InjectMockKs
    private lateinit var handler: ProductHandler

    @MockK
    private lateinit var service: ProductService

    @MockK
    private lateinit var request: ServerRequest

    @Test
    fun findAll() {
        coEvery { service.findAll() } returns flowOf(createProduct(id = 1), createProduct(id=2))

        runBlocking {
            val response = handler.findAll(request)
            assertEquals(response.statusCode(), HttpStatus.OK)
        }
    }

    @Test
    fun save() {
        coEvery { service.save(any()) } returns createProduct(id = 1)
        every { request.bodyToMono(ProductDto::class.java) } returns createProduct().toMono()

        runBlocking {
            val response = handler.save(request)
            assertEquals(response.statusCode(), HttpStatus.CREATED)
        }
    }

    @Test
    fun delete() {
        coEvery { service.delete(1) } returns true
        every { request.pathVariable("id") } returns "1"

        runBlocking {
            val response = handler.delete(request)
            assertEquals(response.statusCode(), HttpStatus.NO_CONTENT)
        }
    }

    @Test
    fun findById() {
        coEvery { service.findById(1) } returns createProduct(id = 1)
        every { request.pathVariable("id") } returns "1"

        runBlocking {
            val response = handler.findById(request)
            assertEquals(response.statusCode(), HttpStatus.OK)
        }
    }

    private fun createProduct(id: Long? = null, name: String = "product", description: String = "a description") = ProductDto(id, name, description)

}