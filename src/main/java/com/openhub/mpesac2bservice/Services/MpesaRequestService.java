package com.openhub.mpesac2bservice.Services;

import com.openhub.mpesac2bservice.Models.MpesaExpressRequest;
import com.openhub.mpesac2bservice.Models.MpesaExpressResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class MpesaRequestService {
    private final WebClient webClient;

    public MpesaRequestService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:9093").build();
    }

    @Retry(name = "mpesaRequest", fallbackMethod = "fallBackMpesaRequest")
    @CircuitBreaker(name = "mpesaRequestCircuitBreaker", fallbackMethod = "fallBackMpesaRequest")
    public Mono<MpesaExpressResponse> makeMpesaRequest(MpesaExpressRequest MpesaExpressRequestdata) {
        return webClient.post()
                .uri("/mpesa-sim/transaction/mpesa-express")
                .bodyValue(MpesaExpressRequestdata)
                .retrieve()
                .bodyToMono(MpesaExpressResponse.class)
                .onErrorResume(WebClientResponseException.class, this::handleWebclientException);
    }

    public Mono<MpesaExpressResponse> handleWebclientException(WebClientResponseException ex) {
        MpesaExpressResponse mpesaExpressResponse = ex.getResponseBodyAs(MpesaExpressResponse.class);
        if (mpesaExpressResponse != null) {
            return Mono.just(mpesaExpressResponse);
        } else {
            MpesaExpressResponse mpesaExpressErrorResponse = new MpesaExpressResponse();
            System.out.println("Ërror response Body: " + mpesaExpressErrorResponse);
            mpesaExpressErrorResponse.setResponseCode("1");
            mpesaExpressErrorResponse.setCustomerMessage("Service Error");
            mpesaExpressErrorResponse.setResponseDescription("API service did not respond.");
            return Mono.just(mpesaExpressErrorResponse);
        }
    }

    public Mono<String> fallBackMpesaRequest(String RequestData, Throwable throwable) {
        return Mono.just("M-SIM service not available:");
    }
}
