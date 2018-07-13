package com.melilogin.demo.common.api.mercadolibre.entities;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class MeliUser {
    private Long id;
    private @SerializedName("first_name") String firstname;
    private @SerializedName("last_name") String lastname;
    private @SerializedName("email") String email;
    private @SerializedName("country_id") String countryCode;
//    private @SerializedName("address") Address address;
    private @SerializedName("status") Status status;

    @Data
    public static class Status {
        private @SerializedName("billing") Billing billing;
    }

    @Data
    public static class Billing {
        private @SerializedName("allow") Boolean allow;
        private @SerializedName("codes") List<String> codes;
    }
    
//    public class Address {
//        
//        private @SerializedName("zip_code") String zipCode;
//        
//    }
}
