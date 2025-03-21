package sdb.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityProperties {
  private Admin admin = new Admin();

  @Getter
  @Setter
  public static class Admin {
    private List<String> usernames;
  }
} 