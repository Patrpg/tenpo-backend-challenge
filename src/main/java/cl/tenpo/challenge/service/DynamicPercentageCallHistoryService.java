package cl.tenpo.challenge.service;

import cl.tenpo.challenge.model.DynamicPercentageCallHistory;
import cl.tenpo.challenge.repository.jpa.DynamicPercentageCallHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.concurrent.CompletableFuture;


@Service
public class DynamicPercentageCallHistoryService {

    @Autowired
    private DynamicPercentageCallHistoryRepository repository;

    public DynamicPercentageCallHistory saveCallHistory(double num1, double num2, double percentage, String response) {
        DynamicPercentageCallHistory callHistory = new DynamicPercentageCallHistory();
        callHistory.setNum1(num1);
        callHistory.setNum2(num2);
        callHistory.setPercentage(percentage);
        callHistory.setResponse(response);
        callHistory.setTimestamp(LocalDateTime.now());
        return repository.save(callHistory);
    }

    public Page<DynamicPercentageCallHistory> getPaginatedDynamicPercentageCallHistory(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public CompletableFuture<DynamicPercentageCallHistory> saveCallHistoryAsync(double num1, double num2, double percentage, String response) {
        return CompletableFuture.supplyAsync(() -> saveCallHistory(num1, num2, percentage, response));
    }

}
