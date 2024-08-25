package org.buildcode.customer_invoice.usercase;

import org.buildcode.customer_invoice.api.model.GenerateOrderInvoiceRequestModel;
import org.buildcode.customer_invoice.api.model.InvoiceDetailsResponseModel;

public interface OrderInvoiceService {
    
    InvoiceDetailsResponseModel generateInvoice(GenerateOrderInvoiceRequestModel requestModel);
}