package oopProject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * TestCentreView implements Phase 3 of the NTS Management System JavaFX upgrade.
 * Features a high-density Master-Detail layout for managing Test Centres and staff duty allocations.
 */
public class TestCentreView extends SplitPane {

    // Master Controls
    private final TableView<TestCentre> centreMasterTable = new TableView<>();
    private final ObservableList<TestCentre> testCentreData = FXCollections.observableArrayList();

    // Detail Summary Controls
    private final Label lblDetailCentreNo = new Label("Select a venue");
    private final Label lblDetailCity = new Label("-");
    private final Label lblDetailBuilding = new Label("-");
    private final Label lblDetailDate = new Label("-");

    // Detail Supervisor Controls
    private final Label lblSpdName = new Label("Not Assigned");
    private final Label lblSpdCnic = new Label("-");
    private final Label lblSpdPhone = new Label("-");
    private final Label lblSpdAllowance = new Label("Rs. 0");

    // Staff Table
    private final TableView<Invigilator> invigilatorTable = new TableView<>();
    private final ObservableList<Invigilator> invigilatorData = FXCollections.observableArrayList();

    // Assignment Form Controls
    private final TextField txtStaffName = new TextField();
    private final TextField txtStaffCnic = new TextField();
    private final TextField txtStaffPhone = new TextField();
    private final TextField txtStaffAllowance = new TextField();
    private final ComboBox<String> cbRoleType = new ComboBox<>();

