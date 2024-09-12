package com.openhub.mpesac2bservice.Utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Utility {
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
