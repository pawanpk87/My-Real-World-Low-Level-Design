package org.buildcode.customer_invoice.usercase;

import org.buildcode.customer_invoice.data.model.OrderDetailsDataModel;

public interface OrderService {

    OrderDetailsDataModel getOrderDetails(String orderId);

    // other endpoints
}