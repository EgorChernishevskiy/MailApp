package org.example.store.repositories;

import org.example.store.entities.SendMailTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SendMailTaskRepository extends JpaRepository<SendMailTaskEntity, Long> {

    @Query("""
        SELECT task.id
        FROM SendMailTaskEntity task
        WHERE task.processedAt IS NULL
            AND (task.latestTryAt IS NULL OR task.latestTryAt <= :latestTryAtLte)
        ORDER BY task.createdAt
    """)
    List<Long> findAllNotProcessed(Instant latestTryAtLte);

    @Query("""
        SELECT task
        FROM SendMailTaskEntity task
        WHERE task.id = :id
            AND task.processedAt IS NULL
            AND (task.latestTryAt IS NULL OR task.latestTryAt <= :latestTryAtLte)
    """)
    Optional<SendMailTaskEntity> findNotProcessedById(Long id, Instant latestTryAtLte);

    @Modifying
    @Query("""
        UPDATE SendMailTaskEntity task
        SET task.processedAt = CURRENT_TIMESTAMP
        WHERE task.id = :id
    """)
    void markAsProcessed(Long id);

    @Modifying
    @Query("""
        UPDATE SendMailTaskEntity task
        SET task.latestTryAt = CURRENT_TIMESTAMP
        WHERE task.id = :id
    """)
    void updateLatestTryAt(Long id);
}
