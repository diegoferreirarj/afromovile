package br.com.movile.afromovile.product

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class ProductServiceIntegrationTest(
    @Autowired private val service: ProductService,
    @Autowired private val repository: ProductRepository
) {

    @BeforeEach
    fun beforeEach() {
        runBlocking {
            repository.deleteAll()

            val products = listOf(
                Product(name = "Product1", description = "Product 1"),
                Product(name = "Product2", description = "Product 2"),
                Product(name = "Product3", description = "Product 3")
            )

            repository.saveAll(products).count()
        }
    }

    @Test
    fun `findAll returns values`() {
        runBlocking {
            val result = service.findAll()

            assertEquals(result.count(), 3)
        }
    }

    @Test
    fun `findById returns a value`() {
        runBlocking {
            val product = service.findAll().firstOrNull()
            val result = service.findById(product?.id!!)

            assertNotNull(result)
            assertEquals(result?.id, product.id)
        }
    }

    @Test
    fun `findById returns a null if value does not exists`() {
        runBlocking {
            val result = service.findById(999)

            assertNull(result)
        }
    }

    @Test
    fun `addOne adds a user`() {
        runBlocking {
            val product = ProductDto(name = "product", description = "Product details")

            val result = service.save(product)

            assertNotNull(result)
            assertEquals(result.name, product.name)
            assertEquals(result.description, product.description)
        }
    }

    @Test
    fun `delete deletes inexisting user return false`() {
        runBlocking {
            val result = service.delete(9999)

            assertFalse(result)
        }
    }

}