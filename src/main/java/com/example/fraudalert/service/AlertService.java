package com.example.fraudalert.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.fraudalert.model.Alert;
import com.example.fraudalert.repository.AlertRepository;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<Alert> findAll() {
        return alertRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public boolean transactionIdExists(String transactionId) {
        return alertRepository.existsByTransactionId(transactionId);
    }

    public Alert save(Alert alert) {
        return alertRepository.save(alert);
    }
}
