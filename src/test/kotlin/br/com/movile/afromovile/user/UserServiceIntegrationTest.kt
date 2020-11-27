package br.com.movile.afromovile.user

import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class UserServiceIntegrationTest(
    @Autowired private val service: UserService,
    @Autowired private val repository: UserRepository
) {

    @BeforeEach
    fun beforeEach() {
        repository.deleteAll().subscribe()

        val users = listOf(
            User(name = "User1", email = "user1@users.com"),
            User(name = "User2", email = "user2@users.com"),
            User(name = "User3", email = "user3@users.com")
        )

        repository.saveAll(users).subscribe()
    }

    @Test
    fun `findAll returns values`() {
        runBlocking {
            val result = service.findAll().collectList().block()

            assertEquals(result?.size, 3)
        }
    }

    @Test
    fun `findById returns a value`() {
        runBlocking {
            val dbUser = service.findAll().awaitFirst()
            val result = service.findById(dbUser.id!!).awaitFirst()

            assertNotNull(result)
            assertEquals(result.id, dbUser.id)
        }
    }

    @Test
    fun `findById returns a null if value does not exists`() {
        runBlocking {
            val result = service.findById(999).awaitFirstOrNull()

            assertNull(result)
        }
    }

    @Test
    fun `addOne adds a user`() {
        runBlocking {
            val user = UserDto(name = "newuser", email = "newuser@users.com")

            val result = service.save(user).awaitFirst()

            assertNotNull(result)
            assertEquals(result.name, user.name)
            assertEquals(result.email, user.email)
        }
    }

    @Test
    fun `delete deletes inexisting user return false`() {
        runBlocking {
            assertDoesNotThrow {
                service.delete(9999)
            }
        }
    }

}