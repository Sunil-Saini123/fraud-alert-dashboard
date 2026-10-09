package com.example.fraudalert.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.fraudalert.model.Alert;
import com.example.fraudalert.model.AlertStatus;
import com.example.fraudalert.model.RiskLevel;
import com.example.fraudalert.service.AlertService;

import jakarta.validation.Valid;

@Controller
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/alerts")
    public String list(Model model) {
        model.addAttribute("alerts", alertService.findAll());
        return "alerts";
    }

    @GetMapping("/alerts/new")
    public String newForm(Model model) {
        model.addAttribute("alert", new Alert());
        addOptions(model);
        return "alert-form";
    }

    @PostMapping("/alerts")
    public String save(@Valid @ModelAttribute("alert") Alert alert, BindingResult result,
                       Model model, RedirectAttributes redirectAttributes) {
        // Reject a duplicate transaction ID with a form error instead of a database error
        if (!result.hasFieldErrors("transactionId")
                && alertService.transactionIdExists(alert.getTransactionId())) {
            result.rejectValue("transactionId", "duplicate",
                    "An alert with this transaction ID already exists");
        }
        if (result.hasErrors()) {
            addOptions(model);
            return "alert-form";
        }
        alertService.save(alert);
        redirectAttributes.addFlashAttribute("message", "Alert saved successfully");
        return "redirect:/alerts";
    }

    private void addOptions(Model model) {
        model.addAttribute("riskLevels", RiskLevel.values());
        model.addAttribute("statuses", AlertStatus.values());
    }
}
