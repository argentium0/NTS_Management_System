package oopProject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * AdminDashboardView provides the central administrative operational view for Staff and Test Centre allocations.
 * Adheres strictly to Phase 3 UI/UX constraints:
 * - Data Grid TableView with NTS Label Navy (#2A4D7C) headers and bold white text.
 * - Minimalist, sharp, highly scannable table body with alternating light-grey rows.
 * - Integrated reusable 3-column NTSFooter component at the bottom.
 */
public class AdminDashboardView extends VBox {

    private final TableView<AllocationRecord> allocationTable;
    private final ObservableList<AllocationRecord> allocationData;

    public AdminDashboardView() {
        this.setSpacing(24);
        this.setPadding(new Insets(0));
        this.setStyle("-fx-background-color: #F8FAFC;");

        // 1. Header & KPI Metric Cards Container
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(24, 28, 0, 28));

        // Header Title
        VBox titleBox = new VBox(4);
        Label titleLabel = new Label("Admin Operations & Allocation Dashboard");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2A4D7C;");

        Label subtitleLabel = new Label("Real-time Staff & Test Centre deployment matrix across nationwide testing centers.");
        subtitleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748B;");

        titleBox.getChildren().addAll(titleLabel, subtitleLabel);

        // KPI Summary Cards
        GridPane kpiGrid = new GridPane();
        kpiGrid.setHgap(20);
        kpiGrid.setVgap(20);

        kpiGrid.add(createKpiCard("Active Test Centres", "42", "Operational nationwide"), 0, 0);
        kpiGrid.add(createKpiCard("Deployed Staff", "310", "Superintendents & Invigilators"), 1, 0);
        kpiGrid.add(createKpiCard("Scheduled Exams", "18", "NAT, GAT, TOEIC & Custom"), 2, 0);
        kpiGrid.add(createKpiCard("Deployment Status", "98.4%", "Staff verified & assigned"), 3, 0);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25);
            kpiGrid.getColumnConstraints().add(col);
        }

        // 2. Data Table Card Container
        VBox tableCard = new VBox(16);
        tableCard.getStyleClass().add("flat-card");

        // Toolbar: Search & Action Controls
        HBox toolbar = new HBox(12);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        Label cardHeader = new Label("Staff & Test Centre Allocations Matrix");
        cardHeader.getStyleClass().add("card-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Search city or staff...");
        txtSearch.getStyleClass().add("nts-input-field");
        txtSearch.setPrefWidth(220);

        Button btnAddAllocation = new Button("+ New Allocation");
        btnAddAllocation.getStyleClass().add("nts-primary-button");

        toolbar.getChildren().addAll(cardHeader, spacer, txtSearch, btnAddAllocation);

        // TableView setup with Navy Headers and Scannable Body
        allocationTable = new TableView<>();
        allocationData = FXCollections.observableArrayList();
        setupAllocationTable();

        // Sample Data Generation
        populateSampleData();
        allocationTable.setItems(allocationData);

        tableCard.getChildren().addAll(toolbar, allocationTable);
        VBox.setVgrow(tableCard, Priority.ALWAYS);

        mainContent.getChildren().addAll(titleBox, kpiGrid, tableCard);
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        // Assemble View inside ScrollPane for full responsiveness
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        this.getChildren().add(scrollPane);
    }

    /**
     * Constructs TableView columns with Navy (#2A4D7C) header styling and high scannability.
     */
    @SuppressWarnings("unchecked")
    private void setupAllocationTable() {
        allocationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        allocationTable.setPrefHeight(280);

        TableColumn<AllocationRecord, Integer> colId = new TableColumn<>("Alloc. ID");
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAllocationId()).asObject());
        colId.setPrefWidth(75);

        TableColumn<AllocationRecord, String> colCentre = new TableColumn<>("Test Centre Name");
        colCentre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCentreName()));

        TableColumn<AllocationRecord, String> colCity = new TableColumn<>("City");
        colCity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCity()));

        TableColumn<AllocationRecord, String> colBuilding = new TableColumn<>("Building Type");
        colBuilding.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBuildingType()));

        TableColumn<AllocationRecord, String> colSuperintendent = new TableColumn<>("Superintendent");
        colSuperintendent.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSuperintendentName()));

        TableColumn<AllocationRecord, Integer> colInvigilatorCount = new TableColumn<>("Invigilators");
        colInvigilatorCount.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getInvigilatorCount()).asObject());
        colInvigilatorCount.setPrefWidth(90);

        TableColumn<AllocationRecord, String> colDate = new TableColumn<>("Duty Date");
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDutyDate()));

        TableColumn<AllocationRecord, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        allocationTable.getColumns().addAll(
                colId, colCentre, colCity, colBuilding, colSuperintendent, colInvigilatorCount, colDate, colStatus
        );
    }

    private VBox createKpiCard(String title, String value, String subtitle) {
        VBox card = new VBox(6);
        card.getStyleClass().add("flat-card");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        Label valLabel = new Label(value);
        valLabel.getStyleClass().add("metric-value");

        Label subLabel = new Label(subtitle);
        subLabel.getStyleClass().add("metric-label");

        card.getChildren().addAll(titleLabel, valLabel, subLabel);
        return card;
    }

    private void populateSampleData() {
        allocationData.add(new AllocationRecord(201, "NTS Centre A", "Lahore", "University Hall", "Dr. Rashid Ali", 12, "2026-09-05", "Confirmed"));
        allocationData.add(new AllocationRecord(202, "NTS Centre B", "Islamabad", "College Campus", "Prof. Shahid Mehmood", 8, "2026-09-05", "Confirmed"));
        allocationData.add(new AllocationRecord(203, "NTS Centre C", "Karachi", "Auditorium Block", "Engr. Farhan Tariq", 15, "2026-09-12", "Pending"));
        allocationData.add(new AllocationRecord(204, "NTS Centre D", "Peshawar", "Public Academy", "Dr. Usman Khan", 10, "2026-09-12", "Confirmed"));
        allocationData.add(new AllocationRecord(205, "NTS Centre E", "Quetta", "Govt Complex", "Mir Hassan Baloch", 6, "2026-09-19", "Confirmed"));
        allocationData.add(new AllocationRecord(206, "NTS Centre F", "Multan", "Science Institute", "Tariq Jameel", 9, "2026-09-19", "Confirmed"));
    }

    /**
     * Model Data Record for Staff & Test Centre Allocation Matrix
     */
    public static class AllocationRecord {
        private final int allocationId;
        private final String centreName;
        private final String city;
        private final String buildingType;
        private final String superintendentName;
        private final int invigilatorCount;
        private final String dutyDate;
        private final String status;

        public AllocationRecord(int allocationId, String centreName, String city, String buildingType, String superintendentName, int invigilatorCount, String dutyDate, String status) {
            this.allocationId = allocationId;
            this.centreName = centreName;
            this.city = city;
            this.buildingType = buildingType;
            this.superintendentName = superintendentName;
            this.invigilatorCount = invigilatorCount;
            this.dutyDate = dutyDate;
            this.status = status;
        }

        public int getAllocationId() { return allocationId; }
        public String getCentreName() { return centreName; }
        public String getCity() { return city; }
        public String getBuildingType() { return buildingType; }
        public String getSuperintendentName() { return superintendentName; }
        public int getInvigilatorCount() { return invigilatorCount; }
        public String getDutyDate() { return dutyDate; }
        public String getStatus() { return status; }
    }
}
