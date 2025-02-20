package com.example.demo.model;

import jakarta.validation.constraints.*;

public record HelloRequest(
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name cannot be bigger than 50 symbols")
    String name,
    @Min(value = 0, message = "Age can't be less than 0")
    @Max(value = 999, message = "Age can't be bigger than 999")
    @NotNull
    int age
) {
}
