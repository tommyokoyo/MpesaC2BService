package com.openhub.mpesac2bservice.Models;

import lombok.Data;

@Data
public class TransactionMessage {
    private String transactionId;
    private Double amount;
    private String phoneNumber;
    private String status;
}
