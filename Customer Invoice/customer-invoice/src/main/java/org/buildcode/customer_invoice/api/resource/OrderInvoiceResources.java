package org.buildcode.customer_invoice.api.resource;

import org.buildcode.customer_invoice.api.constants.ApiConstants;
import org.buildcode.customer_invoice.api.model.GenerateOrderInvoiceRequestModel;
import org.buildcode.customer_invoice.api.model.InvoiceDetailsResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstants.CUSTOMER_INVOICE_SERVICE + ApiConstants.ORDER)
public interface OrderInvoiceResources {

    @PostMapping
    ResponseEntity<InvoiceDetailsResponseModel> generateInvoice(
            @RequestBody @Validated GenerateOrderInvoiceRequestModel requestModel
    );

    // other endpoints
}