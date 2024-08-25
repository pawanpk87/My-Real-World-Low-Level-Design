package org.buildcode.customer_invoice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.buildcode.customer_invoice.api.constants.InvoiceType;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerateOrderInvoiceRequestModel {
    private String orderId;
    private InvoiceType invoiceType;
}