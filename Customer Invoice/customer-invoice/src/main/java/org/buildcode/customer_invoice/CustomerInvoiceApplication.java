package org.buildcode.customer_invoice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerInvoiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(CustomerInvoiceApplication.class, args);
		System.out.println("Customer Invoice service started...");
	}
}