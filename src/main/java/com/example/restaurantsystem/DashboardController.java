package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML private Label revenueLabel;
    @FXML private Label ordersLabel;
    @FXML private Label bestSellerLabel;
    @FXML private Label lowStockLabel;

    private ReportService reportService = new ReportService();

    @FXML
    public void initialize() {

        revenueLabel.setText("$" + reportService.getRevenueToday());

        ordersLabel.setText(String.valueOf(reportService.getOrdersToday()));

        bestSellerLabel.setText(reportService.getBestSeller());

        lowStockLabel.setText(String.valueOf(reportService.getLowStockCount()));
    }

    @FXML
    private void goBack() {
        SceneSwitcher.switchScene(revenueLabel, "Admin.fxml");
    }
}