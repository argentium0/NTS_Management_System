package oopProject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * StaffView implements Phase 4 of the NTS Management System JavaFX upgrade.
 * Manages Employee staff records and configures allowances via the Allowance_Management interface.
 * Utilizes DialogHelper for custom modal alerts and feedback.
 */
public class StaffView extends VBox {

    // Form Inputs
    private final TextField txtEmpNo = new TextField();
    private final TextField txtName = new TextField();
    private final TextField txtFatherName = new TextField();
    private final TextField txtCnic = new TextField();
    private final TextField txtPhone = new TextField();
    private final TextField txtCity = new TextField();
    private final TextField txtInvigAllowance = new TextField();
    private final TextField txtSpdtAllowance = new TextField();

    // Summary Metric Labels
    private final Label lblTotalInvigBudget = new Label("Rs. 0");
    private final Label lblTotalSpdtBudget = new Label("Rs. 0");

    // TableView & Data Model
    private final TableView<Employee> employeeTable = new TableView<>();
    private final ObservableList<Employee> employeeData = FXCollections.observableArrayList();

    public StaffView() {
        setSpacing(20);
        setPadding(new Insets(0));

        // Master Layout: Left (Form Pane) | Right (Staff & Allowance Directory)
        HBox mainContent = new HBox(20);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        VBox formCard = createStaffFormCard();
        formCard.setPrefWidth(380);
        formCard.setMinWidth(340);

        VBox tableCard = createStaffTableCard();
        HBox.setHgrow(tableCard, Priority.ALWAYS);

        mainContent.getChildren().addAll(formCard, tableCard);
        getChildren().add(mainContent);

        // Load Seed Data
        refreshEmployeeData();
    }

