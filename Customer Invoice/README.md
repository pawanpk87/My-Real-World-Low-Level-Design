# Customer Invoice

## Problem Statement
**Existing Service:**
- **Service Name:** customer-invoice
- **Functionality:** Generates and stores customer invoices based on order information.

**New Requirements:**
- Implement a feature to automatically send invoice notifications to customers via email after the invoice is generated.

*Note: The analysis provided here will be conducted using dummy data, but the design will be applicable in real scenarios.*

## Scope

**Focus Areas:**
- We will focus on the invoice generation process and the new email notification feature.
- Authentication, authorization, and other endpoints are out of scope for this design.

**Assumptions:**
- For simplicity, let's assume there are already three existing services:
  - **Order Service:** Uses the customer-invoice endpoint to generate invoices.
  - **Customer-Email Service:** Sends invoice notifications to customers via email.
  - **Customer Service:** Stores customer-related information.

## Event Flow
- The customer-email service listens to the AWS SQS queue `INVOICE_EMAIL`. This queue is subscribed to the SNS topic `INVOICE_NOTIFICATION`.
- A filter policy is added to the SNS topic for the `INVOICE_EMAIL` SQS queue. The filter policy matches only messages with the `eventType` of `invoice_email` and sends them to the `INVOICE_EMAIL` queue.
- To trigger an email notification, the customer-invoice service publishes a message to the SNS topic `INVOICE_NOTIFICATION` with the `eventType` set to `invoice_email`.

## High-Level Design for Sending Invoice Emails

