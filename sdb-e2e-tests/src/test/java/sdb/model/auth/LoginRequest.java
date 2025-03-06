package sdb.model.auth;

public record LoginRequest(
    String username,
    String password
) {
}
