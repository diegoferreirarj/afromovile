package br.com.movile.afromovile.user

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

@ExtendWith(MockKExtension::class)
internal class UserHandlerTest {

    @InjectMockKs
    private lateinit var handler: UserHandler

    @MockK
    private lateinit var service: UserService

    @MockK
    private lateinit var request: ServerRequest

    @Test
    fun findAll() {
        every { service.findAll() } returns listOf(createUser(id = 1), createUser(id=2)).toFlux()

        runBlocking {
            val response = handler.findAll(request).block()
            assertEquals(response?.statusCode(), HttpStatus.OK)
        }
    }

    @Test
    fun save() {
        every { service.save(any()) } returns createUser(id = 1).toMono()
        every { request.bodyToMono<UserDto>() } returns createUser().toMono()

        runBlocking {
            val response = handler.save(request).block()
            assertEquals(response?.statusCode(), HttpStatus.CREATED)
        }
    }

    @Test
    fun delete() {
        every { service.delete(1) } returns Mono.empty()
        every { service.findById(1) } returns createUser(id = 1).toMono()
        every { request.pathVariable("id") } returns "1"

        runBlocking {
            val response = handler.delete(request).block()
            assertEquals(HttpStatus.NO_CONTENT, response?.statusCode())
        }
    }

    @Test
    fun findById() {
        every { service.findById(1) } returns createUser(id = 1).toMono()
        every { request.pathVariable("id") } returns "1"

        runBlocking {
            val response = handler.findById(request).block()
            assertEquals(response?.statusCode(), HttpStatus.OK)
        }
    }

    private fun createUser(id: Long? = null, name: String = "a user", email: String = "auser@users.com") = UserDto(id, name, email)

}