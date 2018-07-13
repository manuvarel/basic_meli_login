package com.melilogin.demo.data.api.interfaces;

import com.melilogin.demo.common.api.mercadolibre.entities.MeliAccessToken;
import com.melilogin.demo.common.api.mercadolibre.entities.MeliUser;
import com.melilogin.demo.webservices.exceptions.HttpStatusException;

public interface APIMeliService {

    public MeliUser getMe(String accessToken) throws HttpStatusException;

    public MeliAccessToken getAccessToken(String authorizationCode);// throws HttpAuthenticationException;

    public MeliAccessToken getAccessTokenFromRefreshToken(String refreshToken); // throws HttpAuthenticationException;
    
}