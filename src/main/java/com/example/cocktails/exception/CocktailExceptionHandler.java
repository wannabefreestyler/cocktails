package com.example.cocktails.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@ControllerAdvice
public class CocktailExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(CocktailExceptionHandler.class);

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
    log.error("500 Internal Server Error - {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("500: Internal Server Error");
  }

  @ExceptionHandler({HttpClientErrorException.class})
  public ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex,
                                                               WebRequest request) {
    log.error("400 Bad Request - {}",  ex.getStatusText());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("400: Bad Request");
  }

  @ExceptionHandler({NoResourceFoundException.class})
  public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex,
                                                               WebRequest request) {
    log.error("404 Not Found - {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("404: Not Found");
  }

}