package cl.tenpo.challenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PercentageService {

    private static final Logger logger = LoggerFactory.getLogger(PercentageService.class);

    @Autowired
    private CacheManager cacheManager;

    @Retryable(value = { Exception.class }, maxAttempts = 3)
    @Cacheable(value = "percentageCache", key = "'percentage'")
    public double getPercentage(Boolean should_fail) {
        logger.info("getPercentage was called");
        return fetchPercentageFromExternalService(should_fail); 
    }

    public Double getCachedPercentage() {
        Double cachedPercentage = cacheManager.getCache("percentageCache").get("percentage", Double.class);
        if (cachedPercentage == null) {
            logger.warn("No value found in cache for key 'percentage'");
        }
        return cachedPercentage;
    }

    private double fetchPercentageFromExternalService(Boolean should_fail) {
        simulateDelay();
        if (should_fail) {
            throw new RuntimeException("Error fetching percentage from external service");
        }
        return 10.0;
    }

    private void simulateDelay() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // This method will be called when retries are exhausted
    @Recover
    public double recover(RuntimeException e) {
        Double cachedPercentage = cacheManager.getCache("percentageCache").get("percentage", Double.class);
        if (cachedPercentage == null) {
            throw new RuntimeException("Error fetching percentage from external service");
        }
        return cachedPercentage;
    }

    @CacheEvict(value = "percentageCache", allEntries = true)
    @Scheduled(fixedRate = 1800000) // 30 minutes in milliseconds
    public void emptyPercentageCache() {
        logger.info("Emptying percentage cache");
    }
}