package com.shopme.checkout;

import com.shopme.checkout.paypal.PayPalOrderResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


public class PayPalApiTests {

    private static final String BASE_URL = "https://api.sandbox.paypal.com";
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    private static final String CLIENT_ID = "Ab6KNbfz1MzRBaAAJJPwHvwVowD7UJiPrdQyaqRT0VV94UKU4ioL6JqAsOhDUth0pOdeUz5rPJq7iKNT";
    private static final String CLIENT_SECRET = "ECnkdBcx-jUuQ4ZFeO61RTPRML3_Effls97rHUWzzNHJpNuIuHfZb0_5dQJNazRF6Zar9pfMSKN8O-Ht";

    @Test
    public void testGetOrderDetails(){
        String orderId = "3XB99675DF8834253";
        String requestURL = BASE_URL + GET_ORDER_API + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>>  request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);

        PayPalOrderResponse orderResponse = response.getBody();

        System.out.println("Order ID: " + orderResponse.getId());

        System.out.println("Validated: " + orderResponse.validate(orderId));
    }
}
