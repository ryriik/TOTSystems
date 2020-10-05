package ru.totsystems.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "History not found")
public class HistoryNotFoundException extends RuntimeException {
}
