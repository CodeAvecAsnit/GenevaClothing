package com.ecomm.np.genevaecommerce.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author : Asnit Bakhati
 */
@Component
@Order(1)
public class APIRateLimitingFilter extends OncePerRequestFilter {

    private Logger log = LoggerFactory.getLogger(APIRateLimitingFilter.class);

    private Cache<String, Bucket> bucketCache;
    private Cache<String, Long> prisonCache;

    private static final int MAX_REQUESTS_PER_MINUTE = 20;
    private static final long PRISON_DURATION_HOURS = 1;

    public APIRateLimitingFilter() {
    }

    @PostConstruct
    public void init() {
        this.bucketCache = Caffeine.newBuilder()
                .expireAfterAccess(2, TimeUnit.MINUTES)
                .maximumSize(10000)
                .build();
        this.prisonCache = Caffeine.newBuilder()
                .expireAfterWrite(PRISON_DURATION_HOURS, TimeUnit.HOURS)
                .maximumSize(10000)
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.debug("Rate limit filter triggered for: {}", request.getRequestURI());

        String userIP = getClientIPAddress(request);

        Long prisonTime = prisonCache.getIfPresent(userIP);
        if (prisonTime != null) {
            long elapsedMinutes = (System.currentTimeMillis() - prisonTime) / (1000 * 60);
            long remainingMinutes = (PRISON_DURATION_HOURS * 60) - elapsedMinutes;

            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                    "{\"error\": \"Too many requests. IP blocked. Try again in %d minutes.\"}",
                    Math.max(0, remainingMinutes)
            ));
            return;
        }

        Bucket bucket = bucketCache.get(userIP, this::createNewBucket);
        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            sendToPrison(userIP);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(String.format(
                    "{\"error\": \"Rate limit exceeded. IP blocked for %d hour(s).\"}",
                    PRISON_DURATION_HOURS
            ));
        }
    }

    /**
     * Creates a new token bucket with configured limits
     */
    private Bucket createNewBucket(String ip) {
        log.debug("Creating new rate limit bucket for IP: {}", ip);
        Bandwidth limit = Bandwidth.builder().capacity(MAX_REQUESTS_PER_MINUTE)
                .refillGreedy(MAX_REQUESTS_PER_MINUTE, Duration.ofMinutes(1)).build();
        return Bucket.builder().addLimit(limit).build();
    }

    /**
     * Sends IP to prison
     */
    private void sendToPrison(String ipAddress) {
        log.warn("IP address sent to prison for rate limit violation: {}", ipAddress);
        prisonCache.put(ipAddress, System.currentTimeMillis());
        bucketCache.invalidate(ipAddress);
    }

    /**
     * Extracts client IP address from request, checking proxy headers
     */
    private String getClientIPAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }

    /**
     * Check if IP is currently in prison (for testing)
     */
    public boolean isInPrison(String ipAddress) {
        return prisonCache.getIfPresent(ipAddress) != null;
    }

    /**
     * Get remaining prison time in minutes
     */
    public long getPrisonRemainingMinutes(String ipAddress) {
        Long prisonTime = prisonCache.getIfPresent(ipAddress);
        if (prisonTime == null) {
            return 0;
        }
        long elapsedMinutes = (System.currentTimeMillis() - prisonTime) / (1000 * 60);
        return Math.max(0, (PRISON_DURATION_HOURS * 60) - elapsedMinutes);
    }

    public void releaseFromPrison(String ipAddress) {
        log.info("Manually releasing IP from prison: {}", ipAddress);
        prisonCache.invalidate(ipAddress);
        bucketCache.invalidate(ipAddress);
    }
}