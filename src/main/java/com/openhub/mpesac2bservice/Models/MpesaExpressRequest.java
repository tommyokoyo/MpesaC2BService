package com.openhub.mpesac2bservice.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MpesaExpressRequest {
    @JsonProperty("BusinessShortCode")
    private String BusinessShortCode;
    @JsonProperty("Password")
    private String Password;
    @JsonProperty("Timestamp")
    private String Timestamp;
    @JsonProperty("TransactionType")
    private String TransactionType;
    @JsonProperty("Amount")
    private String Amount;
    @JsonProperty("PartyA")
    private String PartyA;
    @JsonProperty("PartyB")
    private String PartyB;
    @JsonProperty("PhoneNumber")
    private String PhoneNumber;
    @JsonProperty("CallBackURL")
    private String CallBackURL;
    @JsonProperty("AccountReference")
    private String AccountReference;
    @JsonProperty("TransactionDesc")
    private String TransactionDesc;
}
