package com.idea101.backendengine.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProblemDetailService {
    public ProblemDetail create(HttpStatus status, String title, String detail) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setProperty("timestamp", LocalDateTime.now());
        return problem;
    }
}
