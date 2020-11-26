package br.com.movile.afromovile.product

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("products")
data class Product(
    @Id val id: Long? = null,
    val name: String,
    val description: String
)
