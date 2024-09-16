package com.openhub.mpesac2bservice.Controllers;

import com.openhub.mpesac2bservice.Models.*;
import com.openhub.mpesac2bservice.Services.TransactionService;
import com.openhub.mpesac2bservice.Utils.RabbitMQConfig;
import com.openhub.mpesac2bservice.Utils.Utility;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/lipa-na-mpesa")
    public ResponseEntity<?> customerToBusiness(@RequestBody MpesaExpressRequest mpesaExpressRequest) {
        // Check the parameters to ensure no null values
        if (mpesaExpressRequest.getBusinessShortCode() == null || mpesaExpressRequest.getBusinessShortCode().isEmpty()){
            return ResponseUtil.buildMpesaErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "Missing Parameter",
                    "Business Short Code Required");
        } else if (mpesaExpressRequest.getPassword() == null || mpesaExpressRequest.getPassword().isEmpty()){
            return ResponseUtil.buildMpesaErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "Missing Parameter",
                    "Password Required");
        } else if (mpesaExpressRequest.getTimestamp() == null || mpesaExpressRequest.getTimestamp().isEmpty()) {
            return ResponseUtil.buildMpesaErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "Missing Parameter",
                    "Timestamp Required");
        }else if (mpesaExpressRequest.getTransactionType() == null) {
            return ResponseUtil.buildMpesaErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "Missing Parameter",
                    "Transaction Type Required");
        } else if (mpesaExpressRequest.getAmount() == null || mpesaExpressRequest.getAmount().isEmpty()){
            return ResponseUtil.buildMpesaErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "Missing Parameter",
                    "Amount Required");
        } else if (mpesaExpressRequest.getPartyA() == null || mpesaExpressRequest.getPartyA().isEmpty()){
            return ResponseUtil.buildMpesaErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "Missing Parameter",
                    "Party A Required");
        }else if (mpesaExpressRequest.getPartyB() == null || mpesaExpressRequest.getPartyB().isEmpty()){
            return ResponseUtil.buildMpesaErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "Missing Parameter",
                    "Party B Required");
        } else if (mpesaExpressRequest.getPhoneNumber() == null || mpesaExpressRequest.getPhoneNumber().isEmpty()){
            return ResponseUtil.buildMpesaErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "Missing Parameter",
                    "Phone Number Required");
        }else if (mpesaExpressRequest.getCallBackURL() == null || mpesaExpressRequest.getCallBackURL().isEmpty()){
            return ResponseUtil.buildMpesaErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "Missing Parameter",
                    "CallBack URL Required");
        }else if (mpesaExpressRequest.getAccountReference() == null || mpesaExpressRequest.getAccountReference().isEmpty()){
            return ResponseUtil.buildMpesaErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "Missing Parameter",
                    "Account Reference Required");
        }else if (mpesaExpressRequest.getTransactionDesc() == null || mpesaExpressRequest.getTransactionDesc().isEmpty()){
            return ResponseUtil.buildMpesaErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "Missing Parameter",
                    "Transaction Desc Required");
        }

        MpesaExpressResponse mpesaExpressResponse = transactionService.lipaNaMpesa(mpesaExpressRequest);

        if (mpesaExpressResponse != null) {
            return ResponseEntity.status(HttpStatus.OK).body(mpesaExpressResponse);
        } else {
            return ResponseUtil.buildMpesaErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Internal Server Error",
                    "Error Processing request");
        }
    }

    @PostMapping("/callbackStatus")
    public ResponseEntity<?> customerToBusinessCallback() {
        return ResponseUtil.buildMpesaErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Success",
                "Callback reached successfully");
    }
}
