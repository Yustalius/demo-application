package sdb.jupiter.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import sdb.jupiter.extension.OrderExtension;
import sdb.jupiter.extension.UserExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith({
    UserExtension.class,
    OrderExtension.class
})
public @interface User {
  String username() default "";

  String password() default "";

  String firstname() default "";

  String lastName() default "";

  int age() default 0;

  Order[] orders() default {};
}
