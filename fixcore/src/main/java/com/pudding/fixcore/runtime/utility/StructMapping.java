package com.pudding.fixcore.runtime.utility;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * Annotation to mark a struct member.
 *
 * @author Pudding
 * @version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StructMapping {
    int offset() default -1;
    int length() default -1;
}
