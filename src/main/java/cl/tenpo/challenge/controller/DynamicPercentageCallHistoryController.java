package cl.tenpo.challenge.controller;

import cl.tenpo.challenge.model.DynamicPercentageCallHistory;
import cl.tenpo.challenge.service.DynamicPercentageCallHistoryService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class DynamicPercentageCallHistoryController {

    @Autowired
    private DynamicPercentageCallHistoryService dynamicPercentageCallHistoryService;

    @GetMapping("/dynamic-percentage-call-history")
    @RateLimiter(name = "historyRateLimiter", fallbackMethod = "rateLimiterFallback")
    public ResponseEntity<Page<DynamicPercentageCallHistory>> getPaginatedDynamicPercentageCallHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DynamicPercentageCallHistory> data = dynamicPercentageCallHistoryService.getPaginatedDynamicPercentageCallHistory(pageable);
        ResponseEntity<Page<DynamicPercentageCallHistory>> response = new ResponseEntity<>(data, HttpStatus.OK);
        return response;
    }

    public ResponseEntity<Page<DynamicPercentageCallHistory>> rateLimiterFallback(int page, int size, Throwable ex) {
        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }
}