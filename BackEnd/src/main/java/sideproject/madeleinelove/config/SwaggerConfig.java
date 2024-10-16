package sideproject.madeleinelove.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Madeleine Love Project API 명세서")
                .description("최악의 사랑, 최고의 사랑 post들을 작성하고 확인하는 서비스입니다")
                .version("v1.0.0-alpha");
    }
}