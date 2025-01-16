package cl.tenpo.challenge.service;

import cl.tenpo.challenge.model.DynamicPercentageCallHistory;
import cl.tenpo.challenge.repository.jpa.DynamicPercentageCallHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DynamicPercentageCallHistoryServiceTest {

    @Mock
    private DynamicPercentageCallHistoryRepository repository;

    @InjectMocks
    private DynamicPercentageCallHistoryService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveCallHistory() {
        DynamicPercentageCallHistory callHistory = new DynamicPercentageCallHistory();
        callHistory.setNum1(1.0);
        callHistory.setNum2(2.0);
        callHistory.setPercentage(50.0);
        callHistory.setResponse("Success");

        when(repository.save(any(DynamicPercentageCallHistory.class))).thenReturn(callHistory);

        DynamicPercentageCallHistory result = service.saveCallHistory(1.0, 2.0, 50.0, "Success");

        assertNotNull(result);
        assertEquals(1.0, result.getNum1());
        assertEquals(2.0, result.getNum2());
        assertEquals(50.0, result.getPercentage());
        assertEquals("Success", result.getResponse());
        verify(repository, times(1)).save(any(DynamicPercentageCallHistory.class));
    }

    @Test
    public void testGetPaginatedDynamicPercentageCallHistory() {
        Pageable pageable = PageRequest.of(0, 10);
        DynamicPercentageCallHistory callHistory = new DynamicPercentageCallHistory();
        Page<DynamicPercentageCallHistory> page = new PageImpl<>(Collections.singletonList(callHistory));

        when(repository.findAll(pageable)).thenReturn(page);

        Page<DynamicPercentageCallHistory> result = service.getPaginatedDynamicPercentageCallHistory(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    public void testSaveCallHistoryAsync() throws ExecutionException, InterruptedException {
        DynamicPercentageCallHistory callHistory = new DynamicPercentageCallHistory();
        callHistory.setNum1(1.0);
        callHistory.setNum2(2.0);
        callHistory.setPercentage(50.0);
        callHistory.setResponse("Success");

        when(repository.save(any(DynamicPercentageCallHistory.class))).thenReturn(callHistory);

        CompletableFuture<DynamicPercentageCallHistory> future = service.saveCallHistoryAsync(1.0, 2.0, 50.0, "Success");

        assertNotNull(future);
        DynamicPercentageCallHistory result = future.get();
        assertNotNull(result);
        assertEquals(1.0, result.getNum1());
        assertEquals(2.0, result.getNum2());
        assertEquals(50.0, result.getPercentage());
        assertEquals("Success", result.getResponse());
        verify(repository, times(1)).save(any(DynamicPercentageCallHistory.class));
    }
}