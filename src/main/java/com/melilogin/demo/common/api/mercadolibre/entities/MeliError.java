package com.melilogin.demo.common.api.mercadolibre.entities;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class MeliError {
    private @SerializedName("message") String message;
    private @SerializedName("error") String error;
    private @SerializedName("status") Integer status;

}
