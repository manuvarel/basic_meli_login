package com.melilogin.demo.webservices.exceptions;

public class HttpStatusException extends Exception {
    private static final long serialVersionUID = -8688416654466199073L;
    private HttpError httpError;

    public HttpStatusException(HttpError httpError) {
        super(httpError.getMessageKey());
        this.httpError = httpError;
    }

    public HttpError getHttpError() {
        return httpError;
    }

}
