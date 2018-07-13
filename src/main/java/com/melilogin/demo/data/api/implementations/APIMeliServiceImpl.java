package com.melilogin.demo.data.api.implementations;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilogin.demo.common.api.dao.interfaces.MeliDAO;
import com.melilogin.demo.common.api.mercadolibre.entities.MeliAccessToken;
import com.melilogin.demo.common.api.mercadolibre.entities.MeliUser;
import com.melilogin.demo.data.api.interfaces.APIMeliService;
import com.melilogin.demo.webservices.exceptions.HttpStatusException;

//@Transactional
@Service
public class APIMeliServiceImpl implements APIMeliService {
    private static final String HAS_DEBT = "has_debt";

    private static final Logger log = Logger.getLogger(APIMeliServiceImpl.class);

    private MeliDAO meliDAO;

    @Autowired
    public APIMeliServiceImpl(MeliDAO meliDAO) {
        this.meliDAO = meliDAO;
    }

    @Override
    public MeliUser getMe(String accessToken) throws HttpStatusException {
        return meliDAO.getMe(accessToken);
    }
//    
//    @Override
//    public MeliUser getMeInternal(Long shopId) throws HttpStatusException {
//        return meliDAO.getMeInternal(shopId);
//    }

    @Override
    public MeliAccessToken getAccessToken(String authorizationCode) {// throws HttpAuthenticationException {
        return meliDAO.getAccessToken(authorizationCode);
    }

    @Override
    public MeliAccessToken getAccessTokenFromRefreshToken(String refreshToken) {//throws HttpAuthenticationException {
        return meliDAO.getAccessTokenFromRefreshToken(refreshToken);
    }

    
}
