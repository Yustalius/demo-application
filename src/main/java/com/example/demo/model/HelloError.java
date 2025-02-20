package com.example.demo.model;

import java.util.List;

public record HelloError(
    List<String> errorMessages
) {
}
