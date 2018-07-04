package com.melilogin.demo.common.api.dao.interfaces;

import com.melilogin.demo.common.api.mercadolibre.entities.MeliAccessToken;

public interface MeliDAO {

//    public MeliUser getMe(String accessToken) throws HttpStatusException;

    public MeliAccessToken getAccessToken(String authorizationCode);// throws HttpAuthenticationException;

    public MeliAccessToken getAccessTokenFromRefreshToken(String refreshToken);// throws HttpAuthenticationException;

//    public MeliShop getMeliShop(String accessToken, Long shopId) throws HttpStatusException;
//
//    public MeliUser getMeInternal(Long shopId) throws HttpStatusException;
//
//    public MeliShop getMeliShopInternal(Long shopId) throws HttpStatusException;

}