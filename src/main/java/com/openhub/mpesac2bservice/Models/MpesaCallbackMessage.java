package com.openhub.mpesac2bservice.Models;

import lombok.Data;

@Data
public class MpesaCallbackMessage {
    private String MerchantRequestID;
    private String CheckoutRequestID;
    private int ResultCode;
    private String ResultDesc;
    private Double transactionAmount;
    private String transactionID;
}
