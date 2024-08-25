package org.buildcode.customer_invoice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.buildcode.customer_invoice.api.constants.Address;
import org.buildcode.customer_invoice.api.constants.InvoiceStatus;
import org.buildcode.customer_invoice.api.constants.InvoiceType;

import java.time.Instant;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceDetailsResponseModel {
    private String id;
    private String orderId;
    private String customerId;
    private Double totalAmount;
    private Double taxAmount;
    private Double grandTotal;
    private Address billingAddress;
    private Instant invoiceDate;
    private InvoiceType type;
    private InvoiceStatus status;
    private Instant createdAt;
}