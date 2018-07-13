package com.melilogin.demo.services.implementations;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.melilogin.demo.common.api.mercadolibre.entities.MeliAccessToken;
import com.melilogin.demo.common.api.mercadolibre.entities.MeliUser;
import com.melilogin.demo.common.persistence.entities.User;
import com.melilogin.demo.data.api.interfaces.APIMeliService;
import com.melilogin.demo.data.persistence.dao.interfaces.UserDAO;
import com.melilogin.demo.services.interfaces.MeliService;
import com.melilogin.demo.webservices.exceptions.HttpStatusException;

@Transactional
@Service
public class MeliServiceImpl implements MeliService {
    private static final String ENCODING_UTF_8 = "UTF-8";

    private static final Logger log = Logger.getLogger(MeliServiceImpl.class);

    private final APIMeliService apiMeliService;
    
    private final UserDAO userDAO;
    
    @Autowired
    public MeliServiceImpl(APIMeliService apiMeliService, UserDAO userDao) {
        this.apiMeliService = apiMeliService;
        this.userDAO = userDao;
    }

    public MeliAccessToken getAccessToken(String authorizationCode) throws HttpStatusException {
        MeliAccessToken accessToken = apiMeliService.getAccessToken(authorizationCode);
        saveRefreshToken(accessToken);
        return accessToken;
    }

    @Override
    public MeliAccessToken getAccessTokenFromRefreshToken(String refreshToken) throws HttpStatusException {
        MeliAccessToken accessToken = apiMeliService.getAccessTokenFromRefreshToken(refreshToken);
        saveRefreshToken(accessToken);
        return accessToken;
    }

    private void saveRefreshToken(MeliAccessToken meliAccessToken) throws HttpStatusException {
        MeliUser meliUser = apiMeliService.getMe(meliAccessToken.getAccessToken());
        User user;
        Optional<User> foundUser = userDAO.findById(meliUser.getId());
        if (!foundUser.isPresent()) {
            user = User.builder().accessToken(meliAccessToken.getAccessToken()).refreshToken(meliAccessToken.getRefreshToken()).meliUserId(meliUser.getId()).build();
            userDAO.create(user);
        } else {
            user = foundUser.get();
            user.setRefreshToken(meliAccessToken.getRefreshToken());
            userDAO.update(user);
        }
    }

    
}
