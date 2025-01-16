package cl.tenpo.challenge.controller;

import cl.tenpo.challenge.model.DynamicPercentageCallHistory;
import cl.tenpo.challenge.service.DynamicPercentageCallHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;




public class DynamicPercentageCallHistoryControllerTest {

    @InjectMocks
    private DynamicPercentageCallHistoryController dynamicPercentageCallHistoryController;

    @Mock
    private DynamicPercentageCallHistoryService dynamicPercentageCallHistoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPaginatedDynamicPercentageCallHistory() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<DynamicPercentageCallHistory> page = new PageImpl<>(Collections.emptyList());
        when(dynamicPercentageCallHistoryService.getPaginatedDynamicPercentageCallHistory(pageable)).thenReturn(page);

        ResponseEntity<Page<DynamicPercentageCallHistory>> response = dynamicPercentageCallHistoryController.getPaginatedDynamicPercentageCallHistory(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
    }

    @Test
    public void testRateLimiterFallback() {
        ResponseEntity<Page<DynamicPercentageCallHistory>> response = dynamicPercentageCallHistoryController.rateLimiterFallback(0, 10, new RuntimeException());

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
    }
}