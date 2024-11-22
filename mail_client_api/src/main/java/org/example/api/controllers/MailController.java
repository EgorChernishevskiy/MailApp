package org.example.api.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.store.dao.SendMailTaskDao;
import org.example.store.entities.SendMailTaskEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailController {

    SendMailTaskDao sendMailTaskDao;

    private static final String SEND_MAIL = "/api/mail/send";

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(SEND_MAIL)
    public void sendMail(
            @RequestParam("destination_email") String destinationEmail,
            @RequestParam("message") String message
    ) {

        sendMailTaskDao.save(
                SendMailTaskEntity.builder()
                        .destinationEmail(destinationEmail)
                        .message(message)
                        .build()
        );
    }

}
