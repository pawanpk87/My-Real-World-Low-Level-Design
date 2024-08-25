package org.buildcode.customer_invoice.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import org.buildcode.customer_invoice.api.constants.Address;
import org.buildcode.customer_invoice.api.constants.InvoiceStatus;
import org.buildcode.customer_invoice.api.constants.InvoiceType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "INVOICE")
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orderId", nullable = false)
    private String orderId;

    @Column(name = "customerId", nullable = false)
    private String customerId;

    @Column(name = "totalAmount", nullable = false)
    private Double totalAmount;

    @Column(name = "taxAmount", nullable = false)
    private Double taxAmount;

    @Column(name = "grandTotal", nullable = false)
    private Double grandTotal;

    @Column(name = "billingAddress", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Address billingAddress;

    @Column(name = "invoiceDate", nullable = false)
    private Instant invoiceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private InvoiceType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InvoiceStatus status;

    @Column(name = "createdAt", updatable = false, nullable = false)
    private Instant createdAt;

    @Column(name = "updatedAt", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void prePersist() {
        if(this.createdAt == null) {
            this.createdAt = Instant.now();
            this.updatedAt = Instant.now();
        }
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = Instant.now();
    }
}