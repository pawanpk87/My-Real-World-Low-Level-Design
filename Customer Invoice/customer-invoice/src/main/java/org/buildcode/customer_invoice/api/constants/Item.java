package org.buildcode.customer_invoice.api.constants;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private String itemId;
    private String productName;
    private int quantity;
    private double pricePerUnit;
    private double totalPrice;
}