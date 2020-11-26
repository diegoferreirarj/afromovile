package br.com.movile.afromovile.user

import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserService(private val userRepository: UserRepository) {

    fun findAll(): Flux<UserDto> = userRepository.findAll().map { it.toDto() }
    fun save(dto: UserDto): Mono<UserDto> = userRepository.save(dto.toEntity()).map { it.toDto() }
    fun findById(id: Long): Mono<UserDto> = userRepository.findById(id).map { it.toDto() }
    fun delete(id: Long): Mono<Void> = userRepository.deleteById(id)

}