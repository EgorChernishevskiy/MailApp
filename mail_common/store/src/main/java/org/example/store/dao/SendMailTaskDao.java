package org.example.store.dao;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.store.entities.SendMailTaskEntity;
import org.example.store.repositories.SendMailTaskRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class SendMailTaskDao {

    SendMailTaskRepository sendMailTaskRepository;

    private static final Duration TASK_EXECUTE_DURATION = Duration.ofSeconds(10);

    @Transactional
    public SendMailTaskEntity save(SendMailTaskEntity sendMailTaskEntity) {
        return sendMailTaskRepository.save(sendMailTaskEntity);
    }

    public List<Long> findNotProcessedIds() {

        Instant latestTryAtLte = Instant.now().minus(TASK_EXECUTE_DURATION);

        return sendMailTaskRepository.findAllNotProcessed(latestTryAtLte);
    }

    public Optional<SendMailTaskEntity> findNotProcessedById(Long id) {

        Instant latestTryAtLte = Instant.now().minus(TASK_EXECUTE_DURATION);

        return sendMailTaskRepository.findNotProcessedById(id, latestTryAtLte);
    }

    @Transactional
    public void markAsProcessed(SendMailTaskEntity sendMailTaskEntity) {
        sendMailTaskRepository.markAsProcessed(sendMailTaskEntity.getId());
    }

    @Transactional
    public void updateLatestTryAt(SendMailTaskEntity sendMailTaskEntity) {
       sendMailTaskRepository.updateLatestTryAt(sendMailTaskEntity.getId());
    }
}
