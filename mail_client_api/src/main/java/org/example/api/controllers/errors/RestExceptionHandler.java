package org.example.api.controllers.errors;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.example.data.dto.ErrorDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception ex, WebRequest request) throws Exception {

        log.error("Exception during execution of application", ex);

        return handleException(ex, request);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException e,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request)
    {

        String message = String.format("Parameter \"%s\" is missing", e.getParameterName());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorDto.builder()
                                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                .errorDescription(message)
                                .build()
                );
    }
}