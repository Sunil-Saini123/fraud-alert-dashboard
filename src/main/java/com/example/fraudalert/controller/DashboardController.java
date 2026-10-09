package com.example.fraudalert.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.fraudalert.model.AlertStatus;
import com.example.fraudalert.service.AlertService;

@Controller
public class DashboardController {

    private final AlertService alertService;

    public DashboardController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        Map<AlertStatus, Long> counts = alertService.countByStatus();
        long total = counts.values().stream().mapToLong(Long::longValue).sum();
        model.addAttribute("counts", counts);
        model.addAttribute("total", total);
        model.addAttribute("exceptionCount", alertService.findExceptions().size());
        return "dashboard";
    }

    @GetMapping("/exceptions")
    public String exceptions(Model model) {
        model.addAttribute("alerts", alertService.findExceptions());
        return "exceptions";
    }
}
