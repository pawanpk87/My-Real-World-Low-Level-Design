package org.buildcode.customer_invoice.usercase.iml;

import lombok.extern.slf4j.Slf4j;
import org.buildcode.customer_invoice.api.model.GenerateOrderInvoiceRequestModel;
import org.buildcode.customer_invoice.api.model.InvoiceDetailsResponseModel;
import org.buildcode.customer_invoice.data.entity.Invoice;
import org.buildcode.customer_invoice.data.mapper.InvoiceMapper;
import org.buildcode.customer_invoice.data.model.OrderDetailsDataModel;
import org.buildcode.customer_invoice.exception.CustomerInvoiceGenerationException;
import org.buildcode.customer_invoice.repository.InvoiceRepository;
import org.buildcode.customer_invoice.usercase.OrderInvoiceService;
import org.buildcode.customer_invoice.usercase.OrderService;
import org.buildcode.customer_invoice.utils.AWSUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderInvoiceServiceImpl implements OrderInvoiceService {

    private final OrderService orderService;

    private final InvoiceMapper invoiceMapper;

    private final InvoiceRepository invoiceRepository;

    private final AWSUtils awsUtils;

    public OrderInvoiceServiceImpl(
            OrderService orderService,
            InvoiceMapper invoiceMapper,
            InvoiceRepository invoiceRepository,
            AWSUtils awsUtils
    ) {
        this.orderService = orderService;
        this.invoiceMapper = invoiceMapper;
        this.invoiceRepository = invoiceRepository;
        this.awsUtils = awsUtils;
    }

    @Override
    @Transactional
    public InvoiceDetailsResponseModel generateInvoice(GenerateOrderInvoiceRequestModel requestModel) {
        String orderId = requestModel.getOrderId();
        try {
            // fetch the order details
            OrderDetailsDataModel orderDetails = orderService.getOrderDetails(orderId);

            // create invoice
            Invoice invoice = invoiceMapper.toInvoice(orderDetails, requestModel.getInvoiceType());
            invoice = invoiceRepository.save(invoice);

            // Publish message to AWS SNS
            awsUtils.publishInvoiceEmailNotification(invoiceMapper.toInvoiceDto(invoice));

            return invoiceMapper.toInvoiceDetailsResponseModel(invoice);
        } catch (DataAccessException dataAccessException) {
            log.error("Error saving invoice for orderId: {}", orderId, dataAccessException);
            throw new CustomerInvoiceGenerationException("Error occurred while generating the invoice.");
        } catch (Exception exception) {
            log.error("Unexpected error while processing orderId: {}", orderId, exception);
            throw new CustomerInvoiceGenerationException("An unexpected error occurred during invoice generation.");
        }
    }
}