package org.buildcode.customer_invoice.utils;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.buildcode.customer_invoice.data.dto.InvoiceDto;
import org.buildcode.customer_invoice.exception.CustomerInvoiceInternalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class AWSUtils {
    @Value("${invoice.sns.notificationTopicArn}")
    private String invoiceNotificationTopicArn;

    @Value("${invoice.sns.attribute.attributeDataType}")
    private String invoiceNotificationAttributeAttributeDataType;

    @Value("${invoice.sns.attribute.attributeName}")
    private String invoiceNotificationAttributeAttributeName;

    private final AmazonSNS amazonSNS;

    private final ObjectMapper objectMapper;

    public AWSUtils(AmazonSNS amazonSNS, ObjectMapper objectMapper) {
        this.amazonSNS = amazonSNS;
        this.objectMapper = objectMapper;
    }

    private PublishResult publishMessageToSNS(PublishRequest publishRequest) {
        try {
            log.info("Publishing the message to SNS with request: {}", publishRequest);
            PublishResult publishResult = amazonSNS.publish(publishRequest);
            log.info("Message successfully published to SNS: {} ", publishResult);
            return publishResult;
        } catch (Exception exception) {
            log.error("Failed to send message to SNS", exception);
            throw new RuntimeException("Failed to send message to SNS", exception);
        }
    }

    public void publishInvoiceEmailNotification(InvoiceDto invoice) {
        try {
            String messageBody = objectMapper.writeValueAsString(invoice);
            log.info("Serialized invoice to JSON: {}", messageBody);

            Map<String, MessageAttributeValue> messageAttributes = buildInvoiceSNSMessageAttributes();

            PublishRequest publishRequest = new PublishRequest()
                    .withTargetArn(invoiceNotificationTopicArn)
                    .withMessage(messageBody)
                    .withMessageAttributes(messageAttributes);

            PublishResult publishResult = publishMessageToSNS(publishRequest);

            log.info("Invoice email notification published successfully: {}", publishResult);
        } catch (JsonProcessingException exception) {
            log.error("Failed to serialize invoice to JSON: {}", invoice, exception);
            throw new CustomerInvoiceInternalServerException("Error serializing invoice data");
        } catch (Exception exception) {
            log.error("Failed to publish invoice email notification for invoice: {}", invoice, exception);
            throw new CustomerInvoiceInternalServerException("Error publishing invoice email notification");
        }
    }

    private Map<String, MessageAttributeValue> buildInvoiceSNSMessageAttributes() {
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put(
                "eventType",
                new MessageAttributeValue()
                        .withDataType(invoiceNotificationAttributeAttributeDataType)
                        .withStringValue(invoiceNotificationAttributeAttributeName)
        );
        return messageAttributes;
    }
}