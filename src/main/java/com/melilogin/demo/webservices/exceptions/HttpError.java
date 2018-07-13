package com.melilogin.demo.webservices.exceptions;

public enum HttpError {
    BAD_REQUEST_HEADER("mailing.field.missing"),
    METHOD_NOT_SUPPORTED("mailing.method.not.supported"),
    SERVICE_UNAVAILABLE("mailing.api.service.unavailable");

    private String messageKey;

    private HttpError(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }

}
