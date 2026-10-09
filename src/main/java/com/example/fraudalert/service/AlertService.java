package com.example.fraudalert.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.fraudalert.model.Alert;
import com.example.fraudalert.model.AlertStatus;
import com.example.fraudalert.model.RiskLevel;
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

    public List<Alert> search(String q, AlertStatus status, RiskLevel riskLevel) {
        String term = (q == null) ? "" : q.trim().toLowerCase();
        return findAll().stream()
                .filter(a -> term.isEmpty()
                        || a.getTransactionId().toLowerCase().contains(term)
                        || a.getCustomerName().toLowerCase().contains(term))
                .filter(a -> status == null || a.getStatus() == status)
                .filter(a -> riskLevel == null || a.getRiskLevel() == riskLevel)
                .toList();
    }

    public boolean transactionIdExists(String transactionId) {
        return alertRepository.existsByTransactionId(transactionId);
    }

    public Alert save(Alert alert) {
        return alertRepository.save(alert);
    }
}
