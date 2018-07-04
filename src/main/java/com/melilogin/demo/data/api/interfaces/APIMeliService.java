package com.melilogin.demo.data.api.interfaces;

import com.melilogin.demo.common.api.mercadolibre.entities.MeliAccessToken;

public interface APIMeliService {

//    public MeliUser getMe(String accessToken) throws HttpStatusException;

    public MeliAccessToken getAccessToken(String authorizationCode);// throws HttpAuthenticationException;

    public MeliAccessToken getAccessTokenFromRefreshToken(String refreshToken); // throws HttpAuthenticationException;
    
}