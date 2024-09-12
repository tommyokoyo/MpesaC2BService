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
    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/lipa-na-mpesa")
    public ResponseEntity<?> customerToBusiness(@RequestBody TransactionMessage transactionMessage) {
        // Check the parameters to ensure no null values
        if (transactionMessage.getPhoneNumber() == null || transactionMessage.getPhoneNumber().isEmpty()) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, "Failed", "Missing Phone Number");
        } else if (transactionMessage.getAmount() == null) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, "Failed", "Missing Amount");
        }

        ResponseModel responseModel = transactionService.lipaNaMpesa(transactionMessage);

        if (responseModel != null && responseModel.getStatus().equals("success")) {
            return ResponseUtil.buildResponse(HttpStatus.OK, responseModel.getStatus(), responseModel.getMessage());
        } else {
            return ResponseUtil.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed", "Error Processing Request" );
        }


    }

    @PostMapping("/callbackStatus")
    public ResponseEntity<?> customerToBusinessCallback() {
        return ResponseUtil.buildResponse(HttpStatus.OK, "Successful", "Callback Status");
    }
}
