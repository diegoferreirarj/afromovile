package br.com.movile.afromovile.user

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import org.springframework.web.reactive.function.BodyInserters

@SpringBootTest
@AutoConfigureWebTestClient
internal class WebfluxUserTestIT(
    @Autowired private val client: WebTestClient,
    @Autowired private val userRepository: UserRepository
) {

    companion object {
        private val USERS = listOf(
            User(name = "Test no 1", email = "test1@users.com"),
            User(name = "Test no 2", email = "test2@users.com"),
            User(name = "Test no 3", email = "test3@users.com"),
            User(name = "Test no 4", email = "test4@users.com")
        )
    }

    @BeforeEach
    fun beforeEach() {
        userRepository.saveAll(USERS).subscribe()
    }

    @Test
    fun `list of users`() {
        val users = userRepository.findAll().collectList().block()?.map(User::toDto)

        val response = client.get()
                .uri("/users")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBodyList<UserDto>()
                .hasSize(users?.size!!)
                .returnResult()
                .responseBody

        assertThat(response)
                .isNotNull
                .allSatisfy { assertThat(it).isIn(users) }
    }

    @Test
    fun `existing user returns OK`() {
        val expectedUser = userRepository.findAll()
                .collectList().block()?.firstOrNull { it.id != null && it.id == 1L }?.toDto()
        assertThat(expectedUser).isNotNull

        val response = client.get()
                .uri("/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody(UserDto::class.java)
                .returnResult()
                .responseBody

        assertThat(response)
                .isNotNull
                .isEqualTo(expectedUser)
    }

    @Test
    fun `returns OK`() {
        val newUser = UserDto(name = "New Test", email = "newtest@users.com")

        val response = client.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(newUser))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated
                .expectBody(UserDto::class.java)
                .returnResult()
                .responseBody

        assertThat(response)
                .isNotNull
                .isEqualToComparingOnlyGivenFields(newUser, "name", "email")
    }

}