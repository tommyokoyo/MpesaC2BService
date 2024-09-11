package com.openhub.mpesac2bservice.Models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static ResponseEntity<ResponseModel> buildResponse(HttpStatus status, String state, String message) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setStatus(state);
        responseModel.setMessage(message);
        return ResponseEntity.status(status).body(responseModel);
    }
}
