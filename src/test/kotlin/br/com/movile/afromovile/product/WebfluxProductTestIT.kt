package br.com.movile.afromovile.product

import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
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
internal class WebfluxProductTestIT(
    @Autowired private val client: WebTestClient,
    @Autowired private val productRepository: ProductRepository
) {

    companion object {
        private val PRODUCTS = listOf(
            Product(name = "Test no 1", description = "Product Test no 1"),
            Product(name = "Test no 2", description = "Product Test no 2"),
            Product(name = "Test no 3", description = "Product Test no 3"),
            Product(name = "Test no 4", description = "Product Test no 4")
        )
    }

    init {
        runBlocking {
            productRepository.saveAll(PRODUCTS).count()
        }
    }

    @Test
    fun `list of users`() {
        val products = mutableListOf<Product>()
        runBlocking { productRepository.findAll().toList(products) }

        val response = client.get()
                .uri("/products")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBodyList<ProductDto>()
                .hasSize(products.size)
                .returnResult()
                .responseBody

        assertThat(response)
                .isNotNull
                .allSatisfy { assertThat(it).isIn(products.map(Product::toDto)) }
    }

    @Test
    fun `existing user returns OK`() {
        val expectedProduct = runBlocking { productRepository.findAll().first().toDto() }

        assertThat(expectedProduct).isNotNull

        val response = client.get()
                .uri("/products/${expectedProduct.id}")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody(ProductDto::class.java)
                .returnResult()
                .responseBody

        assertThat(response)
                .isNotNull
                .isEqualTo(expectedProduct)
    }

    @Test
    fun `returns OK`() {
        val productDto = ProductDto(name = "New Product", description = "New")

        val response = client.post()
                .uri("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(productDto))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated
                .expectBody(ProductDto::class.java)
                .returnResult()
                .responseBody

        assertThat(response)
                .isNotNull
                .usingRecursiveComparison()
                .ignoringFields("id")
    }

}