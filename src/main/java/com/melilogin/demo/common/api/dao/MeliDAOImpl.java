package com.melilogin.demo.common.api.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

//import com.google.gson.Gson;
import com.melilogin.demo.common.api.dao.interfaces.MeliDAO;
import com.melilogin.demo.common.api.mercadolibre.entities.MeliAccessToken;
import com.melilogin.demo.common.api.mercadolibre.entities.MeliUser;
import com.melilogin.demo.webservices.exceptions.HttpStatusException;
import com.mercadolibre.sdk.AuthorizationFailure;
import com.mercadolibre.sdk.Meli;
import com.mercadolibre.sdk.MeliException;
import com.ning.http.client.FluentStringsMap;
import com.ning.http.client.Response;

@Repository
public class MeliDAOImpl extends BaseApiDAOImpl implements MeliDAO {

    private static final Logger log = Logger.getLogger(MeliDAOImpl.class);

    private static final int STATUS_OK = 200;
    private static final int ML_EXPIRE_TIME = 10800;
    private static final String ACCESS_TOKEN_PARAMETER = "access_token";
    private static final String ACCESS_TOKEN_FIELD = "?access_token=%s";

    // MeLi endpoints
    private static final String REFRESH_TOKEN = "/oauth/token";

    private static final String RESPONSYS_API_URL = "/rsys_proxy/responsysUploader/files";

    // MShops endpoints
    private static final String ML_API_URL = "https://api.mercadolibre.com/";
    private static final String USERS = "/users/%s";
    private static final String USERS_ME = "/users/me";
    private static final String ML_USER_ME = ML_API_URL + USERS_ME + ACCESS_TOKEN_FIELD;
    private static final String ML_USER = ML_API_URL + "/users/%s" + ACCESS_TOKEN_FIELD;

    private static final String MSHOP_API_VERSION = "v1";
    private static final String MSHOP_API_URL = "https://api.mercadoshops.com/" + MSHOP_API_VERSION;
    private static final String SHOP_MSHOP_URL = MSHOP_API_URL + "/shops/%s";
    private static final String CALLER_ID_PARAM = "?caller.id=%s";
    private static final String MSHOPS_GET_SHOP_URL = "/v1/shops/%s" + CALLER_ID_PARAM;
    
    private final Meli meli;
//    private final EncryptionUtils encryption;

    //@Value("${meli.internal.ml.host}")
    private String internalMercadoLibreHost;

    //@Value("${meli.internal.ms.host}")
    private String internalMercadoShopsHost;

    //@Value("${meli.app.id}")
    private String meliAppId = "1158642842496424";

    //@Value("${meli.secret.key}")
    private String meliSecretKey = "anSpSxcp1pFpGl7PINtwPSd2MLy6vkmD";

    @Value("${meli.redirect.uri}")
    private String meliRedirectUri;

    //@Autowired
    public MeliDAOImpl() { //(RestTemplate restTemplate) { //Meli meli) {
        //this.meli = meli;
//        super(restTemplate);
        meli = new Meli(Long.valueOf(meliAppId), meliSecretKey);
    }

    @Override
    public MeliUser getMe(String accessToken) throws HttpStatusException {
        Map<String, String> parameters = new HashMap<String, String>();
        String url = String.format(ML_USER_ME, accessToken);
        MeliUser meliUser = sendHttpGetMethod(url, parameters, MeliUser.class);

        if (meliUser.getEmail() == null) {
            url = String.format(ML_USER, meliUser.getId(),accessToken);
            meliUser = sendHttpGetMethod(url, parameters, MeliUser.class);
        }

        return meliUser;
    }
//    
//    @Override
//    public MeliUser getMeInternal(Long shopId) throws HttpStatusException {
//        String url = internalMercadoLibreHost + USERS_ME + CALLER_ID_PARAM;
//        url = String.format(url, shopId);
//        Map<String, String> params = new HashMap<String, String>();
//
//        MeliUser meliUser = sendHttpGetMethod(url, params, MeliUser.class);
//        
//        if (meliUser.getEmail() == null) {
//            url = String.format(internalMercadoLibreHost + USERS + CALLER_ID_PARAM, shopId, shopId);
//            meliUser = sendHttpGetMethod(url, params, MeliUser.class);
//        }
//        
//        return meliUser;
//    }

