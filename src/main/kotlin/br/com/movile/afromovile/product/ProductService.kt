package br.com.movile.afromovile.product

import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class ProductService(private val productRepository: ProductRepository) {

    suspend fun findAll() = productRepository.findAll().map { it.toDto() }
    suspend fun findById(id: Long) = productRepository.findById(id)?.toDto()
    suspend fun save(dto: ProductDto) = productRepository.save(dto.toEntity()).toDto()

    suspend fun delete(id: Long): Boolean {
        val exist = findById(id)

        return if (exist != null) {
            productRepository.deleteById(id)
            true
        } else false
    }

}