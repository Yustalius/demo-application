package sdb.jupiter.extension;

import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sdb.config.Config;
import sdb.data.entity.auth.RegisterEntity;
import sdb.jupiter.annotation.User;
import sdb.model.TestData;
import sdb.model.user.UserDTO;
import sdb.service.impl.AuthDbClient;

import static sdb.data.Databases.dataSource;
import static sdb.utils.RandomUtils.*;

public class UserExtension implements BeforeEachCallback, ParameterResolver {
  private static final Config CFG = Config.getInstance();
  public static final Namespace NAMESPACE = Namespace.create(UserExtension.class);

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(userAnno -> {
          AuthDbClient authClient = new AuthDbClient(dataSource(CFG.coreDbUrl()));
          PasswordEncoder encoder = new BCryptPasswordEncoder();

          String password = "".equals(userAnno.password()) ? randomPassword() : userAnno.password();

          RegisterEntity entity = new RegisterEntity();
          entity.setUsername("".equals(userAnno.username()) ? randomUsername() : userAnno.username());
          entity.setPassword(encoder.encode(password));
          entity.setFirstName("".equals(userAnno.firstname()) ? randomName() : userAnno.firstname());
          entity.setLastName("".equals(userAnno.lastName()) ? randomLastName() : userAnno.lastName());
          entity.setAge(userAnno.age() == 0 ? randomAge() : userAnno.age());
          UserDTO user = authClient.createNewUser(entity);

          context.getStore(NAMESPACE).put(
              context.getUniqueId(),
              user.addTestData(
                  new TestData(
                      entity.getUsername(),
                      password
                  ))
          );
        });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(UserDTO.class);
  }

  @Override
  public UserDTO resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(
        extensionContext.getUniqueId(),
        UserDTO.class
    );
  }
}
