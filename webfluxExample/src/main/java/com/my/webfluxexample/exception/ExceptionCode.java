package com.my.webfluxexample.exception;

import lombok.Getter;

public enum ExceptionCode {
    BOOK_NOT_FOUND(404,"Book not found"),
    BOOK_EXISTS(409,"Book already exists");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
