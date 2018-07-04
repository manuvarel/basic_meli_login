package com.melilogin.webservices.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtils {
    private static String SECRET_KEY = "alice-in-wonderland";

    @Value("${password.encrypted}")
    private boolean passwordEncrypted;

    public String encrypt(String accessToken) {
//        if (passwordEncrypted) {
//            JwtBuilder jwtBuilder = Jwts.builder();
//            jwtBuilder.setSubject(accessToken);
//            jwtBuilder.signWith(SignatureAlgorithm.HS512, SECRET_KEY);
//            return jwtBuilder.compact();
//        }
        return accessToken;
    }

    public String decrypt(String accessToken) { // throws HttpAuthenticationException {
//        if (passwordEncrypted) {
//            try {
//                Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken);
//                return claimsJws.getBody().getSubject();
//            } catch (ExpiredJwtException e) {
//                throw new HttpAuthenticationException(HttpError.UNAUTHORIZED_MERCADO_LIBRE);
//            } catch (UnsupportedJwtException e) {
//                throw new HttpAuthenticationException(HttpError.UNAUTHORIZED_MERCADO_LIBRE);
//            } catch (MalformedJwtException e) {
//                throw new HttpAuthenticationException(HttpError.UNAUTHORIZED_MERCADO_LIBRE);
//            } catch (SignatureException e) {
//                throw new HttpAuthenticationException(HttpError.UNAUTHORIZED_MERCADO_LIBRE);
//            } catch (IllegalArgumentException e) {
//                throw new HttpAuthenticationException(HttpError.UNAUTHORIZED_MERCADO_LIBRE);
//            }
//        }
        return accessToken;
    }

}
