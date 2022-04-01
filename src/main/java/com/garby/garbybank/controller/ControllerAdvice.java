package com.garby.garbybank.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ControllerAdvice
class GlobalControllerExceptionHandler {
@ResponseStatus(HttpStatus.CONFLICT)
@ExceptionHandler(DataIntegrityViolationException.class)
public void handleException() {

  }
}