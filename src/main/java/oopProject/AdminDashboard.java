package oopProject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * AdminDashboard provides the central administrative operational dashboard.
 * Includes a dedicated Settings modal allowing the active admin to change their password.
 */
public class AdminDashboard extends VBox {

    private static final String activeAdminUsername = "admin";
    private static String activeAdminPassword = "admin123";

    private final TableView<AllocationRecord> allocationTable;
    private final ObservableList<AllocationRecord> allocationData;

    public AdminDashboard() {
        setSpacing(24);
        setPadding(new Insets(0));
        setStyle("-fx-background-color: #F8FAFC;");

        // Main Dashboard Content Box
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(24, 28, 0, 28));

        // Header Title & Action Buttons
        HBox headerBox = new HBox(12);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        Label titleLabel = new Label("Admin Executive Dashboard");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2A4D7C;");

        Label subtitleLabel = new Label("System overview, operational metrics, and staff allocation matrix.");
        subtitleLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748B;");

        titleBox.getChildren().addAll(titleLabel, subtitleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnSettings = new Button("Admin Settings (Change Password)");
        btnSettings.getStyleClass().add("btn-secondary");
        btnSettings.setOnAction(e -> openChangePasswordModal());

        headerBox.getChildren().addAll(titleBox, spacer, btnSettings);

        // KPI Summary Cards
        GridPane kpiGrid = new GridPane();
        kpiGrid.setHgap(20);
        kpiGrid.setVgap(20);

        kpiGrid.add(createKpiCard("Active Test Centres", "42", "Operational nationwide"), 0, 0);
        kpiGrid.add(createKpiCard("Deployed Staff", "310", "Superintendents & Invigilators"), 1, 0);
        kpiGrid.add(createKpiCard("Scheduled Exams", "18", "NAT, GAT, TOEIC & Custom"), 2, 0);
        kpiGrid.add(createKpiCard("System Admin", activeAdminUsername, "Active Superuser Account"), 3, 0);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25);
            kpiGrid.getColumnConstraints().add(col);
        }

        // Table Card Container
        VBox tableCard = new VBox(16);
        tableCard.getStyleClass().add("flat-card");

        HBox toolbar = new HBox(12);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        Label cardHeader = new Label("Staff & Test Centre Deployments");
        cardHeader.getStyleClass().add("card-title");

        Region tblSpacer = new Region();
        HBox.setHgrow(tblSpacer, Priority.ALWAYS);

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Search city or staff...");
        txtSearch.getStyleClass().add("nts-input-field");
        txtSearch.setPrefWidth(220);

        toolbar.getChildren().addAll(cardHeader, tblSpacer, txtSearch);

        allocationTable = new TableView<>();
        allocationData = FXCollections.observableArrayList();
        setupAllocationTable();
        populateSampleData();
        allocationTable.setItems(allocationData);

        tableCard.getChildren().addAll(toolbar, allocationTable);
        VBox.setVgrow(tableCard, Priority.ALWAYS);

        mainContent.getChildren().addAll(headerBox, kpiGrid, tableCard);
        VBox.setVgrow(mainContent, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        getChildren().add(scrollPane);
    }

    public static boolean validateAdminCredentials(String username, String password) {
        return activeAdminUsername.equalsIgnoreCase(username) && activeAdminPassword.equals(password);
    }

    public static String getActiveAdminUsername() {
        return activeAdminUsername;
    }

    /**
     * Dedicated Settings Modal allowing the active admin to change their password.
     */
    private void openChangePasswordModal() {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Admin Settings - Change Password");

        VBox modalLayout = new VBox(16);
        modalLayout.setPadding(new Insets(24));
        modalLayout.setStyle("-fx-background-color: #FFFFFF;");

        Label lblModalTitle = new Label("Change Admin Password");
        lblModalTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2A4D7C;");

        PasswordField txtCurrentPass = new PasswordField();
        txtCurrentPass.setPromptText("Enter current password...");

        PasswordField txtNewPass = new PasswordField();
        txtNewPass.setPromptText("Enter new password...");

        PasswordField txtConfirmPass = new PasswordField();
        txtConfirmPass.setPromptText("Confirm new password...");

        Label lblMsg = new Label();
        lblMsg.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        VBox form = new VBox(10);
        form.getChildren().addAll(
                createModalField("Current Password", txtCurrentPass),
                createModalField("New Password", txtNewPass),
                createModalField("Confirm New Password", txtConfirmPass),
                lblMsg
        );

        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        Button btnSave = new Button("Update Password");
        btnSave.getStyleClass().add("nts-primary-button");

        Button btnCancel = new Button("Cancel");
        btnCancel.getStyleClass().add("btn-secondary");
        btnCancel.setOnAction(e -> modalStage.close());

        btnSave.setOnAction(e -> {
            String curr = txtCurrentPass.getText().trim();
            String nPass = txtNewPass.getText().trim();
            String cPass = txtConfirmPass.getText().trim();

            if (!activeAdminPassword.equals(curr)) {
                lblMsg.setText("Current password is incorrect.");
                lblMsg.setStyle("-fx-text-fill: #ED6B6B;");
                return;
            }
            if (nPass.isEmpty() || nPass.length() < 4) {
                lblMsg.setText("New password must be at least 4 characters long.");
                lblMsg.setStyle("-fx-text-fill: #ED6B6B;");
                return;
            }
            if (!nPass.equals(cPass)) {
                lblMsg.setText("New passwords do not match.");
                lblMsg.setStyle("-fx-text-fill: #ED6B6B;");
                return;
            }

            activeAdminPassword = nPass;
            DialogHelper.showSuccess("Security Update", "Password Changed", "Admin password successfully updated.");
            modalStage.close();
        });

        btnBox.getChildren().addAll(btnCancel, btnSave);
        modalLayout.getChildren().addAll(lblModalTitle, form, btnBox);

        Scene scene = new Scene(modalLayout, 400, 340);
        modalStage.setScene(scene);
        modalStage.showAndWait();
    }

    private VBox createModalField(String labelText, PasswordField field) {
        VBox box = new VBox(4);
        Label label = new Label(labelText);
        label.getStyleClass().add("nts-form-label");
        field.getStyleClass().add("nts-input-field");
        box.getChildren().addAll(label, field);
        return box;
    }

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

        allocationTable.getColumns().addAll(
                colId, colCentre, colCity, colBuilding, colSuperintendent, colInvigilatorCount, colDate
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
        allocationData.add(new AllocationRecord(201, "NTS Centre A", "Lahore", "University Hall", "Dr. Rashid Ali", 12, "2026-09-05"));
        allocationData.add(new AllocationRecord(202, "NTS Centre B", "Islamabad", "College Campus", "Prof. Shahid Mehmood", 8, "2026-09-05"));
        allocationData.add(new AllocationRecord(203, "NTS Centre C", "Karachi", "Auditorium Block", "Engr. Farhan Tariq", 15, "2026-09-12"));
        allocationData.add(new AllocationRecord(204, "NTS Centre D", "Peshawar", "Public Academy", "Dr. Usman Khan", 10, "2026-09-12"));
    }

    public static class AllocationRecord {
        private final int allocationId;
        private final String centreName;
        private final String city;
        private final String buildingType;
        private final String superintendentName;
        private final int invigilatorCount;
        private final String dutyDate;

        public AllocationRecord(int allocationId, String centreName, String city, String buildingType, String superintendentName, int invigilatorCount, String dutyDate) {
            this.allocationId = allocationId;
            this.centreName = centreName;
            this.city = city;
            this.buildingType = buildingType;
            this.superintendentName = superintendentName;
            this.invigilatorCount = invigilatorCount;
            this.dutyDate = dutyDate;
        }

        public int getAllocationId() { return allocationId; }
        public String getCentreName() { return centreName; }
        public String getCity() { return city; }
        public String getBuildingType() { return buildingType; }
        public String getSuperintendentName() { return superintendentName; }
        public int getInvigilatorCount() { return invigilatorCount; }
        public String getDutyDate() { return dutyDate; }
    }
}
