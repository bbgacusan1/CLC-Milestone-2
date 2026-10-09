package edu.gcu.cst339.lab2_chinook_api.invoiceline;

import org.springframework.data.jpa.repository.JpaRepository;

interface InvoiceLineRepository extends JpaRepository<InvoiceLine, Integer> {
    
}