    public TestCentreView() {
        setOrientation(javafx.geometry.Orientation.HORIZONTAL);
        setDividerPositions(0.42);

        // 1. Left Master Pane (Test Centres List)
        VBox masterPane = createMasterPane();

        // 2. Right Detail Pane (Duty Team Allocation)
        VBox detailPane = createDetailPane();

        getItems().addAll(masterPane, detailPane);

        // Load Seed Data & Selection Listener
        refreshCentreData();

        centreMasterTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateDetailPane(newVal);
            }
        });

        if (!testCentreData.isEmpty()) {
            centreMasterTable.getSelectionModel().selectFirst();
        }
    }

    /* ==========================================================================
       1. MASTER PANE: TEST CENTRES LIST
       ========================================================================== */
    @SuppressWarnings("unchecked")
    private VBox createMasterPane() {
        VBox card = new VBox(12);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(16));

        // Header & Search
        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Test Centres");
        title.getStyleClass().add("card-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnNewCentre = new Button("+ New Centre");
        btnNewCentre.getStyleClass().add("btn-accent");
        btnNewCentre.setOnAction(this::handleAddNewCentre);

        header.getChildren().addAll(title, spacer, btnNewCentre);

        // Setup Master Table
        TableColumn<TestCentre, Integer> colNo = new TableColumn<>("Centre #");
        colNo.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getTestCentreNo()).asObject());
        colNo.setPrefWidth(70);

        TableColumn<TestCentre, String> colCity = new TableColumn<>("City");
        colCity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTestCentreCity()));

        TableColumn<TestCentre, String> colBuilding = new TableColumn<>("Building");
        colBuilding.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBuildingType()));

        TableColumn<TestCentre, String> colDate = new TableColumn<>("Allocation Date");
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAllocationDate()));

        centreMasterTable.getColumns().addAll(colNo, colCity, colBuilding, colDate);
        centreMasterTable.setItems(testCentreData);
        centreMasterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(centreMasterTable, Priority.ALWAYS);

        card.getChildren().addAll(header, centreMasterTable);
        return card;
    }

    /* ==========================================================================
       2. DETAIL PANE: DUTY TEAM ALLOCATION & SUPERVISOR DETAILS
       ========================================================================== */
    private VBox createDetailPane() {
        VBox card = new VBox(16);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(16));

        // Header
        Label title = new Label("Venue Operations & Staff Deployment");
        title.getStyleClass().add("card-title");

        // Top Summary Cards Grid
        GridPane summaryGrid = new GridPane();
        summaryGrid.setHgap(12);
        summaryGrid.setVgap(12);

        // Venue Info Card
        VBox venueCard = new VBox(6);
        venueCard.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-border-width: 1px; -fx-padding: 12;");

        lblDetailCentreNo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");
        Label lblCityTag = new Label("Location:");
        lblCityTag.setStyle("-fx-font-weight: bold; -fx-text-fill: #64748B; -fx-font-size: 11px;");

        HBox cityBox = new HBox(6, lblCityTag, lblDetailCity);
        HBox buildingBox = new HBox(6, new Label("Building:"), lblDetailBuilding);
        HBox dateBox = new HBox(6, new Label("Date:"), lblDetailDate);

        venueCard.getChildren().addAll(lblDetailCentreNo, cityBox, buildingBox, dateBox);

        // Supervisor Details Card
        VBox spdCard = new VBox(6);
        spdCard.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-border-width: 1px; -fx-padding: 12;");

        Label spdHeader = new Label("Superintendent (Duty Supervisor)");
        spdHeader.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #1E293B;");

        lblSpdName.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2563EB;");

        HBox spdCnicBox = new HBox(6, new Label("CNIC:"), lblSpdCnic);
        HBox spdPhoneBox = new HBox(6, new Label("Phone:"), lblSpdPhone);
        HBox spdAllowBox = new HBox(6, new Label("Stipend:"), lblSpdAllowance);

        spdCard.getChildren().addAll(spdHeader, lblSpdName, spdCnicBox, spdPhoneBox, spdAllowBox);

        summaryGrid.add(venueCard, 0, 0);
        summaryGrid.add(spdCard, 1, 0);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        summaryGrid.getColumnConstraints().addAll(c1, c2);

        // Invigilators High-Density Table
        VBox invigSection = new VBox(8);
        HBox invigHeader = new HBox(8);
        invigHeader.setAlignment(Pos.CENTER_LEFT);

        Label invigTitle = new Label("Assigned Invigilators Duty Roster");
        invigTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #1E293B;");

        Region invigSpacer = new Region();
        HBox.setHgrow(invigSpacer, Priority.ALWAYS);

        Button btnAddInvig = new Button("+ Assign Invigilator");
        btnAddInvig.getStyleClass().add("btn-primary");
        btnAddInvig.setOnAction(this::addInvigilators);

        Button btnAddSpd = new Button("Assign Superintendent");
        btnAddSpd.getStyleClass().add("btn-secondary");
        btnAddSpd.setOnAction(this::addSupervisor);

        invigHeader.getChildren().addAll(invigTitle, invigSpacer, btnAddSpd, btnAddInvig);

        // Setup Invigilator Table Columns
        setupInvigilatorTable();

        invigSection.getChildren().addAll(invigHeader, invigilatorTable);
        VBox.setVgrow(invigSection, Priority.ALWAYS);

        // Bottom Quick Staff Assignment Form
        VBox assignForm = createStaffAssignmentForm();

        card.getChildren().addAll(title, summaryGrid, invigSection, assignForm);
        return card;
    }

    private VBox createStaffAssignmentForm() {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-width: 1px; -fx-padding: 12;");

        Label formTitle = new Label("Staff Duty Assignment Controls");
        formTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #0F172A;");

        HBox inputs = new HBox(10);
        inputs.setAlignment(Pos.CENTER_LEFT);

        cbRoleType.getItems().addAll("Invigilator", "Superintendent");
        cbRoleType.setValue("Invigilator");
        cbRoleType.setPrefWidth(130);

        txtStaffName.setPromptText("Staff Name");
        txtStaffCnic.setPromptText("CNIC (e.g. 35202-...)");
        txtStaffPhone.setPromptText("Phone Number");
        txtStaffAllowance.setPromptText("Allowance (PKR)");

        HBox.setHgrow(txtStaffName, Priority.ALWAYS);
        HBox.setHgrow(txtStaffCnic, Priority.ALWAYS);

        Button btnAssignSubmit = new Button("Confirm Duty");
        btnAssignSubmit.getStyleClass().add("btn-accent");
        btnAssignSubmit.setOnAction(e -> {
            if ("Superintendent".equals(cbRoleType.getValue())) {
                addSupervisor(e);
            } else {
                addInvigilators(e);
            }
        });

        inputs.getChildren().addAll(cbRoleType, txtStaffName, txtStaffCnic, txtStaffPhone, txtStaffAllowance, btnAssignSubmit);
        box.getChildren().addAll(formTitle, inputs);
        return box;
    }

    @SuppressWarnings("unchecked")
    private void setupInvigilatorTable() {
        TableColumn<Invigilator, Integer> colEmpNo = new TableColumn<>("Emp ID");
        colEmpNo.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEmployeeNo()).asObject());
        colEmpNo.setPrefWidth(70);

        TableColumn<Invigilator, String> colName = new TableColumn<>("Invigilator Name");
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Invigilator, String> colDesig = new TableColumn<>("Designation");
        colDesig.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDesignation()));

        TableColumn<Invigilator, String> colPhone = new TableColumn<>("Phone");
        colPhone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNo()));

        TableColumn<Invigilator, String> colAllowance = new TableColumn<>("Invig. Allowance");
        colAllowance.setCellValueFactory(data -> new SimpleStringProperty("Rs. " + data.getValue().getInvig_allowance()));

        invigilatorTable.getColumns().addAll(colEmpNo, colName, colDesig, colPhone, colAllowance);
        invigilatorTable.setItems(invigilatorData);
        invigilatorTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        invigilatorTable.setPrefHeight(160);
    }

    /* ==========================================================================
       3. MASTER-DETAIL DATA SYNC & ACTION STUBS
       ========================================================================== */
    private void updateDetailPane(TestCentre centre) {
        if (centre == null) return;

        lblDetailCentreNo.setText("Test Centre #" + centre.getTestCentreNo());
        lblDetailCity.setText(centre.getTestCentreCity() != null ? centre.getTestCentreCity() : "N/A");
        lblDetailBuilding.setText(centre.getBuildingType() != null ? centre.getBuildingType() : "N/A");
        lblDetailDate.setText(centre.getAllocationDate() != null ? centre.getAllocationDate() : "Unassigned");

        Superintendent spd = centre.getSpd();
        if (spd != null && spd.getName() != null && !spd.getName().isEmpty()) {
            lblSpdName.setText(spd.getName());
            lblSpdCnic.setText(spd.getIdCard() != null ? spd.getIdCard() : "-");
            lblSpdPhone.setText(spd.getPhoneNo() != null ? spd.getPhoneNo() : "-");
            lblSpdAllowance.setText("Rs. " + spd.getSpdt_allowance());
        } else {
            lblSpdName.setText("Not Assigned");
            lblSpdCnic.setText("-");
            lblSpdPhone.setText("-");
            lblSpdAllowance.setText("Rs. 0");
        }

        invigilatorData.setAll(centre.getInvigilators());
    }

    /**
     * Action stub based on TestCentreTeamManagement interface for assigning a Superintendent.
     */
    public void addSupervisor(ActionEvent event) {
        TestCentre selected = centreMasterTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a Test Centre from the master list first.");
            return;
        }

        String name = txtStaffName.getText().trim();
        String cnic = txtStaffCnic.getText().trim();
        String phone = txtStaffPhone.getText().trim();
        double allowance = parseDouble(txtStaffAllowance.getText().trim(), 12000.0);

        if (name.isEmpty()) {
            name = "Dr. Hamza Malik";
            cnic = "35202-9988776-1";
            phone = "0300-9988776";
        }

        Superintendent spd = new Superintendent(name, "Father Name", cnic, phone, 501, selected.getTestCentreCity(), 0.0, allowance, 6);
        selected.setSpd(spd);

        updateDetailPane(selected);
        clearStaffInputs();
        DialogHelper.showInformation("Supervisor Assigned", "Duty Allocation Updated", "Superintendent " + name + " assigned to Centre #" + selected.getTestCentreNo());
    }

    /**
     * Action stub based on TestCentreTeamManagement interface for adding Invigilators.
     */
    public void addInvigilators(ActionEvent event) {
        TestCentre selected = centreMasterTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("No Selection", "Venue Not Selected", "Please select a Test Centre from the master list first.");
            return;
        }

        String name = txtStaffName.getText().trim();
        String cnic = txtStaffCnic.getText().trim();
        String phone = txtStaffPhone.getText().trim();
        double allowance = parseDouble(txtStaffAllowance.getText().trim(), 6500.0);

        if (name.isEmpty()) {
            name = "Staff Invigilator " + (selected.getInvigilators().size() + 1);
            cnic = "61101-1122334-" + (selected.getInvigilators().size() + 1);
            phone = "0312-5556677";
        }

        Invigilator inv = new Invigilator(name, "Father Name", cnic, phone, 600 + selected.getInvigilators().size(), selected.getTestCentreCity(), "Exam Invigilator", "Superintendent", allowance, 0.0, 0);
        selected.getInvigilators().add(inv);

        updateDetailPane(selected);
        clearStaffInputs();
        DialogHelper.showInformation("Invigilator Added", "Duty Allocation Updated", "Invigilator " + name + " assigned to Centre #" + selected.getTestCentreNo());
    }

    public void handleAddNewCentre(ActionEvent event) {
        int newNo = 100 + testCentreData.size() + 1;
        TestCentre tc = new TestCentre(newNo, "Government Hall", "2026-10-01", "Dr. Supervisor", "Father Name", "35202-0000000-0", "0300-0000000", 700, "Islamabad", "Chief Supervisor", "Board", 0.0, 15000.0, 12, new Invigilator[0]);
        tc.setTestCentreCity("Islamabad");
        tc.setBuildingType("Public High School");
        tc.setAllocationDate("2026-10-15");

        testCentreData.add(tc);
        centreMasterTable.getSelectionModel().select(tc);
    }

    private void clearStaffInputs() {
        txtStaffName.clear();
        txtStaffCnic.clear();
        txtStaffPhone.clear();
        txtStaffAllowance.clear();
    }

    private void refreshCentreData() {
        if (testCentreData.isEmpty()) {
            TestCentre tc1 = new TestCentre(101, "University Campus Hall A", "2026-09-15", "Dr. Hamza Malik", "Muhammad Malik", "35202-1234567-1", "0300-1234567", 501, "Lahore", "Superintendent", "NTS Board", 0.0, 15000.0, 6, new Invigilator[0]);
            tc1.setTestCentreCity("Lahore");
            tc1.getInvigilators().add(new Invigilator("Saima Rashid", "Rashid Ahmed", "35202-5551122-1", "0321-5551122", 601, "Lahore", "Senior Invigilator", "Dr. Hamza Malik", 8500.0, 0.0, 0));
            tc1.getInvigilators().add(new Invigilator("Bilal Chaudhry", "Chaudhry Akram", "35202-9993344-2", "0333-9993344", 602, "Lahore", "Hall Invigilator", "Dr. Hamza Malik", 7000.0, 0.0, 0));

            TestCentre tc2 = new TestCentre(102, "Federal College Complex", "2026-09-15", "Prof. Tariq Mahmood", "Mahmood Khan", "61101-7778899-3", "0312-7778899", 502, "Islamabad", "Superintendent", "NTS Board", 0.0, 15000.0, 6, new Invigilator[0]);
            tc2.setTestCentreCity("Islamabad");
            tc2.getInvigilators().add(new Invigilator("Usman Ali", "Ali Asghar", "61101-4445566-4", "0300-4445566", 603, "Islamabad", "Invigilator", "Prof. Tariq Mahmood", 7500.0, 0.0, 0));

            testCentreData.addAll(tc1, tc2);
        }
    }

    private double parseDouble(String str, double defaultVal) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
