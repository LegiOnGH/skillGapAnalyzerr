package com.project.skillGapAnalyzer.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.skillGapAnalyzer.dto.response.ErrorResponseDTO;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public RateLimitFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private Bucket createBucket(){
        Bandwidth limit = Bandwidth.builder()
                .capacity(10)
                .refillGreedy(10, Duration.ofMinutes(1))
                .build();
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket getBucket(String key){
        return buckets.computeIfAbsent(key, k -> createBucket());
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // only rate limit these endpoints
        if (!path.equals("/analysis") && !path.equals("/repos/recommend")) {
            filterChain.doFilter(request, response);
            return;
        }

        // use IP + path as key
        String ip = request.getRemoteAddr();
        String key = ip + ":" + path;
        Bucket bucket = getBucket(key);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            ErrorResponseDTO error = new ErrorResponseDTO(
                    "Too many requests. Please wait a minute before trying again.",
                    HttpStatus.TOO_MANY_REQUESTS,
                    Instant.now()
            );
            objectMapper.writeValue(response.getOutputStream(), error);
        }
    }
}
