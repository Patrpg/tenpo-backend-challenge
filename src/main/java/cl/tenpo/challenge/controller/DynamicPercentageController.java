package cl.tenpo.challenge.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import cl.tenpo.challenge.service.DynamicPercentageCallHistoryService;
import cl.tenpo.challenge.service.PercentageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DynamicPercentageController {

    @Autowired
    private PercentageService percentageService;

    @Autowired
    private DynamicPercentageCallHistoryService callHistoryService;

    private static final Logger logger = LoggerFactory.getLogger(DynamicPercentageController.class);

    @PostMapping("/dynamicPercentage")
    @RateLimiter(name = "addRateLimiter", fallbackMethod = "rateLimiterFallback")
    public ResponseEntity<String> dynamicPercentage(@RequestParam double num1, @RequestParam double num2) {
        double percentage;
        try {
            percentage = percentageService.getPercentage(((num1 == 11) ? true : false));
        } catch (RuntimeException e) {
            logger.error("Error fetching percentage: " + e.getMessage());
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }

        double response = (num1 + num2) + (num1 + num2) * (percentage / 100);

        callHistoryService.saveCallHistoryAsync(num1, num2, percentage, String.valueOf(response));
        
        return new ResponseEntity<>(String.valueOf(response), HttpStatus.OK);
    }

    public ResponseEntity<String> rateLimiterFallback(double num1, double num2, Throwable ex) {
        logger.info("fallback:" +ex.getMessage());
        callHistoryService.saveCallHistoryAsync(num1, num2, 0.0, "ERROR 429 TOO_MANY_REQUESTS");
        return new ResponseEntity<String>("Rate limit exceeded. Please try again later.", HttpStatus.TOO_MANY_REQUESTS);
    }
}