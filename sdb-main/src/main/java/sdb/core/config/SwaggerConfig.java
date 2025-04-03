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
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sdb.core.model.error.ErrorResponse;

import static sdb.core.model.order.ErrorCode.*;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("API Documentation").version("1.0"))
        .components(new Components()
            .addSchemas("ErrorResponse", createErrorResponseSchema())
            .addResponses("BadRequestResponse", createBadRequestResponse())
            .addResponses("NotAuthorizedResponse", createNotAuthorizedResponse())
            .addResponses("InternalServerErrorResponse", createInternalServerErrorResponse())
            .addResponses("InvalidCredsResponse", createInvalidCredsResponse())
            .addResponses("PermissionDeniedResponse", createPermissionDeniedResponse())
            .addResponses("UserNotFoundResponse", createUserNotFoundResponse())
            .addResponses("ProductNotAvailableResponse", createProductNotAvailableResponse())
            .addResponses("OrderNotFoundResponse", createOrderNotFoundResponse())
            .addResponses("StatusTransitionErrorResponse", createStatusTransitionErrorResponse()));  
  }

  private Schema<?> createErrorResponseSchema() {
    return new Schema<>()
                .type("object")
        .addProperty("errorCode", new StringSchema())
        .addProperty("message", new StringSchema());
  }

  private ApiResponse createNotAuthorizedResponse() {
    Schema resultEntitySchema = ModelConverters.getInstance()
        .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class)).schema;
    return new ApiResponse()
        .description("Требуется аутентификация")
        .content(
            new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                new MediaType()
                    .schema(resultEntitySchema.description("Schema 1"))
                    .addExamples("example",
                        new Example()
                            .value(new ErrorResponse(NOT_AUTHORIZED, "Authentication required")))));
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
                    .addExamples("example",
                        new Example()
                            .value(new ErrorResponse(INVALID_CREDS, "Invalid login or password")))));
  }

  private ApiResponse createPermissionDeniedResponse() {
    Schema resultEntitySchema = ModelConverters.getInstance()
        .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class)).schema;
    return new ApiResponse()
        .description("Нет прав на выполнение операции")
        .content(
            new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                new MediaType()
                    .schema(resultEntitySchema.description("Schema 1"))
                    .addExamples("example",
                        new Example()
                            .value(new ErrorResponse(PERMISSION_DENIED, "Permission denied")))));
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
                    .addExamples("example",
                        new Example()
                            .value(new ErrorResponse(BAD_REQUEST, "error message")))));
  }

  private ApiResponse createUserNotFoundResponse() {
    Schema resultEntitySchema = ModelConverters.getInstance()
        .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class)).schema;
    return new ApiResponse()
        .description("Пользователь не найден")
        .content(
            new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                new MediaType()
                    .schema(resultEntitySchema.description("Schema 1"))
                    .addExamples("example",
                        new Example()
                            .value(new ErrorResponse(USER_NOT_FOUND, "error message")))));
  }

  private ApiResponse createProductNotAvailableResponse() {
    Schema resultEntitySchema = ModelConverters.getInstance()
        .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class)).schema;
    return new ApiResponse()
        .description("Продукт недоступен для покупки")
        .content(
            new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                new MediaType()
                    .schema(resultEntitySchema.description("Schema 1"))
                    .addExamples("example",
                        new Example()
                            .value(new ErrorResponse(PRODUCT_NOT_AVAILABLE, "error message")))));
  }

  private ApiResponse createOrderNotFoundResponse() {
    Schema resultEntitySchema = ModelConverters.getInstance()
        .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class)).schema;
    return new ApiResponse()
        .description("Заказ не найден")
        .content(
            new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                new MediaType()
                    .schema(resultEntitySchema.description("Schema 1"))
                    .addExamples("example",
                        new Example()
                            .value(new ErrorResponse(ORDER_NOT_FOUND, "error message")))));
  }

  private ApiResponse createProductNotFoundResponse() {
    Schema resultEntitySchema = ModelConverters.getInstance()
        .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class)).schema;
    return new ApiResponse()
        .description("Продукт не найден")
        .content(
            new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                new MediaType()
                    .schema(resultEntitySchema.description("Schema 1"))
                    .addExamples("example",
                        new Example()
                            .value(new ErrorResponse(PRODUCT_NOT_FOUND, "error message")))));
  }

  private ApiResponse createStatusTransitionErrorResponse() {
    Schema resultEntitySchema = ModelConverters.getInstance()
        .resolveAsResolvedSchema(new AnnotatedType(ErrorResponse.class)).schema;
    return new ApiResponse()
        .description("Невозможно перейти в данный статус")
        .content(
            new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                new MediaType()
                    .schema(resultEntitySchema.description("Schema 1"))
                    .addExamples("example",
                        new Example()
                            .value(new ErrorResponse(STATUS_TRANSITION_ERROR, "error message")))));
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
                    .addExamples("example",
                        new Example()
                            .value(new ErrorResponse(UNKNOWN, "error message")))));
  }
}