    /* ==========================================================================
       1. FORM SECTION: STAFF ENTRY & ALLOWANCE CONFIGURATION
       ========================================================================== */
    private VBox createStaffFormCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(20));

        Label titleLabel = new Label("Staff Record & Allowance Management");
        titleLabel.getStyleClass().add("card-title");

        VBox formGrid = new VBox(8);

        formGrid.getChildren().addAll(
                createFormField("Employee ID", txtEmpNo),
                createFormField("Full Name", txtName),
                createFormField("Father Name", txtFatherName),
                createFormField("CNIC Number", txtCnic),
                createFormField("Phone Number", txtPhone),
                createFormField("City", txtCity)
        );

        // Financial Allowance Inputs Box
        VBox allowanceBox = new VBox(8);
        allowanceBox.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-border-width: 1px; -fx-padding: 12;");

        Label lblAllowanceHeader = new Label("Financial Stipend / Allowance Configuration");
        lblAllowanceHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 11px; -fx-text-fill: #1E293B;");

        HBox allowFields = new HBox(10);
        VBox f1 = createFormField("Invigilator Allowance (PKR)", txtInvigAllowance);
        VBox f2 = createFormField("Superintendent Allowance (PKR)", txtSpdtAllowance);
        HBox.setHgrow(f1, Priority.ALWAYS);
        HBox.setHgrow(f2, Priority.ALWAYS);
        allowFields.getChildren().addAll(f1, f2);

        Button btnUpdateAllowance = new Button("Set Allowances");
        btnUpdateAllowance.getStyleClass().add("btn-accent");
        btnUpdateAllowance.setMaxWidth(Double.MAX_VALUE);
        btnUpdateAllowance.setOnAction(this::handleUpdateAllowances);

        allowanceBox.getChildren().addAll(lblAllowanceHeader, allowFields, btnUpdateAllowance);
        formGrid.getChildren().add(allowanceBox);

        // Action Button Bars
        HBox buttonBar1 = new HBox(10);
        Button btnAdd = new Button("Save Employee");
        btnAdd.getStyleClass().add("btn-primary");
        btnAdd.setOnAction(this::handleAddEmployee);
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnAdd, Priority.ALWAYS);

        Button btnUpdate = new Button("Update Info");
        btnUpdate.getStyleClass().add("btn-secondary");
        btnUpdate.setOnAction(this::handleUpdateEmployee);

        buttonBar1.getChildren().addAll(btnAdd, btnUpdate);

        HBox buttonBar2 = new HBox(10);
        Button btnDelete = new Button("Delete Staff");
        btnDelete.getStyleClass().add("btn-secondary");
        btnDelete.setStyle("-fx-text-fill: #DC2626; -fx-border-color: #FCA5A5;");
        btnDelete.setOnAction(this::handleDeleteEmployee);

        Button btnClear = new Button("Clear Form");
        btnClear.getStyleClass().add("btn-secondary");
        btnClear.setOnAction(e -> handleClearForm());

        buttonBar2.getChildren().addAll(btnDelete, btnClear);

        card.getChildren().addAll(titleLabel, formGrid, buttonBar1, buttonBar2);
        return card;
    }

    private VBox createFormField(String labelText, TextField textField) {
        VBox box = new VBox(3);
        Label label = new Label(labelText);
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #334155; -fx-font-size: 11px;");
        textField.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().addAll(label, textField);
        return box;
    }

    /* ==========================================================================
       2. TABLEVIEW & FINANCIAL SUMMARY CARDS
       ========================================================================== */
    private VBox createStaffTableCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(20));

        // Summary Metric Bar
        GridPane metricsGrid = new GridPane();
        metricsGrid.setHgap(12);
        metricsGrid.setVgap(12);

        metricsGrid.add(createMetricCard("Invigilator Allowance Pool", lblTotalInvigBudget, "Active Staff Allocation"), 0, 0);
        metricsGrid.add(createMetricCard("Superintendent Stipend Pool", lblTotalSpdtBudget, "Venue Supervisory Allocation"), 1, 0);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        metricsGrid.getColumnConstraints().addAll(c1, c2);

        // Header Title
        Label cardHeader = new Label("Staff & Allowance Directory");
        cardHeader.getStyleClass().add("card-title");

        // Setup Table
        setupEmployeeTable();

        employeeTable.setItems(employeeData);
        employeeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(employeeTable, Priority.ALWAYS);

        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateForm(newVal);
            }
        });

        card.getChildren().addAll(metricsGrid, cardHeader, employeeTable);
        return card;
    }

    @SuppressWarnings("unchecked")
    private void setupEmployeeTable() {
        TableColumn<Employee, Integer> colEmpNo = new TableColumn<>("Emp ID");
        colEmpNo.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEmployeeNo()).asObject());
        colEmpNo.setPrefWidth(65);

        TableColumn<Employee, String> colName = new TableColumn<>("Employee Name");
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Employee, String> colFatherName = new TableColumn<>("Father Name");
        colFatherName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFname()));

        TableColumn<Employee, String> colCnic = new TableColumn<>("CNIC");
        colCnic.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdCard()));

        TableColumn<Employee, String> colCity = new TableColumn<>("City");
        colCity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCity()));

        TableColumn<Employee, String> colInvig = new TableColumn<>("Invig. Allowance");
        colInvig.setCellValueFactory(data -> new SimpleStringProperty("Rs. " + String.format("%,.2f", data.getValue().getInvig_allowance())));

        TableColumn<Employee, String> colSpdt = new TableColumn<>("Spdt. Allowance");
        colSpdt.setCellValueFactory(data -> new SimpleStringProperty("Rs. " + String.format("%,.2f", data.getValue().getSpdt_allowance())));

        employeeTable.getColumns().addAll(colEmpNo, colName, colFatherName, colCnic, colCity, colInvig, colSpdt);
    }

    private VBox createMetricCard(String title, Label valLabel, String subtitle) {
        VBox box = new VBox(4);
        box.setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #CBD5E1; -fx-border-width: 1px; -fx-padding: 12;");

        Label lblTitle = new Label(title);
        lblTitle.getStyleClass().add("card-title");

        valLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        Label lblSub = new Label(subtitle);
        lblSub.getStyleClass().add("metric-label");

        box.getChildren().addAll(lblTitle, valLabel, lblSub);
        return box;
    }

    /* ==========================================================================
       3. EVENT HANDLERS & ALLOWANCE_MANAGEMENT INTERFACE INTEGRATION
       ========================================================================== */

    /**
     * Updates allowances via Allowance_Management interface methods on the selected Employee.
     */
    public void handleUpdateAllowances(ActionEvent event) {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("No Selection", "Employee Not Selected", "Please select an employee record from the directory table first.");
            return;
        }

        try {
            double invigVal = Double.parseDouble(txtInvigAllowance.getText().trim());
            double spdtVal = Double.parseDouble(txtSpdtAllowance.getText().trim());

            selected.setInvig_allowance(invigVal);
            selected.setSpdt_allowance(spdtVal);

            refreshEmployeeData();
            DialogHelper.showInformation("Allowance Configured", "Stipends Updated",
                    "Financial allowances successfully set for " + selected.getName() + ":\n" +
                            "• Invigilator Stipend: Rs. " + String.format("%,.2f", invigVal) + "\n" +
                            "• Superintendent Stipend: Rs. " + String.format("%,.2f", spdtVal));
        } catch (NumberFormatException e) {
            DialogHelper.showError("Invalid Input", "Numeric Conversion Error", "Please enter valid numeric amounts for allowances.");
        }
    }

    public void handleAddEmployee(ActionEvent event) {
        try {
            int empNo = Integer.parseInt(txtEmpNo.getText().trim());
            String name = txtName.getText().trim();
            String fname = txtFatherName.getText().trim();
            String cnic = txtCnic.getText().trim();
            String phone = txtPhone.getText().trim();
            String city = txtCity.getText().trim();
            double invig = parseDouble(txtInvigAllowance.getText().trim(), 0.0);
            double spdt = parseDouble(txtSpdtAllowance.getText().trim(), 0.0);

            Employee emp = new Employee(name, fname, cnic, phone, empNo, city, invig, spdt);
            employeeData.add(emp);

            recalculateFinancialTotals();
            handleClearForm();
            DialogHelper.showInformation("Staff Registered", "Employee Created", "Employee " + name + " (ID: " + empNo + ") added successfully.");
        } catch (NumberFormatException e) {
            DialogHelper.showError("Input Format Error", "Invalid Employee Number", "Please enter a valid numeric Employee ID.");
        }
    }

    public void handleUpdateEmployee(ActionEvent event) {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("No Selection", "Update Error", "Please select an employee from the directory table to update.");
            return;
        }

        selected.setName(txtName.getText().trim());
        selected.setFname(txtFatherName.getText().trim());
        selected.setIdCard(txtCnic.getText().trim());
        selected.setPhoneNo(txtPhone.getText().trim());
        selected.setCity(txtCity.getText().trim());

        refreshEmployeeData();
        DialogHelper.showInformation("Update Complete", "Employee Record Updated", "Details updated successfully for " + selected.getName());
    }

    public void handleDeleteEmployee(ActionEvent event) {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("No Selection", "Delete Action Failed", "Please select an employee to delete.");
            return;
        }

        DialogHelper.showConfirmation("Confirm Deletion", "Remove Staff Member?",
                "Are you sure you want to remove " + selected.getName() + " (ID: " + selected.getEmployeeNo() + ") from the system?",
                () -> {
                    employeeData.remove(selected);
                    recalculateFinancialTotals();
                    handleClearForm();
                    DialogHelper.showInformation("Staff Removed", "Record Deleted", "Employee record removed successfully.");
                });
    }

    private void handleClearForm() {
        txtEmpNo.clear();
        txtName.clear();
        txtFatherName.clear();
        txtCnic.clear();
        txtPhone.clear();
        txtCity.clear();
        txtInvigAllowance.clear();
        txtSpdtAllowance.clear();
        employeeTable.getSelectionModel().clearSelection();
    }

    private void populateForm(Employee emp) {
        txtEmpNo.setText(String.valueOf(emp.getEmployeeNo()));
        txtName.setText(emp.getName() != null ? emp.getName() : "");
        txtFatherName.setText(emp.getFname() != null ? emp.getFname() : "");
        txtCnic.setText(emp.getIdCard() != null ? emp.getIdCard() : "");
        txtPhone.setText(emp.getPhoneNo() != null ? emp.getPhoneNo() : "");
        txtCity.setText(emp.getCity() != null ? emp.getCity() : "");
        txtInvigAllowance.setText(String.valueOf(emp.getInvig_allowance()));
        txtSpdtAllowance.setText(String.valueOf(emp.getSpdt_allowance()));
    }

    private void refreshEmployeeData() {
        if (employeeData.isEmpty()) {
            employeeData.add(new Employee("Dr. Hamza Malik", "Muhammad Malik", "35202-1234567-1", "0300-1234567", 501, "Lahore", 0.0, 15000.0));
            employeeData.add(new Employee("Saima Rashid", "Rashid Ahmed", "35202-5551122-1", "0321-5551122", 502, "Islamabad", 8500.0, 0.0));
            employeeData.add(new Employee("Bilal Chaudhry", "Chaudhry Akram", "35202-9993344-2", "0333-9993344", 503, "Rawalpindi", 7000.0, 0.0));
        }

        employeeTable.refresh();
        recalculateFinancialTotals();
    }

    private void recalculateFinancialTotals() {
        double totalInvig = 0;
        double totalSpdt = 0;

        for (Employee emp : employeeData) {
            totalInvig += emp.getInvig_allowance();
            totalSpdt += emp.getSpdt_allowance();
        }

        lblTotalInvigBudget.setText("Rs. " + String.format("%,.2f", totalInvig));
        lblTotalSpdtBudget.setText("Rs. " + String.format("%,.2f", totalSpdt));
    }

    private double parseDouble(String str, double defaultVal) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
