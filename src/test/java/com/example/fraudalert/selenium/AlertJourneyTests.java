package com.example.fraudalert.selenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Critical user journeys of the Fraud Alert Review Dashboard. */
class AlertJourneyTests extends BaseSeleniumTest {

    @Test
    void journey1_addValidAlert_appearsInList() {
        String txn = uniqueId();

        addAlert(txn, "Amit Verma", "25000.00", "HIGH", "NEW", null);

        String url = getDriver().getCurrentUrl();
        assertTrue(url.contains("/alerts"), "Unexpected page after saving: " + url);
        assertEquals("Alert saved successfully", text("success-message"));
        assertTrue(text("alert-table").contains(txn));
    }

    @Test
    void journey2_invalidAlert_showsErrorsAndSavesNothing() {
        open("/alerts/new");
        type("amount", "-5");
        clickAndWait("save-button");

        assertTrue(exists("alert-form"));
        assertEquals("Transaction ID is required", text("transactionId-error"));
        assertEquals("Customer name is required", text("customerName-error"));
        assertEquals("Amount must be greater than 0", text("amount-error"));
        assertEquals("Risk level is required", text("riskLevel-error"));
    }

    @Test
    void journey3_search_findsOnlyMatchingAlerts() {
        String token = "Zeta" + System.nanoTime();
        String matching = uniqueId();
        addAlert(matching, token + " Sharma", "1500.00", "LOW", "NEW", null);
        addAlert(uniqueId(), "Other Person", "700.00", "LOW", "NEW", null);

        open("/alerts");
        type("q", token);
        clickAndWait("search-button");
        assertEquals("1 alert(s) shown", text("result-count"));
        assertTrue(text("alert-table").contains(matching));

        open("/alerts");
        type("q", "NoSuchCustomer" + System.nanoTime());
        clickAndWait("search-button");
        assertEquals("No alerts found.", text("no-alerts"));
    }

    @Test
    void journey4_dashboardIndicator_matchesDrillDownList() {
        open("/");
        int before = indicator("NEW");

        addAlert(uniqueId(), "Neha Kulkarni", "1200.50", "LOW", "NEW", null);

        open("/");
        assertEquals(before + 1, indicator("NEW"));
        clickAndWait("indicator-NEW");
        assertEquals((before + 1) + " alert(s) shown", text("result-count"));
    }

    @Test
    void journey5_exceptionView_showsHighRiskAndOverdueButNotClosed() {
        String highOpen = uniqueId();
        String overdue = uniqueId();
        String closedHigh = uniqueId();
        String lowOnTime = uniqueId();
        addAlert(highOpen, "Sana Sheikh", "54000.00", "HIGH", "ESCALATED", null);
        addAlert(overdue, "Rohit Patil", "8000.00", "MEDIUM", "UNDER_REVIEW", "2020-01-01");
        addAlert(closedHigh, "Vikram Rao", "300.00", "HIGH", "CLOSED", null);
        addAlert(lowOnTime, "Asha Nair", "90.00", "LOW", "NEW", null);

        open("/exceptions");
        String table = text("exception-table");

        assertTrue(table.contains(highOpen));
        assertTrue(table.contains(overdue));
        assertFalse(table.contains(closedHigh));
        assertFalse(table.contains(lowOnTime));
    }
}
