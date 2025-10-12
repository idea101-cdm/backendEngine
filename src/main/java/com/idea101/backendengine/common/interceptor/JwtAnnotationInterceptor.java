package com.idea101.backendengine.common.interceptor;

import com.idea101.backendengine.common.annotation.authentication.JwtProtected;
import com.idea101.backendengine.common.context.jwtUserContext;
import com.idea101.backendengine.common.enums.UserRole;
import com.idea101.backendengine.common.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.UUID;

@Log4j2
@Component
public class JwtAnnotationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public JwtAnnotationInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(!(handler instanceof HandlerMethod handlerMethod)) {
            return true; // not a controller method
        }

        // checks if method or class has @JwtProtected
        JwtProtected annotation = handlerMethod.getMethodAnnotation(JwtProtected.class);
        if (annotation == null) {
            annotation = handlerMethod.getBeanType().getAnnotation(JwtProtected.class);
        }

        if (annotation == null) {
            return true; // endpoint not protected
        }

        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header for endpoint '{}'", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return false;
        }

        String token = authorizationHeader.substring(7);
        if(!jwtUtil.validateToken(token)) {
            log.warn("Invalid or expired JWT token for endpoint '{}'", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Expired or Invalid Token");
            return false;
        }

        UserRole userRole = jwtUtil.extractRole(token);
        UserRole[] allowedRoles = annotation.rolesAllowed();
        if(allowedRoles.length > 0 && !Arrays.asList(allowedRoles).contains(userRole)) {
            log.warn("Access denied for user with role '{}' on endpoint '{}'", userRole, request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Role "+userRole+" is not allowed to access this resource");
            return false;
        }

        UUID id = jwtUtil.extractUserId(token);
        jwtUserContext context = new jwtUserContext(id, userRole);
        request.setAttribute("userContext", context);

        log.info("JWT validated successfully for user '{}' with role '{}' on endpoint '{}'", id, userRole, request.getRequestURI());

        return true;
    }
}
