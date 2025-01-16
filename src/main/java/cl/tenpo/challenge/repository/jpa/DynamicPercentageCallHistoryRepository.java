package cl.tenpo.challenge.repository.jpa;

import cl.tenpo.challenge.model.DynamicPercentageCallHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DynamicPercentageCallHistoryRepository extends JpaRepository<DynamicPercentageCallHistory, Long> {
}