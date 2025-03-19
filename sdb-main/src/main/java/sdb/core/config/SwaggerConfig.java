package sdb.core.config;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sdb.core.model.error.ErrorResponse;

import java.util.Map;

import static io.swagger.v3.oas.models.media.Content.*;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("API Documentation").version("1.0"))
        .components(new Components()
            .addSchemas("ErrorResponse", createErrorResponseSchema())
            .addResponses("BadRequestResponse", createBadRequestResponse())
            .addResponses("InternalServerErrorResponse", createInternalServerErrorResponse())
            .addResponses("InvalidCredsResponse", createInvalidCredsResponse()));
  }

  private io.swagger.v3.oas.models.media.Schema<?> createErrorResponseSchema() {
    return new io.swagger.v3.oas.models.media.Schema<>()
        .type("object")
        .addProperty("errorCode", new io.swagger.v3.oas.models.media.StringSchema())
        .addProperty("message", new io.swagger.v3.oas.models.media.StringSchema());
  }

  private ApiResponse createInvalidCredsResponse() {
    Schema resultEntitySchema = ModelConverters.getInstance()
        .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class)).schema;
    return new ApiResponse()
        .description("Неверные логин или пароль")
        .content(
            new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                new MediaType()
                    .schema(resultEntitySchema.description("Schema 1"))
                    .addExamples("default",
                        new Example()
                            .value(new ErrorResponse("INVALID_CREDS", "Invalid login or password")))));
  }

  private ApiResponse createBadRequestResponse() {
    Schema resultEntitySchema = ModelConverters.getInstance()
        .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class)).schema;
    return new ApiResponse()
        .description("Некорректный запрос")
        .content(
            new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                new MediaType()
                    .schema(resultEntitySchema.description("Schema 1"))
                    .addExamples("default",
                        new Example()
                            .value(new ErrorResponse("BAD_REQUEST", "error message")))));
  }

  private ApiResponse createInternalServerErrorResponse() {
    Schema resultEntitySchema = ModelConverters.getInstance()
        .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class)).schema;
    return new ApiResponse()
        .description("Внутренняя ошибка сервера")
        .content(
            new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                new MediaType()
                    .schema(resultEntitySchema.description("Schema 1"))
                    .addExamples("default",
                        new Example()
                            .value(new ErrorResponse("UNKNOWN", "error message")))));
  }
}