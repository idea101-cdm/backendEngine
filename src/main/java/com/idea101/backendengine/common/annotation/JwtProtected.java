package com.idea101.backendengine.common.annotation;

import com.idea101.backendengine.common.enums.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtProtected {
    UserRole[] rolesAllowed() default {};
}
