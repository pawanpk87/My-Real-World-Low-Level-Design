package org.buildcode.customer_invoice.controller;

import lombok.extern.slf4j.Slf4j;
import org.buildcode.customer_invoice.api.model.GenerateOrderInvoiceRequestModel;
import org.buildcode.customer_invoice.api.model.InvoiceDetailsResponseModel;
import org.buildcode.customer_invoice.api.resource.OrderInvoiceResources;
import org.buildcode.customer_invoice.usercase.OrderInvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class OrderInvoiceController implements OrderInvoiceResources {

    private final OrderInvoiceService orderInvoiceService;

    public OrderInvoiceController(OrderInvoiceService orderInvoiceService) {
        this.orderInvoiceService = orderInvoiceService;
    }

    @Override
    public ResponseEntity<InvoiceDetailsResponseModel> generateInvoice(
            GenerateOrderInvoiceRequestModel requestModel
    ) {
        log.info("Received request for invoice generation: {}", requestModel);
        InvoiceDetailsResponseModel responseModel = orderInvoiceService.generateInvoice(requestModel);
        log.info("Invoice successfully generated: {}", responseModel);
        return new ResponseEntity<>(responseModel, HttpStatus.CREATED);
    }
}
