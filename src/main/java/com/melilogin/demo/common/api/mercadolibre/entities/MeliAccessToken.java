package com.melilogin.demo.common.api.mercadolibre.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeliAccessToken extends MeliError {
    private @SerializedName("access_token") String accessToken;
    private @SerializedName("refresh_token") String refreshToken;
    private @SerializedName("expires_in") int expiresIn;

}
