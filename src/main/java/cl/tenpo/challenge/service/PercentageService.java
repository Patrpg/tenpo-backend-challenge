package cl.tenpo.challenge.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PercentageService {

    @Retryable(value = { RuntimeException.class }, maxAttempts = 3)
    @Cacheable(value = "percentageCache", key = "'percentage'")
    public double getPercentage() {
        return fetchPercentageFromExternalService(); 
    }

    private double fetchPercentageFromExternalService() {
        simulateDelay();
        
        if (Math.random() < 0.5) {
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
        return 10.0;
    }
}