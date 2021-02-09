package com.robothy.exunion.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Subscribe {

    String exchange();

    String[] symbol();

}
