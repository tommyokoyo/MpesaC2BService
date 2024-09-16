package com.openhub.mpesac2bservice.Models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static ResponseEntity<ResponseModel> buildCustomResponse(HttpStatus status, String state, String message) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setStatus(state);
        responseModel.setMessage(message);
        return ResponseEntity.status(status).body(responseModel);
    }

    public static ResponseEntity<MpesaExpressResponse> buildMpesaErrorResponse(HttpStatus status, String responseDescription, String message) {
        MpesaExpressResponse responseModel = new MpesaExpressResponse();
        responseModel.setResponseCode(String.valueOf(status));
        responseModel.setResponseDescription(responseDescription);
        responseModel.setCustomerMessage(message);
        return ResponseEntity.status(status).body(responseModel);
    }
}
