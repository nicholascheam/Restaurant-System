package com.example.restaurantsystem;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardController {

    @FXML private Label revenueLabel;
    @FXML private Label ordersLabel;
    @FXML private Label bestSellerLabel;
    @FXML private Label lowStockLabel;
    @FXML private PieChart categoryChart;
    @FXML private BarChart<String, Number> ordersChart;
    @FXML private LineChart<String, Number> revenueChart;
    private User currentUser;
    private ReportService reportService = new ReportService();
    private DashboardService dashboardService = new DashboardService();

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
        loadRevenueChart();
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
    private void loadRevenueChart() {

        revenueChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        series.setName("Revenue");

        series.getData().addAll(dashboardService.getLast7DaysRevenue());

        revenueChart.getData().add(series);
    }
    // write each line
    private float writeLine(PDPageContentStream content, int size, boolean bold, float x, float y, String text) throws Exception {
        content.beginText();
        content.setFont(bold ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA, size);
        content.newLineAtOffset(x, y);
        content.showText(text);
        content.endText();
        return y - 18;
    }
    // each section title
    private float sectionTitle(PDPageContentStream content, float y, String title) throws Exception {
        return writeLine(content, 13, true, 50, y, title);
    }

    // export pdf
    @FXML
    private void handleExport() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Business Report");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        fileChooser.setInitialFileName("Restaurant_Report_" + LocalDate.now() + ".pdf");

        File file = fileChooser.showSaveDialog(revenueLabel.getScene().getWindow());

        if (file == null) return;

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDPageContentStream content = new PDPageContentStream(document, page);

            float y = 780;


            y = writeLine(content, 18, true, 50, y,
                    "RESTAURANT BUSINESS REPORT");

            y -= 10;
            y = writeLine(content, 10, false, 50, y,
                    "Generated: " + LocalDateTime.now());

            y -= 25;

            y = sectionTitle(content, y, "BUSINESS SUMMARY");

            y = writeLine(content, 11, false, 55, y,
                    "Revenue Today: RM " + dashboardService.getRevenueToday());

            y = writeLine(content, 11, false, 55, y,
                    "Revenue Last 7 Days: RM " + dashboardService.getRevenueLast7Days());

            y = writeLine(content, 11, false, 55, y,
                    "Orders Today: " + dashboardService.getOrdersToday());

            y = writeLine(content, 11, false, 55, y,
                    "Orders Last 7 Days: " + dashboardService.getOrdersLast7Days());

            y = writeLine(content, 11, false, 55, y,
                    "Average Order Value: RM " + dashboardService.getAverageOrderValue());

            y -= 15;

            y = sectionTitle(content, y, "TOP SELLING ITEMS");

            for (String row : dashboardService.getTopSellingItems()) {
                y = writeLine(content, 11, false, 55, y, row);
            }

            y -= 15;

            y = sectionTitle(content, y, "LOW STOCK ITEMS");

            for (String row : dashboardService.getLowStockItems()) {
                y = writeLine(content, 11, false, 55, y, row);
            }

            y -= 15;


            y = sectionTitle(content, y, "7 DAY PERFORMANCE");

            for (String row : dashboardService.getLast7DayPerformance()) {
                y = writeLine(content, 11, false, 55, y, row);
            }

            y -= 15;


            y = sectionTitle(content, y, "BUSINESS INSIGHTS");

            for (String row : dashboardService.getBusinessInsights()) {
                y = writeLine(content, 11, false, 55, y, row);
            }

            content.close();

            document.save(file);

            AlertUtil.info("Report exported successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.info("Export failed.");
        }
    }
    @FXML
    private void goBack() {
        AdminController controller = SceneSwitcher.switchScene(revenueLabel, "Admin.fxml");
        controller.setUser(currentUser);
    }
}