package org.example.worker.scheduled;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.example.store.dao.SendMailTaskDao;
import org.example.store.entities.SendMailTaskEntity;
import org.example.worker.service.MailClientApiService;
import org.example.worker.service.RedisLockService;
import org.example.worker.service.RedisLockWrapperService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendMailTaskScheduler {

    MailClientApiService mailClientApiService;

    SendMailTaskDao sendMailTaskDao;

    static String SEND_MAIL_TASK_KEY_FORMAT = "tiris:send:mail:task:%s";
    private final RedisLockWrapperService redisLockWrapperService;

    @Scheduled(fixedDelay = 5000)
    public void executeSendEmailTasks() {

        log.debug("Worker start execution.");

        List<Long> sendMailTaskIds = sendMailTaskDao.findNotProcessedIds();

        for (Long sendEmailTaskId: sendMailTaskIds) {

            String sendEmailTaskKey = getSendEmailTaskKey(sendEmailTaskId);

            redisLockWrapperService.lockAndExecuteTask(
                    sendEmailTaskKey,
                    Duration.ofSeconds(5),
                    () -> sendEmail(sendEmailTaskId)
            );
        }
    }

    private void sendEmail(Long sendEmailTaskId) {

        Optional<SendMailTaskEntity> optionalSendEmailTask = sendMailTaskDao
                .findNotProcessedById(sendEmailTaskId);

        if (optionalSendEmailTask.isEmpty()) {
            log.info("Task %d already processed.".formatted(sendEmailTaskId));
            return;
        }

        SendMailTaskEntity sendEmailTask = optionalSendEmailTask.get();

        String destinationEmail = sendEmailTask.getDestinationEmail();
        String message = sendEmailTask.getMessage();

        boolean delivered = mailClientApiService.sendMail(destinationEmail, message);

        if (delivered) {

            log.debug("Task %d processed.".formatted(sendEmailTask.getId()));
            sendMailTaskDao.markAsProcessed(sendEmailTask);

            return;
        }

        log.warn("Task %d returned to process.".formatted(sendEmailTask.getId()));
        sendMailTaskDao.updateLatestTryAt(sendEmailTask);
    }

    private static String getSendEmailTaskKey(Long taskId) {
        return SEND_MAIL_TASK_KEY_FORMAT.formatted(taskId);
    }
}
