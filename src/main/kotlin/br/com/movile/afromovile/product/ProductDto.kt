package br.com.movile.afromovile.product

import org.springframework.data.annotation.Id

data class ProductDto(
    @Id val id: Long? = null,
    val name: String,
    val description: String
)

fun ProductDto.toEntity() = Product(this.id, this.name, this.description)

fun Product.toDto() = ProductDto(this.id, this.name, this.description)
