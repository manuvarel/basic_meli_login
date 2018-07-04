package com.melilogin.demo.webservices.implementations;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.melilogin.demo.common.api.mercadolibre.entities.MeliAccessToken;
import com.melilogin.demo.services.interfaces.MeliService;
import com.melilogin.demo.webservices.interfaces.MeliRestService;

@RestController
public class MeliRestServiceImpl implements MeliRestService {
    private final MeliService meliService;

    private static final Logger log = Logger.getLogger(MeliRestServiceImpl.class);

//    public static final String VERIF_METHOD_REGEX = "(^" + MeliService.VERIF_FILE + "|^" + MeliService.VERIF_META + "|^" + MeliService.VERIF_TXT + "|^"
//            + MeliService.VERIF_CNAME + ")$";

    @Autowired
    public MeliRestServiceImpl(MeliService meliService) {
        this.meliService = meliService;
    }

    @Override
    public String ping(@PathVariable("message") String message) {
        return message;
    }

    @Override
    public MeliAccessToken getAccessToken(@RequestHeader(value = "authorizationCode", required = true) String authorizationCode) {// throws HttpStatusException {
        return meliService.getAccessToken(authorizationCode);
    }

    @Override
    public MeliAccessToken getAccessTokenFromRefreshToken(@RequestHeader(value = "refreshToken", required = true) String refreshToken) {// throws HttpStatusException {
        return meliService.getAccessTokenFromRefreshToken(refreshToken);
    }
}
