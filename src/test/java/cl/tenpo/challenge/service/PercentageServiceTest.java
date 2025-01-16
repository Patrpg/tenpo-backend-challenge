package cl.tenpo.challenge.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PercentageServiceTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @InjectMocks
    private PercentageService percentageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cacheManager.getCache("percentageCache")).thenReturn(cache);
    }

    @Test
    public void testGetPercentageSuccess() {
        double result = percentageService.getPercentage(false);
        assertEquals(10.0, result);
    }

    @Test
    public void testGetPercentageFailure() {
        assertThrows(RuntimeException.class, () -> {
            percentageService.getPercentage(true);
        });
    }

    @Test
    public void testGetCachedPercentage() {
        when(cache.get("percentage", Double.class)).thenReturn(10.0);
        Double result = percentageService.getCachedPercentage();
        assertNotNull(result);
        assertEquals(10.0, result);
    }

    @Test
    public void testGetCachedPercentageNull() {
        when(cache.get("percentage", Double.class)).thenReturn(null);
        Double result = percentageService.getCachedPercentage();
        assertNull(result);
    }

    @Test
    public void testRecoverWithCachedValue() {
        when(cache.get("percentage", Double.class)).thenReturn(10.0);
        double result = percentageService.recover(new RuntimeException("Test exception"));
        assertEquals(10.0, result);
    }

    @Test
    public void testRecoverWithoutCachedValue() {
        when(cache.get("percentage", Double.class)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> {
            percentageService.recover(new RuntimeException("Test exception"));
        });
    }

}