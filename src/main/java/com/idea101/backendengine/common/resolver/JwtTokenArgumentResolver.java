package com.idea101.backendengine.common.resolver;

import com.idea101.backendengine.common.annotation.authentication.jwtUser;
import com.idea101.backendengine.common.context.jwtUserContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class JwtTokenArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.hasParameterAnnotation(jwtUser.class) && parameter.getParameterType().equals(jwtUserContext.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        if (parameter.hasParameterAnnotation(jwtUser.class)) {
            return webRequest.getAttribute("userContext", RequestAttributes.SCOPE_REQUEST);
        }

        return null;
    }
}
