package com.melilogin.demo.common.api.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

public class ApiDaoBase {
    private static final String APPLICATION_JSON_HEADER = "application/json";
    private static final String RESPONSE_LOG_FORMAT = "RESPONSE Status: %s  Body: %s";
    private static final String REQUEST_LOG_FORMAT = "REQUEST Method: %s  Request: %s  Body: %s";
    private static final String REQUEST_LOG_NO_ENTITY_FORMAT = "REQUEST Method: %s  Request: %s";
    private static final Logger log = LoggerFactory.getLogger(ApiDaoBase.class);

    private final RestTemplate restTemplate;

    public ApiDaoBase(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        // interceptors.add(new LoggingRequestInterceptor());
    }

    protected <T> T getForObject(String url, Class<T> responseType, Map<String, ?> urlVariables) {
        try {
            return restTemplate.getForObject(url, responseType, urlVariables);
        } catch (HttpStatusCodeException e) {
            throw e;
        }
    }

    protected <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) {
        try {
            return restTemplate.exchange(url, method, requestEntity, responseType);
        } catch (HttpStatusCodeException e) {
            throw e;
        }
    }

    protected <T> T postForObject(URI url, Object request, Class<T> responseType) {
        try {
            return restTemplate.postForObject(url, request, responseType);
        } catch (HttpStatusCodeException e) {
            throw e;
        }
    }

    protected <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> urlVariables) {
        try {
            return restTemplate.postForObject(url, request, responseType, urlVariables);
        } catch (HttpStatusCodeException e) {
            log.debug(String.format("Response Status Code: %s  Status Text: %s  Message: %s  Body:  %s", e.getStatusCode(), e.getStatusText(), e.getMessage(),
                    e.getResponseBodyAsString()));
            throw e;
        }
    }

    protected URI postForLocation(String url, Object request) {
        try {
            return restTemplate.postForLocation(url, request);
        } catch (HttpStatusCodeException e) {
            throw e;
        }
    }

    protected void addMessageConverter(HttpMessageConverter<?> messageConverter) {
        restTemplate.getMessageConverters().add(messageConverter);
    }

    private int checkStatusCode(int statusCode) {// throws HttpStatusException, HttpAuthenticationException {
        log.debug("RESPONSE status code = " + statusCode);
        if (statusCode != HttpStatus.OK.value() && statusCode != HttpStatus.CREATED.value()) {
            if (statusCode == HttpStatus.SERVICE_UNAVAILABLE.value() || statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()
                    || statusCode == HttpStatus.BAD_GATEWAY.value()) {
                log.warn("The Mercado libre API is unavailable. StatusCode " + statusCode);
                // throw new HttpServiceUnavailable(HttpError.SERVICE_UNAVAILABLE);
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                log.info("unauthorized Access Token to the Mercado Libre API");
                // throw new HttpAuthenticationException(HttpError.UNAUTHORIZED_MERCADO_LIBRE);
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                log.error("Bad Request to the Mercado Libre API");
                // throw new HttpAuthenticationException(HttpError.UNAUTHORIZED_MERCADO_LIBRE);
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // throw new HttpContentNotFoundException(HttpError.CONTENT_NOT_FOUND);
            } else {
                log.error(String.format("There has been an error with Mercado Libre API. StatusCode: %s. Response: %s", statusCode, statusCode));
                // throw new HttpAuthenticationException(HttpError.UNAUTHORIZED_MERCADO_LIBRE);
            }
        }
        return statusCode;
    }

    private <T> T parseResponse(Class<T> clazz, int statusCode, String responseBody) {// throws HttpStatusException {
        return parseResponse(clazz, statusCode, responseBody, null);
    }

    private <T> T parseResponse(Class<T> clazz, int statusCode, String responseBody, String dateFormat) {// throws HttpStatusException {
        T entity = null;
//        try {
            log.debug(format(RESPONSE_LOG_FORMAT, statusCode, responseBody));

//            Gson gson;
//            if (dateFormat != null) {
//                gson = new GsonBuilder().setDateFormat(dateFormat).create();
//            } else {
//                gson = new Gson();
//            }

//            entity = gson.fromJson(responseBody, clazz);
//        } catch (Exception jse) { //JsonSyntaxException 
            log.error("RESPONSE status code = " + statusCode);
            log.error(format("RESPONSE Status: %s", statusCode, responseBody));
            // throw new HttpServiceUnavailable(HttpError.SERVICE_UNAVAILABLE);
//        }
        return entity;
    }
}
