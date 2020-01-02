package com.github.edgar615.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记为虚拟列的注解.
 *
 * @author Edgar
 * @create 2018-09-06 15:02
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface VirtualKey {

}
