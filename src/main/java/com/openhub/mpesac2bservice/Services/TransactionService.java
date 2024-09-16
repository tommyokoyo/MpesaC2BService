package com.openhub.mpesac2bservice.Services;

import com.openhub.mpesac2bservice.Models.*;
import com.openhub.mpesac2bservice.Utils.RabbitMQConfig;
import com.openhub.mpesac2bservice.Utils.Utility;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private final Utility utility;
    private final RabbitTemplate rabbitTemplate;
    private final MpesaRequestService mpesaRequestService;

    @Autowired
    public TransactionService(Utility utility, RabbitTemplate rabbitTemplate, MpesaRequestService mpesaRequestService) {
        this.utility = utility;
        this.rabbitTemplate = rabbitTemplate;
        this.mpesaRequestService = mpesaRequestService;
    }

    public MpesaExpressResponse lipaNaMpesa(MpesaExpressRequest mpesaExpressRequest) {
        // Add transaction to queue for processing
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.TRANSACTION_QUEUE, mpesaExpressRequest);
            try {
                // Send request to SIM
                MpesaExpressResponse mpesaExpressResponse = mpesaRequestService.makeMpesaRequest(mpesaExpressRequest).block();
                System.out.println("M_SIM response: " + mpesaExpressResponse);
                // Return response based on SIM response
                if (mpesaExpressResponse != null) {
                    return mpesaExpressResponse;
                } else {
                    MpesaExpressResponse mpesaExpressErrorResponse = new MpesaExpressResponse();
                    mpesaExpressErrorResponse.setResponseCode("1");
                    mpesaExpressErrorResponse.setResponseDescription("Empty Response");
                    mpesaExpressErrorResponse.setCustomerMessage("Empty Response body");
                    return mpesaExpressErrorResponse;
                }
            }
            catch (Exception e) {
                System.out.println("Request failed: "
                        + e);
                MpesaExpressResponse mpesaExpressExceptionResponse = new MpesaExpressResponse();
                mpesaExpressExceptionResponse.setResponseCode("1");
                mpesaExpressExceptionResponse.setResponseDescription("Service Error");
                mpesaExpressExceptionResponse.setCustomerMessage("Error fetching data");
                return mpesaExpressExceptionResponse;
            }
        } catch (Exception e) {
            System.out.println("Could not add transaction to queue: "
                    + ": " + e.getMessage());
            MpesaExpressResponse mpesaExpressExceptionResponse = new MpesaExpressResponse();
            mpesaExpressExceptionResponse.setResponseCode("1");
            mpesaExpressExceptionResponse.setResponseDescription("Queue Error");
            mpesaExpressExceptionResponse.setCustomerMessage("Could not add transaction to queue");
            return mpesaExpressExceptionResponse;
        }
    }

    public void lipaNaMpesaDemo(TransactionMessage transactionMessage) {
        System.out.println("Transaction " + transactionMessage.getTransactionId() + " retrieved");
        if (transactionMessage.getPhoneNumber().equals("+254716210475")) {
            MpesaCallback.Item transactionAmount = new MpesaCallback.Item();
            transactionAmount.setName("Amount");
            transactionAmount.setValue(transactionMessage.getAmount());

            MpesaCallback.Item phoneNumber = new MpesaCallback.Item();
            phoneNumber.setName("phoneNumber");
            phoneNumber.setValue(transactionMessage.getPhoneNumber());

            MpesaCallback.Item transactionID = new MpesaCallback.Item();
            transactionID.setName("TransactionID");
            transactionID.setValue(transactionMessage.getTransactionId());

            MpesaCallback.Item transactionStatus = new MpesaCallback.Item();
            transactionStatus.setName("TransactionStatus");
            transactionStatus.setValue("Processed");

            List<MpesaCallback.Item> item = new ArrayList<>();
            item.add(transactionAmount);
            item.add(phoneNumber);
            item.add(transactionID);
            item.add(transactionStatus);

            // Set the callback Metadata
            MpesaCallback.CallbackMetadata callbackMetadata = new MpesaCallback.CallbackMetadata();
            callbackMetadata.setItem(item);
            MpesaCallback.StkCallback stkCallback = new MpesaCallback.StkCallback();
            stkCallback.setMerchantRequestID("29115-34620561-1");
            stkCallback.setCheckOutRequestID("ws_CO_191220191020363925");
            stkCallback.setResultCode(0);
            stkCallback.setResultDescription("The service request is processed successfully.");
            stkCallback.setCallBackmetadata(callbackMetadata);

            // Create the Body
            MpesaCallback.Body body = new MpesaCallback.Body();
            body.setStkCallback(stkCallback);

            // Create Callback response
            MpesaCallback mpesaCallback = new MpesaCallback();
            mpesaCallback.setBody(body);

            System.out.println("Callback is:  " + mpesaCallback);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.CALLBACK_QUEUE, mpesaCallback.getBody());
        } else {
            MpesaCallback.Item transactionStatus = new MpesaCallback.Item();
            transactionStatus.setName("TransactionStatus");
            transactionStatus.setValue("Failed");

            List<MpesaCallback.Item> item = new ArrayList<>();
            item.add(transactionStatus);

            // Set the callback Metadata
            MpesaCallback.CallbackMetadata failedCallbackMetadata = new MpesaCallback.CallbackMetadata();
            failedCallbackMetadata.setItem(item);
            MpesaCallback.StkCallback stkCallback = new MpesaCallback.StkCallback();
            stkCallback.setMerchantRequestID("29115-34620561-1");
            stkCallback.setCheckOutRequestID("ws_CO_191220191020363925");
            stkCallback.setResultCode(0);
            stkCallback.setResultDescription("The service request is processed successfully.");
            stkCallback.setCallBackmetadata(failedCallbackMetadata);

            // Create the Body
            MpesaCallback.Body body = new MpesaCallback.Body();
            body.setStkCallback(stkCallback);

            // Create Callback response
            MpesaCallback mpesaCallback = new MpesaCallback();
            mpesaCallback.setBody(body);

            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.CALLBACK_QUEUE, mpesaCallback);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.CALLBACK_QUEUE)
    public void lipaNaMpesaCallback(MpesaCallbackMessage mpesaCallbackMessage) {
        // Dispatch message
        System.out.println("Successfully received the message on the callback queue, message: " + mpesaCallbackMessage);
    }
}
