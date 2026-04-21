package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML private Label revenueLabel;
    @FXML private Label ordersLabel;
    @FXML private Label bestSellerLabel;
    @FXML private Label lowStockLabel;
    @FXML private PieChart categoryChart;
    @FXML private BarChart<String, Number> ordersChart;
    private User currentUser;
    private ReportService reportService = new ReportService();

    // handing over user details
    public void setUser(User user) {
        this.currentUser = user;
    }
    @FXML
    public void initialize() {

        revenueLabel.setText(String.format("$%.2f", reportService.getRevenueToday()));
        ordersLabel.setText(String.valueOf(reportService.getOrdersToday()));
        bestSellerLabel.setText(reportService.getBestSeller());
        lowStockLabel.setText(String.valueOf(reportService.getLowStockCount()));

        loadCategoryChart();
        loadOrdersChart();
    }
    private void loadCategoryChart() {
        categoryChart.getData().clear();
        categoryChart.getData().addAll(reportService.getCategorySales());
    }
    private void loadOrdersChart() {
        ordersChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        series.setName("Orders");

        series.getData().addAll(reportService.getLast7DaysOrders());

        ordersChart.getData().add(series);
    }
    @FXML
    private void goBack() {
        AdminController controller = SceneSwitcher.switchScene(revenueLabel, "Admin.fxml");
        controller.setUser(currentUser);
    }
}