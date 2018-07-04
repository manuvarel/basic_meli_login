package com.melilogin.demo.common.api.dao;

import static java.lang.String.format;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

public class BaseApiDAOImpl {

    private static final String APPLICATION_JSON_HEADER = "application/json";
    private static final String RESPONSE_LOG_FORMAT = "RESPONSE Status: %s  Body: %s";
    private static final String REQUEST_LOG_FORMAT = "REQUEST Method: %s  Request: %s  Body: %s";
    private static final String REQUEST_LOG_NO_ENTITY_FORMAT = "REQUEST Method: %s  Request: %s";

    private static final Logger log = Logger.getLogger(BaseApiDAOImpl.class);

    private static final String UTF_8 = "UTF-8";

    @Autowired
    private HttpClient httpClient;
    
//    @Autowired
//    private Gson gson;

//    public BaseApiDAOImpl() {
//        httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager).setConnectionManagerShared(true).build();
//    }

//    @Autowired
//    private PoolingHttpClientConnectionManager connectionManager;

    /**
     * Used for GET operations
     */
    protected void addParameter(URIBuilder builder, String paratemerKey, String paratemerValue) {
        if (paratemerValue != null) {
            builder.addParameter(paratemerKey, paratemerValue);
        }
    }

    /**
     * Used for POST operations
     */
    protected void addParameter(List<NameValuePair> urlParameters, String paratemerKey, Object paratemerValue) {
        if (paratemerValue != null) {
            urlParameters.add(new BasicNameValuePair(paratemerKey, paratemerValue.toString()));
        }
    }

    protected void addParameter(Map<String, String> parameters, String paratemerKey, Object paratemerValue) {
        if (paratemerValue != null) {
            parameters.put(paratemerKey, paratemerValue.toString());
        }
    }

    private int checkStatusCode(int statusCode) {// throws HttpStatusException, HttpAuthenticationException {
        log.debug("RESPONSE status code = " + statusCode);
        if (statusCode != HttpStatus.OK.value() && statusCode != HttpStatus.CREATED.value()) {
            if (statusCode == HttpStatus.SERVICE_UNAVAILABLE.value() || statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()
                    || statusCode == HttpStatus.BAD_GATEWAY.value()) {
                log.warn("The Mercado libre API is unavailable. StatusCode " + statusCode);
//                throw new HttpServiceUnavailable(HttpError.SERVICE_UNAVAILABLE);
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                log.info("unauthorized Access Token to the Mercado Libre API");
//                throw new HttpAuthenticationException(HttpError.UNAUTHORIZED_MERCADO_LIBRE);
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                log.error("Bad Request to the Mercado Libre API");
//                throw new HttpAuthenticationException(HttpError.UNAUTHORIZED_MERCADO_LIBRE);
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
//                throw new HttpContentNotFoundException(HttpError.CONTENT_NOT_FOUND);
            } else {
                log.error(String.format("There has been an error with Mercado Libre API. StatusCode: %s. Response: %s", statusCode, statusCode));
//                throw new HttpAuthenticationException(HttpError.UNAUTHORIZED_MERCADO_LIBRE);
            }
        }
        return statusCode;
    }

    private <T> T parseResponse(Class<T> clazz, int statusCode, String responseBody) {// throws HttpStatusException {
        return parseResponse(clazz, statusCode, responseBody, null);
    }

    private <T> T parseResponse(Class<T> clazz, int statusCode, String responseBody, String dateFormat) {// throws HttpStatusException {
        T entity = null;
        try {
            log.debug(format(RESPONSE_LOG_FORMAT, statusCode, responseBody));

//            Gson gson;
//            if (dateFormat != null) {
//                gson = new GsonBuilder().setDateFormat(dateFormat).create();
//            } else {
//                gson = new Gson();
//            }

//            entity = gson.fromJson(responseBody, clazz);
        } catch (Exception jse) {
            log.error("RESPONSE status code = " + statusCode);
            log.error(format("RESPONSE Status: %s", statusCode, responseBody));
//            throw new HttpServiceUnavailable(HttpError.SERVICE_UNAVAILABLE);
        }
        return entity;
    }

    protected <T> T sendHttpGetMethod(String url, Map<String, String> parameters, Class<T> clazz) {//throws HttpStatusException {
        return sendHttpGetMethod(url, parameters, null, clazz, null);
    }

    protected <T> T sendHttpGetMethod(String url, Map<String, String> parameters, Map<String, String> headers, Class<T> clazz, String dateFormat)
    {//  throws HttpStatusException {
        HttpGet request = null;

        T entity = null;
        try {
            URIBuilder builder = new URIBuilder(url);

            if (parameters != null) {
                for (Entry<String, String> entry : parameters.entrySet()) {
                    builder = builder.addParameter(entry.getKey(), entry.getValue());
                }
            }

            request = new HttpGet(builder.build());

            if (headers != null) {
                for (String key : headers.keySet()) {
                    String value = headers.get(key);
                    request.addHeader(key, value);
                    log.debug(format("REQUEST Header: %s:%s", key, value));
                }
            }

            log.debug(format(REQUEST_LOG_NO_ENTITY_FORMAT, request.getMethod(), request.getRequestLine() == null ? "" : request.getRequestLine().getUri()));

            HttpResponse response = getHttpClient().execute(request);
            int statusCode = checkStatusCode(response.getStatusLine().getStatusCode());

            String responseBody = (response.getEntity() == null) ? "" : EntityUtils.toString(response.getEntity());
            entity = parseResponse(clazz, statusCode, responseBody, dateFormat);

        } catch (IOException e) {
            log.error("An IOException has been thrown when getting GET object from " + url + ". " + e.getMessage());
//            throw new HttpServiceUnavailable(HttpError.SERVICE_UNAVAILABLE);
        } catch (URISyntaxException e) {
            log.error("An URISyntaxException has been thrown when getting GET object from " + url + ". " + e.getMessage());
//            throw new HttpServiceUnavailable(HttpError.SERVICE_UNAVAILABLE);
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
        }
        return entity;
    }

