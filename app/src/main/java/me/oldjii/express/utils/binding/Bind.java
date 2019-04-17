package me.oldjii.express.utils.binding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 这里Bind不是接口，而是一个Annotation
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)     //RetentionPolicy.RUNTIME 可以让你从JVM中读取Annotation注解的信息，以便在分析程序的时候使用
public @interface Bind {
    int value();
}
