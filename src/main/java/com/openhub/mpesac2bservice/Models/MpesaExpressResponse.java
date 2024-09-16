package com.openhub.mpesac2bservice.Models;

import lombok.Data;

@Data
public class MpesaExpressResponse {
    private String MerchantRequestID;
    private String CheckoutRequestID;
    private String ResponseCode;
    private String ResponseDescription;
    private String CustomerMessage;
}
