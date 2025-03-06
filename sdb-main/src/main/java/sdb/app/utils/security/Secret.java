package sdb.app.utils.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class Secret {
  @Bean
  public SecretKey secretKey() {
    String secret = "9fcc7c49e0a14e6d3e3c6a5b2d1f8e7d9a0b8c7d6e5f4a3b2c1d0e9f8a7b6";
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }
}
