package com.melilogin.demo.services.implementations;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.melilogin.demo.common.api.mercadolibre.entities.MeliAccessToken;
import com.melilogin.demo.data.api.interfaces.APIMeliService;
import com.melilogin.demo.services.interfaces.MeliService;
//import com.melilogin.demo.webservices.exceptions.HttpStatusException;

@Transactional
@Service
public class MeliServiceImpl implements MeliService {
    private static final String ENCODING_UTF_8 = "UTF-8";

    private static final Logger log = Logger.getLogger(MeliServiceImpl.class);

    private final APIMeliService apiMeliService;
    
    @Autowired
    public MeliServiceImpl(APIMeliService apiMeliService) {
        this.apiMeliService = apiMeliService;
    }

    public MeliAccessToken getAccessToken(String authorizationCode) {// throws HttpStatusException {
        MeliAccessToken accessToken = apiMeliService.getAccessToken(authorizationCode);
//        saveRefreshToken(accessToken);
        return accessToken;
    }

    @Override
    public MeliAccessToken getAccessTokenFromRefreshToken(String refreshToken) {// throws HttpStatusException {
        MeliAccessToken accessToken = apiMeliService.getAccessTokenFromRefreshToken(refreshToken);
//        saveRefreshToken(accessToken);
        return accessToken;
    }

    private void saveRefreshToken(MeliAccessToken accessToken) {// throws HttpStatusException {
//        MeliUser meliUser = apiMeliService.getMe(accessToken.getAccessToken());
//
//        User user;
//        Optional<User> foundUser = userDAO.findById(meliUser.getId());
//        if (!foundUser.isPresent()) {
//            user = UserTransformer.transformTo(meliUser);
//            user.setRefreshToken(accessToken.getRefreshToken());
//            userDAO.create(user);
//        } else {
//            user = foundUser.get();
//            user.setRefreshToken(accessToken.getRefreshToken());
//            userDAO.update(user);
//        }
    }

    
}
