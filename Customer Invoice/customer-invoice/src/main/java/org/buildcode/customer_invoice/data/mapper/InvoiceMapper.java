package org.buildcode.customer_invoice.data.mapper;

import org.buildcode.customer_invoice.api.constants.InvoiceStatus;
import org.buildcode.customer_invoice.api.constants.InvoiceType;
import org.buildcode.customer_invoice.api.model.InvoiceDetailsResponseModel;
import org.buildcode.customer_invoice.data.dto.InvoiceDto;
import org.buildcode.customer_invoice.data.entity.Invoice;
import org.buildcode.customer_invoice.data.model.OrderDetailsDataModel;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class InvoiceMapper {

    public Invoice toInvoice(OrderDetailsDataModel orderDetails, InvoiceType invoiceType) {
        Invoice invoice = new Invoice();
        invoice.setOrderId(orderDetails.getOrderId());
        invoice.setCustomerId(orderDetails.getCustomerId());
        invoice.setTotalAmount(orderDetails.getTotalAmount());
        invoice.setTaxAmount(orderDetails.getTaxAmount());
        invoice.setGrandTotal(orderDetails.getGrandTotal());
        invoice.setBillingAddress(orderDetails.getBillingAddress());
        invoice.setInvoiceDate(Instant.now());
        invoice.setType(invoiceType);
        invoice.setStatus(InvoiceStatus.PENDING);
        return invoice;
    }

    public InvoiceDetailsResponseModel toInvoiceDetailsResponseModel(Invoice invoice) {
        InvoiceDetailsResponseModel invoiceDetailsResponseModel = new InvoiceDetailsResponseModel();
        invoiceDetailsResponseModel.setId(invoice.getId().toString());
        invoiceDetailsResponseModel.setOrderId(invoice.getOrderId());
        invoiceDetailsResponseModel.setCustomerId(invoice.getCustomerId());
        invoiceDetailsResponseModel.setTotalAmount(invoice.getTotalAmount());
        invoiceDetailsResponseModel.setTaxAmount(invoice.getTaxAmount());
        invoiceDetailsResponseModel.setGrandTotal(invoice.getGrandTotal());
        invoiceDetailsResponseModel.setBillingAddress(invoice.getBillingAddress());
        invoiceDetailsResponseModel.setInvoiceDate(invoice.getInvoiceDate());
        invoiceDetailsResponseModel.setType(invoice.getType());
        invoiceDetailsResponseModel.setStatus(invoice.getStatus());
        invoiceDetailsResponseModel.setCreatedAt(invoice.getCreatedAt());
        return invoiceDetailsResponseModel;
    }

    public InvoiceDto toInvoiceDto(Invoice invoice) {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(invoice.getId().toString());
        invoiceDto.setOrderId(invoice.getOrderId());
        invoiceDto.setCustomerId(invoice.getCustomerId());
        invoiceDto.setTotalAmount(invoice.getTotalAmount());
        invoiceDto.setTaxAmount(invoice.getTaxAmount());
        invoiceDto.setGrandTotal(invoice.getGrandTotal());
        invoiceDto.setBillingAddress(invoice.getBillingAddress());
        invoiceDto.setInvoiceDate(invoice.getInvoiceDate());
        invoiceDto.setType(invoice.getType());
        invoiceDto.setStatus(invoice.getStatus());
        invoiceDto.setCreatedAt(invoice.getCreatedAt());
        invoiceDto.setUpdatedAt(invoice.getUpdatedAt());
        return invoiceDto;
    }
}