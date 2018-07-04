package com.melilogin.demo.services.interfaces;

import com.melilogin.demo.common.api.mercadolibre.entities.MeliAccessToken;

public interface MeliService {

    MeliAccessToken getAccessToken(String authorizationCode);// throws HttpStatusException;

    MeliAccessToken getAccessTokenFromRefreshToken(String refreshToken); // throws HttpStatusException;

}