    protected <T> T sendHttpPostMethod(String url, Map<String, String> parameters, Class<T> clazz) {// throws HttpStatusException {
        HttpPost request = null;

        T entity = null;

        try {
            request = new HttpPost(url);

            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            for (Entry<String, String> entry : parameters.entrySet()) {
                urlParameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            request.setEntity(new UrlEncodedFormEntity(urlParameters, UTF_8));

            log.debug(format(REQUEST_LOG_FORMAT, request.getMethod(), request.getRequestLine() == null ? "" : request.getRequestLine().getUri(),
                    EntityUtils.toString(request.getEntity())));

            HttpResponse response = getHttpClient().execute(request);
            int statusCode = checkStatusCode(response.getStatusLine().getStatusCode());

            String responseBody = (response.getEntity() == null) ? "" : EntityUtils.toString(response.getEntity());
            entity = parseResponse(clazz, statusCode, responseBody);

        } catch (IOException e) {
            log.error("An IOException has been thrown when getting POST object from " + url + ". " + e.getMessage());
//            throw new HttpServiceUnavailable(HttpError.SERVICE_UNAVAILABLE);
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
        }
        return entity;
    }

    protected HttpResponse sendHttpPostMethodWithJSonNoResponseEntity(String url, String json, Map<String, String> headers) {// throws HttpStatusException {
        HttpPost request = null;
        HttpResponse response = null;

        try {
            request = new HttpPost(url);

            if (headers != null) {
                for (String key : headers.keySet()) {
                    String value = headers.get(key);
                    request.addHeader(key, value);
                    log.debug(format("REQUEST Header: %s:%s", key, value));
                }
            }

            StringEntity postingString = new StringEntity(json);
            request.setEntity(postingString);
            request.setHeader("Content-type", APPLICATION_JSON_HEADER);
            log.debug("REQUEST Header: Content-type:application/json");

            log.debug(format(REQUEST_LOG_FORMAT, request.getMethod(), request.getRequestLine() == null ? "" : request.getRequestLine().getUri(),
                    EntityUtils.toString(request.getEntity())));

            response = getHttpClient().execute(request);

            checkStatusCode(response.getStatusLine().getStatusCode());

        } catch (IOException e) {
            log.error("An IOException has been thrown when getting POST object from " + url + ". " + e.getMessage());
//            throw new HttpServiceUnavailable(HttpError.SERVICE_UNAVAILABLE);
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
        }
        return response;
    }

    protected <T> T sendHttpPostMethodWithJson(String url, Object objectToSend, Map<String, String> headers, Class<T> clazz) {//throws HttpStatusException {
        HttpPost request = null;

        T entity = null;

        try {
            request = new HttpPost(url);

            if (headers != null) {
                for (String key : headers.keySet()) {
                    String value = headers.get(key);
                    request.addHeader(key, value);
                    log.debug(format("REQUEST Header: %s:%s", key, value));
                }
            }

//            String json = new Gson().toJson(objectToSend); // convert your pojo to json
//            StringEntity postingString = new StringEntity(json, UTF_8);
//            request.setEntity(postingString);
            request.setHeader("Content-type", APPLICATION_JSON_HEADER);
            log.debug("REQUEST Header: Content-type:application/json");
            log.debug(format(REQUEST_LOG_FORMAT, request.getMethod(), request.getRequestLine() == null ? "" : request.getRequestLine().getUri(),
                    EntityUtils.toString(request.getEntity())));

            HttpResponse response = getHttpClient().execute(request);
            int statusCode = checkStatusCode(response.getStatusLine().getStatusCode());

            String responseBody = (response.getEntity() == null) ? "" : EntityUtils.toString(response.getEntity());
            entity = parseResponse(clazz, statusCode, responseBody);

        } catch (IOException e) {
            log.error("An IOException has been thrown when getting POST object from " + url + ". " + e.getMessage());
//            throw new HttpServiceUnavailable(HttpError.SERVICE_UNAVAILABLE);
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
        }
        return entity;
    }

    protected <T> T sendHttpDeleteMethod(String url, Map<String, String> parameters, Class<T> clazz) {//throws HttpStatusException {
        HttpDelete request = null;

        T entity = null;

        try {
            URIBuilder builder = new URIBuilder(url);

            for (Entry<String, String> entry : parameters.entrySet()) {
                builder = builder.addParameter(entry.getKey(), entry.getValue());
            }

            request = new HttpDelete(builder.build());

            log.debug(format(REQUEST_LOG_NO_ENTITY_FORMAT, request.getMethod(), request.getRequestLine() == null ? "" : request.getRequestLine().getUri()));

            HttpResponse response = getHttpClient().execute(request);

            int statusCode = checkStatusCode(response.getStatusLine().getStatusCode());

            String responseBody = (response.getEntity() == null) ? "" : EntityUtils.toString(response.getEntity());
            entity = parseResponse(clazz, statusCode, responseBody);

        } catch (IOException e) {
            log.error("An IOException has been thrown when getting POST object from " + url + ". " + e.getMessage());
//            throw new HttpServiceUnavailable(HttpError.SERVICE_UNAVAILABLE);
        } catch (URISyntaxException e) {
            log.error("An URISyntaxException has been thrown when sending a DELETE method from " + url + ". " + e.getMessage());
//            throw new HttpServiceUnavailable(HttpError.SERVICE_UNAVAILABLE);
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
        }
        return entity;
    }

    protected HttpClient getHttpClient() {
        return httpClient;
    }
}
