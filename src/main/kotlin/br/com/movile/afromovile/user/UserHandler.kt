package br.com.movile.afromovile.user

import org.springframework.http.HttpStatus.CREATED
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.json
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class UserHandler(private val userService: UserService) {

    fun findAll(request: ServerRequest): Mono<ServerResponse> =
        ServerResponse.ok().json().body(userService.findAll(), UserDto::class.java)

    fun save(request: ServerRequest): Mono<ServerResponse> =
        request
            .bodyToMono<UserDto>()
            .flatMap { ServerResponse.status(CREATED).body(userService.save(it), UserDto::class.java) }

    fun delete(request: ServerRequest): Mono<ServerResponse> =
        request
            .pathVariable("id").toMono()
            .map { id -> id.toLong() }
            .flatMap { id -> userService.delete(id) }
            .then(ServerResponse.ok().build())

    fun findById(request: ServerRequest): Mono<ServerResponse> =
        this.userService
            .findById(request.pathVariable("id").toLong())
            .flatMap { user -> ServerResponse.ok().body(user.toMono(), UserDto::class.java) }
            .switchIfEmpty(ServerResponse.notFound().build())

}
