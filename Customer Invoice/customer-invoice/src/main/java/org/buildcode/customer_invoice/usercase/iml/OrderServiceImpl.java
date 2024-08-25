package org.buildcode.customer_invoice.usercase.iml;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.buildcode.customer_invoice.api.constants.Address;
import org.buildcode.customer_invoice.api.constants.Item;
import org.buildcode.customer_invoice.data.model.OrderDetailsDataModel;
import org.buildcode.customer_invoice.exception.OrderServiceUnavailableException;
import org.buildcode.customer_invoice.usercase.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final RestTemplate restTemplate;

    @Value("${services.order.scheme}")
    private String orderServiceScheme;

    @Value("${services.order.host}")
    private String orderServiceHost;

    @Value("${services.order.orderDetailsPath}")
    private String orderServiceOrderDetailsPath;


    public OrderServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Retry(name = "orderServiceRetry", fallbackMethod = "orderServiceFallback")
    public OrderDetailsDataModel getOrderDetails(String orderId) {
        return getDummyOrderDetailsDataModel();
//        URI url = UriComponentsBuilder.newInstance()
//                .scheme(orderServiceScheme)
//                .host(orderServiceHost)
//                .path(orderServiceOrderDetailsPath)
//                .buildAndExpand(orderId)
//                .toUri();
//
//        HttpMethod httpMethod = HttpMethod.GET;
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//
//        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
//
//        try {
//            ResponseEntity<OrderDetailsDataModel> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, OrderDetailsDataModel.class);
//            log.info("Fetched the order details for orderId {}: {}", orderId, responseEntity.getBody());
//            return responseEntity.getBody();
//        } catch (RestClientException restClientException) {
//            log.error("Error while fetching order details for orderId: {}", orderId, restClientException);
//            throw restClientException;
//        }
    }

    public OrderDetailsDataModel orderServiceFallback(String orderId, Throwable throwable) {
        log.error("Failed to fetch order details for orderId: {}, after retries. Fallback method invoked.", orderId);
        throw new OrderServiceUnavailableException("Order service is currently unavailable, please try again later.");
    }

    public OrderDetailsDataModel getDummyOrderDetailsDataModel() {
        Address billingAddress = new Address();
        billingAddress.setLine1("123 Main Street");
        billingAddress.setLine2("Apt 4B");
        billingAddress.setCity("Metropolis");
        billingAddress.setState("NY");
        billingAddress.setZipCode("12345");
        billingAddress.setCountry("USA");

        Address shippingAddress = new Address();
        shippingAddress.setLine1("456 Elm Street");
        shippingAddress.setLine2("Suite 5A");
        shippingAddress.setCity("Gotham");
        shippingAddress.setState("NY");
        shippingAddress.setZipCode("54321");
        shippingAddress.setCountry("USA");

        Item item1 = new Item();
        item1.setItemId("ITEM123");
        item1.setProductName("Widget");
        item1.setQuantity(2);
        item1.setPricePerUnit(50.00);
        item1.setTotalPrice(item1.getQuantity() * item1.getPricePerUnit());

        Item item2 = new Item();
        item2.setItemId("ITEM456");
        item2.setProductName("Gadget");
        item2.setQuantity(1);
        item2.setPricePerUnit(100.00);
        item2.setTotalPrice(item2.getQuantity() * item2.getPricePerUnit());

        OrderDetailsDataModel orderDetails = new OrderDetailsDataModel();
        orderDetails.setOrderId("ORD987654");
        orderDetails.setCustomerId("CUST123456");
        orderDetails.setOrderDate("2024-08-23");
        orderDetails.setStatus("Processed");
        orderDetails.setItems(Arrays.asList(item1, item2));
        orderDetails.setTotalAmount(200.00);
        orderDetails.setTaxAmount(20.00);
        orderDetails.setGrandTotal(220.00);
        orderDetails.setBillingAddress(billingAddress);
        orderDetails.setShippingAddress(shippingAddress);

        return orderDetails;
    }
}