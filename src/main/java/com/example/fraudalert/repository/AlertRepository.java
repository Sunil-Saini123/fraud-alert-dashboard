package com.example.fraudalert.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.fraudalert.model.Alert;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    boolean existsByTransactionId(String transactionId);
}