![Screenshot 2024-08-26 011641](https://github.com/user-attachments/assets/d5975fc4-6e00-4c40-98ca-8def02667df7)


# Low-Level Design - Invoice Generation

**Authors:** Pawan Mehta  
**Reviewers:** N/A

## Objective
We need to create an endpoint that generates invoices, which will be called by the order service.

## Order Service's Endpoint to Fetch Order Details

**Endpoint:** `api/order/v1/{orderId}`

### Success Response
```json
{
  "resCode": 200,
  "message": "Order details retrieved successfully.",
  "data": {
    "orderId": "ORD123456",
    "customerId": "CUST7890",
    "orderDate": "2024-08-20",
    "status": "Completed",
    "items": [
      {
        "itemId": "ITEM001",
        "productName": "Wireless Mouse",
        "quantity": 2,
        "pricePerUnit": 25.00,
        "totalPrice": 50.00
      },
      {
        "itemId": "ITEM002",
        "productName": "Keyboard",
        "quantity": 1,
        "pricePerUnit": 45.00,
        "totalPrice": 45.00
      }
    ],
    "totalAmount": 95.00,
    "taxAmount": 9.50,
    "grandTotal": 104.50,
    "shippingAddress": {
      "line1": "123 Main Street",
      "line2": "Apt 4B",
      "city": "Metropolis",
      "state": "NY",
      "zipCode": "12345",
      "country": "USA"
    },
    "billingAddress": {
      "line1": "123 Main Street",
      "line2": "Apt 4B",
      "city": "Metropolis",
      "state": "NY",
      "zipCode": "12345",
      "country": "USA"
    }
  }
}
```

When an invoice is successfully generated and stored in the database, a message should be published to the SNS topic `INVOICE_NOTIFICATION`. The message should include the relevant payload with the following fields:

- **eventType**: This key is used by the SNS filtering policy to route the message to the appropriate SQS queue. Use `"eventType": "invoice_email"` for email notifications. For push notifications, use `"eventType": "invoice_push"`.
- **invoiceId**: The unique identifier for the generated invoice.
- **orderId**: The ID of the order associated with the invoice.
- **customerId**: The ID of the customer who will receive the invoice.
- **totalAmount, taxAmount, grandTotal**: Financial details of the invoice.
- **billingAddress**: The billing address associated with the customer and order.
- **invoiceDate**: The date when the invoice was generated.
- **status**: The current status of the invoice (e.g., 'Generated').

## Example Payload

```json
{
  "eventType": "invoice_email",
  "invoiceId": "INV987654321",
  "orderId": "ORD123456",
  "customerId": "CUST7890",
  "totalAmount": 250.00,
  "taxAmount": 25.00,
  "grandTotal": 275.00,
  "billingAddress": {
    "line1": "123 Main Street",
    "line2": "Apt 4B",
    "city": "Metropolis",
    "state": "NY",
    "zipCode": "12345",
    "country": "USA"
  },
  "invoiceDate": "2024-08-22",
  "status": "Generated"
}
```

# Database Design

## Table: INVOICE

| Column Name    | Data Type      | Description                                               |
|----------------|----------------|-----------------------------------------------------------|
| `id`            | VARCHAR(50)    | Unique identifier for the invoice (Primary Key).         |
| `orderId`       | VARCHAR(50)    | ID of the order associated with this invoice.            |
| `customerId`    | VARCHAR(50)    | ID of the customer to whom the invoice belongs.          |
| `totalAmount`   | DECIMAL(10,2)  | Total amount before taxes.                               |
| `taxAmount`     | DECIMAL(10,2)  | Amount of tax applied to the total amount.               |
| `grandTotal`    | DECIMAL(10,2)  | Final amount after taxes.                                |
| `billingAddress`| TEXT           | Billing address of the customer.                         |
| `invoiceDate`   | DATE           | Date when the invoice was generated.                     |
| `status`        | VARCHAR(20)    | Status of the invoice (e.g., 'Generated', 'Sent').       |
| `createdAt`     | TIMESTAMP      | Timestamp when the invoice was created.                  |
| `updatedAt`     | TIMESTAMP      | Timestamp when the invoice was last updated.             |


# API Endpoint

## Endpoint

- **Method:** POST
- **URL:** `/api/invoice/v1/order`

## Payload Structure

```json
{
  "orderId": "ORD123456",
  "invoiceType": "Standard"
}
```

## Functionality

1. **Generate an Invoice**: Create an invoice for the specified `orderId`.
2. **Fetch Order Details**: Retrieve order details using the `orderId`.
3. **Store Invoice**: Save the generated invoice in the database.
4. **Send Email Notification**: Dispatch an email notification to the customer associated with the order.

## Response

The response from the invoice generation and notification process will be a JSON object with the following structure:

```json
{
  "invoiceId": "INV987654321",
  "orderId": "ORD123456",
  "customerId": "CUST7890",
  "totalAmount": 250.00,
  "taxAmount": 25.00,
  "grandTotal": 275.00,
  "billingAddress": {
    "line1": "123 Main Street",
    "line2": "Apt 4B",
    "city": "Metropolis",
    "state": "NY",
    "zipCode": "12345",
    "country": "USA"
  },
  "invoiceDate": "2024-08-22",
  "status": "Sent",
  "message": "Invoice generated and email sent successfully."
}
```

## Error Responses

The following error responses may be returned by the system:

- **ErrorCode: ORDER_NOT_FOUND**
  - **Message**: "The specified order was not found."
  - **HTTP Status**: 404

- **ErrorCode: INVALID_CUSTOMER_ID**
  - **Message**: "The customer ID provided is invalid."
  - **HTTP Status**: 400

- **ErrorCode: INVOICE_GENERATION_FAILED**
  - **Message**: "Failed to generate the invoice. Please try again later."
  - **HTTP Status**: 500

- **ErrorCode: EMAIL_SEND_FAILURE**
  - **Message**: "Invoice generated, but failed to send the email notification."
  - **HTTP Status**: 500

### Example Error Response

An example of an error response is as follows:

```json
{
  "resCode": 400,
  "errorCode": "INVALID_CUSTOMER_ID",
  "message": "The customer ID provided is invalid."
}
```

## Activity Diagram

![Screenshot 2024-08-26 005458](https://github.com/user-attachments/assets/04ea1dec-755c-4af5-965b-ab6801273fa1)
![Screenshot 2024-08-26 005527](https://github.com/user-attachments/assets/3dbdd16f-ac45-4c7e-baf5-cad9271ef13c)


# Development

## All Development Setups

### AWS Setup

1. **Run LocalStack:**

   ```bash
   docker run -d --rm -it -p 127.0.0.1:4566:4566 -p 127.0.0.1:4510-4559:4510-4559 -v /var/run/docker.sock:/var/run/docker.sock localstack/localstack
   ```

# AWS LocalStack Setup

## Docker Commands

1. List running Docker containers:
    ```bash
    docker ps
    ```

2. Access the container's bash shell:
    ```bash
    docker exec -it f128edc30a11 bash
    ```

## Setting Up AWS LocalStack

1. **Create the SNS Topic `INVOICE_NOTIFICATION`**

    ```bash
    awslocal sns create-topic --name INVOICE_NOTIFICATION
    ```

2. **Create the SQS Queues**

    - Create `INVOICE_EMAIL` queue:
        ```bash
        awslocal sqs create-queue --queue-name INVOICE_EMAIL
        ```

    - Create `INVOICE_PUSH_NOTIFICATION` queue:
        ```bash
        awslocal sqs create-queue --queue-name INVOICE_PUSH_NOTIFICATION
        ```

3. **Subscribe the SQS Queues to the SNS Topic**

    First, get the ARNs for the SQS queues:

    - Get ARN for `INVOICE_EMAIL` queue:
        ```bash
        QUEUE_EMAIL_ARN=$(awslocal sqs get-queue-attributes --queue-url http://localhost:4566/000000000000/INVOICE_EMAIL --attribute-name QueueArn --query 'Attributes.QueueArn' --output text)
        ```

    - Get ARN for `INVOICE_PUSH_NOTIFICATION` queue:
        ```bash
        QUEUE_PUSH_ARN=$(awslocal sqs get-queue-attributes --queue-url http://localhost:4566/000000000000/INVOICE_PUSH_NOTIFICATION --attribute-name QueueArn --query 'Attributes.QueueArn' --output text)
        ```

# SNS Subscription Setup

To subscribe your SQS queues to the SNS topic, use the following commands:

## Subscribe INVOICE_EMAIL Queue

```sh
awslocal sns subscribe \
    --topic-arn arn:aws:sns:us-east-1:000000000000:INVOICE_NOTIFICATION \
    --protocol sqs \
    --notification-endpoint $QUEUE_EMAIL_ARN
```

# SNS Subscription Setup

To subscribe the `INVOICE_PUSH_NOTIFICATION` queue to the SNS topic, use the following command:

```sh
awslocal sns subscribe \
    --topic-arn arn:aws:sns:us-east-1:000000000000:INVOICE_NOTIFICATION \
    --protocol sqs \
    --notification-endpoint $QUEUE_PUSH_ARN
```


# Add Filter Policies to the Subscriptions

First, you need to get the subscription ARNs:

## List Subscriptions

Run the following command to list all subscriptions and find their ARNs:

```sh
awslocal sns list-subscriptions
```

```json
{
    "Subscriptions": [
        {
            "SubscriptionArn": "arn:aws:sns:us-east-1:000000000000:INVOICE_NOTIFICATION:4de83d65-8e58-42b1-a749-3fe9a6cd3e83",
            "Owner": "000000000000",
            "Protocol": "sqs",
            "Endpoint": "arn:aws:sqs:us-east-1:000000000000:INVOICE_EMAIL",
            "TopicArn": "arn:aws:sns:us-east-1:000000000000:INVOICE_NOTIFICATION"
        },
        {
            "SubscriptionArn": "arn:aws:sns:us-east-1:000000000000:INVOICE_NOTIFICATION:c2c4f634-458b-48b1-bbc0-8278d00f43af",
            "Owner": "000000000000",
            "Protocol": "sqs",
            "Endpoint": "arn:aws:sqs:us-east-1:000000000000:INVOICE_PUSH_NOTIFICATION",
            "TopicArn": "arn:aws:sns:us-east-1:000000000000:INVOICE_NOTIFICATION"
        }
    ]
}
```

# AWS SNS Filter Policy Configuration

To set the filter policies for your SNS subscriptions, follow the steps below:

## Setting Filter Policy for `INVOICE_EMAIL` Queue

Run the following command to set the filter policy for the `INVOICE_EMAIL` queue:

```bash
awslocal sns set-subscription-attributes --subscription-arn arn:aws:sns:us-east-1:000000000000:INVOICE_NOTIFICATION:4de83d65-8e58-42b1-a749-3fe9a6cd3e83 --attribute-name FilterPolicy --attribute-value '{"eventType": ["invoice_email"]}'
```

## Set Filter Policy for INVOICE_PUSH_NOTIFICATION Queue

To set the filter policy for the `INVOICE_PUSH_NOTIFICATION` queue, use the following AWS CLI command:

```sh
awslocal --endpoint-url=http://localhost:4566 sns set-subscription-attributes --subscription-arn arn:aws:sns:us-east-1:000000000000:INVOICE_NOTIFICATION:c2c4f634-458b-48b1-bbc0-8278d00f43af --attribute-name FilterPolicy --attribute-value '{"eventType": ["invoice_push"]}'
```


# Testing in LocalStack CLI

## Publish Messages with Attributes

### Publish an invoice email message

```bash
awslocal sns publish --topic-arn arn:aws:sns:us-east-1:000000000000:INVOICE_NOTIFICATION --message "Invoice #12345 email has been sent." --message-attributes '{"eventType":{"DataType":"String","StringValue":"invoice_email"}}'
```

### Publish an Invoice Push Notification Message

To publish an invoice push notification message using LocalStack, use the following command:

```bash
awslocal --endpoint-url=http://localhost:4566 sns publish --topic-arn arn:aws:sns:us-east-1:000000000000:INVOICE_NOTIFICATION --message "Invoice #12345 push notification has been sent." --message-attributes '{"eventType":{"DataType":"String","StringValue":"invoice_push"}}'
```



## Verify the Setup

Check the messages in the queues to ensure they received the filtered messages:

#### Receive messages from INVOICE_EMAIL queue
```bash
awslocal sqs receive-message --queue-url http://localhost:4566/000000000000/INVOICE_EMAIL
```

### Receive messages from INVOICE_PUSH_NOTIFICATION queue

```bash
awslocal sqs receive-message --queue-url http://localhost:4566/000000000000/INVOICE_PUSH_NOTIFICATION
```


# Failed to connect to service endpoint

The error you're encountering indicates that the AWS SDK is unable to locate the necessary AWS credentials for authentication. Since you're using LocalStack, which emulates AWS services locally, you typically don't need real AWS credentials. However, the AWS SDK still expects some credentials to be present.

**Solution:** [link](https://github.com/pawanpk87/My-Real-World-Low-Level-Design/blob/65a81a9660829c12357fd10749773dc08a68cedf/Customer%20Invoice/customer-invoice/src/main/java/org/buildcode/customer_invoice/aws/AWSConfig.java#L32)
