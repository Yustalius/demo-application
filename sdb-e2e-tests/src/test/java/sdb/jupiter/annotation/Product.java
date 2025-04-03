package sdb.jupiter.annotation;

import org.junit.jupiter.api.extension.ExtendWith;
import sdb.jupiter.extension.ProductExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(ProductExtension.class)
public @interface Product {
  String productName() default "";

  String description() default "";

  int price() default -1;

  boolean isAvailable() default true;

  int addToWarehouse() default -1;
}
