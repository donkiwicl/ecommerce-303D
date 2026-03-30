package dev.rampmaster.ecommerce.cart.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cartOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cart API")
                        .description("Documentacion del microservicio de carrito de compras")
                        .version("v1"))
                .addServersItem(new Server()
                        .url("/cart")
                        .description("Context path del microservicio cart"));
    }
}
