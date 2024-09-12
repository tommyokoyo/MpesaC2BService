package com.openhub.mpesac2bservice.Models;

import lombok.Data;

import java.util.List;

@Data
public class MpesaCallback {
    private Body body;
    @Data
    public static class Body {
        private StkCallback stkCallback;
    }
    @Data
    public static class StkCallback {
        private String merchantRequestID;
        private String checkOutRequestID;
        private int resultCode;
        private String resultDescription;
        private CallbackMetadata callBackmetadata;
    }
    @Data
    public static class CallbackMetadata {
        private List<Item> item;
    }
    @Data
    public static class Item {
        private String name;
        private Object value;
    }
}
