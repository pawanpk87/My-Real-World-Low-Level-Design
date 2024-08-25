package org.buildcode.customer_invoice.repository;

import jakarta.transaction.Transactional;
import org.buildcode.customer_invoice.data.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
}