package org.buildcode.customer_invoice.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.buildcode.customer_invoice.api.constants.Address;
import org.buildcode.customer_invoice.api.constants.Item;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDetailsDataModel {
    private String orderId;
    private String customerId;
    private String orderDate;
    private String status;
    private List<Item> items;
    private double totalAmount;
    private double taxAmount;
    private double grandTotal;
    private Address shippingAddress;
    private Address billingAddress;
}