package cl.tenpo.challenge.controller;

import cl.tenpo.challenge.service.DynamicPercentageCallHistoryService;
import cl.tenpo.challenge.service.PercentageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;



public class DynamicPercentageControllerTest {

    @InjectMocks
    private DynamicPercentageController dynamicPercentageController;

    @Mock
    private PercentageService percentageService;

    @Mock
    private DynamicPercentageCallHistoryService callHistoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDynamicPercentageSuccess() {
        double num1 = 10;
        double num2 = 20;
        double percentage = 10;

        when(percentageService.getPercentage(false)).thenReturn(percentage);

        ResponseEntity<String> response = dynamicPercentageController.dynamicPercentage(num1, num2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("33.0", response.getBody());
        verify(callHistoryService, times(1)).saveCallHistoryAsync(num1, num2, percentage, "33.0");
    }

    @Test
    public void testDynamicPercentageErrorFetchingPercentage() {
        double num1 = 10;
        double num2 = 20;

        when(percentageService.getPercentage(false)).thenThrow(new RuntimeException("Service unavailable"));

        ResponseEntity<String> response = dynamicPercentageController.dynamicPercentage(num1, num2);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(callHistoryService, never()).saveCallHistoryAsync(anyDouble(), anyDouble(), anyDouble(), anyString());
    }

    @Test
    public void testRateLimiterFallback() {
        double num1 = 10;
        double num2 = 20;
        Throwable ex = new RuntimeException("Rate limit exceeded");

        ResponseEntity<String> response = dynamicPercentageController.rateLimiterFallback(num1, num2, ex);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertEquals("Rate limit exceeded. Please try again later.", response.getBody());
        verify(callHistoryService, times(1)).saveCallHistoryAsync(num1, num2, 0.0, "ERROR 429 TOO_MANY_REQUESTS");
    }
}