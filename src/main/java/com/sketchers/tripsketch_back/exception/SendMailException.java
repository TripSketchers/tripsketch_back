package com.sketchers.tripsketch_back.exception;

public class SendMailException extends RuntimeException{
    public SendMailException(String message) {
        super(message);
    }
}
