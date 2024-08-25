package org.buildcode.customer_invoice.api.constants;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}