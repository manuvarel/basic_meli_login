package com.melilogin.demo.webservices.interfaces;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.melilogin.demo.common.api.mercadolibre.entities.MeliAccessToken;


@RequestMapping("/meli")
public interface MeliRestService {

    @RequestMapping(value = "/authorize", method = RequestMethod.GET)
    public MeliAccessToken getAccessToken(String authorizationCode);// throws HttpStatusException;

    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    MeliAccessToken getAccessTokenFromRefreshToken(String refreshToken);// throws HttpStatusException;

    @RequestMapping(value = "/ping/{message}", method = RequestMethod.GET)
    public String ping(String message);

}
