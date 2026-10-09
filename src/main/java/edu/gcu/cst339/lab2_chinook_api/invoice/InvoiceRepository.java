package edu.gcu.cst339.lab2_chinook_api.invoice;

import org.springframework.data.jpa.repository.JpaRepository;

interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    
}