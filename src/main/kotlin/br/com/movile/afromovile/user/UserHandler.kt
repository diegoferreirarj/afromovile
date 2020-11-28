package br.com.movile.afromovile.user

import br.com.movile.afromovile.model.ErrorMessage
import org.springframework.http.HttpStatus.CREATED
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.json
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.publisher.toMono

@Component
class UserHandler(private val userService: UserService) {

    fun findAll(request: ServerRequest): Mono<ServerResponse> =
        ServerResponse.ok().json().body(userService.findAll(), UserDto::class.java)

    fun save(request: ServerRequest): Mono<ServerResponse> = request
        .bodyToMono<UserDto>()
        .map { userService.save(it) }
        .flatMap { ServerResponse.status(CREATED).json().body(it, UserDto::class.java) }
        .onErrorResume { ServerResponse.badRequest().build() }

    fun findById(request: ServerRequest): Mono<ServerResponse> = request
        .pathVariable("id")
        .toMono()
        .map { it.toLong() }
        .flatMap { userService.findById(it) }
        .flatMap { ServerResponse.ok().json().bodyValue(it) }
        .switchIfEmpty { ServerResponse.notFound().build() }
        .onErrorResume { ServerResponse.badRequest().json().bodyValue(ErrorMessage("Valid `Id` is mandatory")) }

    fun delete(request: ServerRequest): Mono<ServerResponse> = request
        .pathVariable("id")
        .toMono()
        .map { it.toLong() }
        .flatMap { userService.findById(it) }
        .flatMap { userService.delete(it.id!!).then(ServerResponse.noContent().build()) }
        .switchIfEmpty { ServerResponse.notFound().build() }
        .onErrorResume { ServerResponse.badRequest().json().bodyValue(ErrorMessage("Valid `Id` is mandatory")) }

}
