package br.com.movile.afromovile

import br.com.movile.afromovile.product.ProductHandler
import br.com.movile.afromovile.user.UserHandler
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.router

@Configuration
class ApplicationConfiguration {

    @Bean
    fun userRouter(userHandler: UserHandler) = router {
        GET("/users", userHandler::findAll)
        GET("/users/{id}", userHandler::findById)
        POST("/users", userHandler::save)
        DELETE("/users/{id}", userHandler::delete)
    }

    @Bean
    fun productRouter(productHandler: ProductHandler) = coRouter {
        GET("/products", productHandler::findAll)
        GET("/products/{id}", productHandler::findById)
        POST("/products", productHandler::save)
        DELETE("/products/{id}", productHandler::delete)
    }

    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer? {
        val populator = CompositeDatabasePopulator()
        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("schema.sql")))

        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory)
        initializer.setDatabasePopulator(populator)

        return initializer
    }

}