    @Override
    public MeliAccessToken getAccessToken(String authorizationCode) {// throws HttpAuthenticationException {
        try {
//            String redirectUrl = meli.getAuthUrl("http://localhost:8080/meli/authorize");//("http://somecallbackurl",Meli.apiUrl); //Don't forget to set the autentication URL of your country
            meli.authorize(authorizationCode, meliRedirectUri);
        } catch (AuthorizationFailure e) {
            log.warn("A AuthorizationFailure from MeLi has been thrown " + e.getMessage());
//            throw new HttpAuthenticationException(HttpError.UNAUTHORIZED_MERCADO_LIBRE);
        }

        MeliAccessToken meliAccessToken = MeliAccessToken.builder().refreshToken(meli.getRefreshToken()).expiresIn(ML_EXPIRE_TIME).build();
        meliAccessToken.setAccessToken(meli.getAccessToken());
//        meliAccessToken.setAccessToken(encryption.encrypt(meli.getAccessToken()));
        return meliAccessToken;
    }

    @Override
    public MeliAccessToken getAccessTokenFromRefreshToken(String refreshToken) {// throws HttpAuthenticationException {
        MeliAccessToken meliAccessToken = null;

        FluentStringsMap params = new FluentStringsMap();
        params.add("grant_type", "refresh_token");
        params.add("client_id", meliAppId);
        params.add("client_secret", meliSecretKey);
        params.add("refresh_token", refreshToken);

        try {
            Response response = meli.post(REFRESH_TOKEN, params, null);
            String responseBody = response.getResponseBody();
//            meliAccessToken = new Gson().fromJson(responseBody, MeliAccessToken.class);
            if (meliAccessToken.getStatus() != null && !meliAccessToken.getStatus().equals(STATUS_OK)) {
                log.warn("A MeliException has been thrown when getting refresh token" + meliAccessToken);
//                throw new HttpAuthenticationException(HttpError.UNAUTHORIZED_MERCADO_LIBRE);
            }
//            meliAccessToken.setAccessToken(encryption.encrypt(meliAccessToken.getAccessToken()));
        } catch (MeliException me) {
            log.warn("A MeliException has been thrown when getting refresh token" + me.getMessage());
//            throw new HttpAuthenticationException(HttpError.UNAUTHORIZED_MERCADO_LIBRE);
        } catch (IOException e) {
            log.error("An IOException has been thrown when getting refresh token" + e.getMessage());
        }

        return meliAccessToken;
    }

//    @Override
//    public MeliShop getMeliShop(String accessToken, Long shopId) throws HttpStatusException {
//        Map<String, String> parameters = new HashMap<String, String>();
//        parameters.put(ACCESS_TOKEN_PARAMETER, encryption.decrypt(accessToken));
//        String url = String.format(SHOP_MSHOP_URL, shopId);
//        return sendHttpGetMethod(url, parameters, MeliShop.class);
//    }
    
//    @Override
//    public MeliShop getMeliShopInternal(Long shopId) throws HttpStatusException {
//        Map<String, String> parameters = new HashMap<String, String>();
//        String url = String.format(internalMercadoShopsHost + MSHOPS_GET_SHOP_URL, shopId, shopId);
//        return sendHttpGetMethod(url, parameters, MeliShop.class);
//    }

  

    private Map<String, String> parseHeaders(String headerStr) {
        Map<String, String> headers = new HashMap<String, String>();

        if (headerStr != null && !headerStr.trim().isEmpty()) {
            String[] keyValues = headerStr.split(",");

            for (String keyValue : keyValues) {
                String[] pair = keyValue.split(":");
                if (pair.length != 2)
                    continue;

                headers.put(pair[0].trim(), pair[1].trim());
            }
        }
        return headers;
    }
}
