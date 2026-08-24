package oopProject;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * StaffManagementView mapped 1:1 to Person -> Employee -> Invigilator/Superintendent UML architecture.
 * Shared Form Fields: name, fname, id card, phoneNo, employeeID, employeeCity, allowance, experience, invig_allowance, spdt_allowance.
 * Invigilator Specific Fields: designation, superintendentName, supdtPhone.
 * Superintendent Specific Fields: interval.
 * Action Buttons: Set Allowance, Get Allowance, Update, Delete.
 */
public class StaffManagementView extends VBox {

    // Shared Form Fields
    private final TextField txtName = new TextField();
    private final TextField txtFname = new TextField();
    private final TextField txtIdCard = new TextField();
    private final TextField txtPhoneNo = new TextField();
    private final TextField txtEmployeeID = new TextField();
    private final TextField txtEmployeeCity = new TextField();
    private final TextField txtAllowance = new TextField();
    private final TextField txtExperience = new TextField();
    private final TextField txtInvigAllowance = new TextField();
    private final TextField txtSpdtAllowance = new TextField();

    // Staff Type Selector
    private final RadioButton rbInvigilator = new RadioButton("Invigilator");
    private final RadioButton rbSuperintendent = new RadioButton("Superintendent");
    private final ToggleGroup staffGroup = new ToggleGroup();

    // Invigilator Specific Fields
    private final TextField txtDesignation = new TextField();
    private final TextField txtSuperintendentName = new TextField();
    private final TextField txtSupdtPhone = new TextField();
    private final VBox invigSpecificBox = new VBox(8);

    // Superintendent Specific Fields
    private final TextField txtInterval = new TextField();
    private final VBox spdtSpecificBox = new VBox(8);

    // TableView & Data Model
    private final TableView<Employee> staffTable = new TableView<>();
    private final ObservableList<Employee> staffData = FXCollections.observableArrayList();

    public StaffManagementView() {
        setSpacing(20);
        setPadding(new Insets(0));

        HBox mainContent = new HBox(20);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        VBox formCard = createStaffFormCard();
        formCard.setPrefWidth(420);
        formCard.setMinWidth(380);

        VBox tableCard = createStaffTableCard();
        HBox.setHgrow(tableCard, Priority.ALWAYS);

        mainContent.getChildren().addAll(formCard, tableCard);
        getChildren().add(mainContent);

        refreshStaffData();

        staffTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateForm(newVal);
            }
        });

        if (!staffData.isEmpty()) {
            staffTable.getSelectionModel().selectFirst();
        }
    }

    private VBox createStaffFormCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(20));

        Label titleLabel = new Label("Staff Management Form (Employee Hierarchy)");
        titleLabel.getStyleClass().add("card-title");

        // Staff Role Toggle Box
        HBox typeBox = new HBox(16);
        typeBox.setAlignment(Pos.CENTER_LEFT);
        rbInvigilator.setToggleGroup(staffGroup);
        rbSuperintendent.setToggleGroup(staffGroup);
        rbInvigilator.setSelected(true);

        rbInvigilator.setOnAction(e -> toggleSpecificFields(true));
        rbSuperintendent.setOnAction(e -> toggleSpecificFields(false));

        typeBox.getChildren().addAll(new Label("Staff Role:"), rbInvigilator, rbSuperintendent);

        // Shared Form Fields Grid
        VBox formGrid = new VBox(8);
        formGrid.getChildren().addAll(
                createFormField("Employee ID (employeeID)", txtEmployeeID, "Numeric ID..."),
                createFormField("Full Name (name)", txtName, "Employee full name..."),
                createFormField("Father Name (fname)", txtFname, "Father name..."),
                createFormField("ID Card (id card)", txtIdCard, "e.g. 35202-1234567-1"),
                createFormField("Phone No (phoneNo)", txtPhoneNo, "Phone number..."),
                createFormField("City (employeeCity)", txtEmployeeCity, "Employee city..."),
                createFormField("Base Allowance (allowance)", txtAllowance, "Float allowance..."),
                createFormField("Experience (experience)", txtExperience, "Years of experience..."),
                createFormField("Invigilator Allowance (invig_allowance)", txtInvigAllowance, "Double allowance..."),
                createFormField("Superintendent Allowance (spdt_allowance)", txtSpdtAllowance, "Double allowance...")
        );

        // Invigilator Specific Fields
        invigSpecificBox.getChildren().addAll(
                createFormField("Designation (designation)", txtDesignation, "e.g. Senior Invigilator"),
                createFormField("Superintendent Name (superintendentName)", txtSuperintendentName, "Assigned Superintendent"),
                createFormField("Superintendent Phone (supdtPhone)", txtSupdtPhone, "Numeric phone...")
        );

        // Superintendent Specific Fields
        spdtSpecificBox.getChildren().addAll(
                createFormField("Interval in Months (interval)", txtInterval, "Numeric interval...")
        );
        spdtSpecificBox.setManaged(false);
        spdtSpecificBox.setVisible(false);

        VBox specificContainer = new VBox(10, invigSpecificBox, spdtSpecificBox);
        specificContainer.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 10; -fx-border-color: #CBD5E1; -fx-border-width: 1px;");

        // Action Buttons mapped 1:1 to UML methods
        VBox buttonBox = new VBox(10);

        HBox row1 = new HBox(10);
        Button btnSetAllowance = new Button("Set Allowance");
        btnSetAllowance.getStyleClass().add("btn-primary");
        btnSetAllowance.setOnAction(e -> handleSetAllowance());

        Button btnGetAllowance = new Button("Get Allowance");
        btnGetAllowance.getStyleClass().add("btn-accent");
        btnGetAllowance.setOnAction(e -> handleGetAllowance());

        row1.getChildren().addAll(btnSetAllowance, btnGetAllowance);

        HBox row2 = new HBox(10);
        Button btnUpdate = new Button("Update");
        btnUpdate.getStyleClass().add("btn-secondary");
        btnUpdate.setOnAction(e -> handleUpdate());

        Button btnDelete = new Button("Delete");
        btnDelete.getStyleClass().add("btn-secondary");
        btnDelete.setStyle("-fx-text-fill: #DC2626; -fx-border-color: #FCA5A5;");
        btnDelete.setOnAction(e -> handleDelete());

        row2.getChildren().addAll(btnUpdate, btnDelete);

        buttonBox.getChildren().addAll(row1, row2);
        card.getChildren().addAll(titleLabel, typeBox, formGrid, specificContainer, buttonBox);

        ScrollPane scroll = new ScrollPane(card);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox wrapper = new VBox(scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        return wrapper;
    }

    private VBox createStaffTableCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(20));

        Label cardHeader = new Label("Staff & Allowance Directory");
        cardHeader.getStyleClass().add("card-title");

        setupStaffTable();
        staffTable.setItems(staffData);
        VBox.setVgrow(staffTable, Priority.ALWAYS);

        card.getChildren().addAll(cardHeader, staffTable);
        return card;
    }

    private VBox createFormField(String labelText, TextField textField, String promptText) {
        VBox box = new VBox(4);
        Label label = new Label(labelText);
        label.getStyleClass().add("nts-form-label");
        textField.getStyleClass().add("nts-input-field");
        textField.setPromptText(promptText);
        textField.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().addAll(label, textField);
        return box;
    }

    private void toggleSpecificFields(boolean isInvigilator) {
        invigSpecificBox.setVisible(isInvigilator);
        invigSpecificBox.setManaged(isInvigilator);
        spdtSpecificBox.setVisible(!isInvigilator);
        spdtSpecificBox.setManaged(!isInvigilator);
    }

    @SuppressWarnings("unchecked")
    private void setupStaffTable() {
        TableColumn<Employee, Integer> colID = new TableColumn<>("Emp ID");
        colID.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEmployeeID()).asObject());

        TableColumn<Employee, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Employee, String> colCity = new TableColumn<>("City");
        colCity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmployeeCity()));

        TableColumn<Employee, String> colRole = new TableColumn<>("Role");
        colRole.setCellValueFactory(data -> new SimpleStringProperty(data.getValue() instanceof Invigilator ? "Invigilator" : "Superintendent"));

        TableColumn<Employee, Float> colAllowance = new TableColumn<>("Base Allowance");
        colAllowance.setCellValueFactory(data -> new SimpleFloatProperty(data.getValue().getAllowanceValue()).asObject());

        TableColumn<Employee, String> colInvigAllow = new TableColumn<>("Invig. Stipend");
        colInvigAllow.setCellValueFactory(data -> new SimpleStringProperty("Rs. " + data.getValue().getInvig_allowance()));

        TableColumn<Employee, String> colSpdtAllow = new TableColumn<>("Spdt. Stipend");
        colSpdtAllow.setCellValueFactory(data -> new SimpleStringProperty("Rs. " + data.getValue().getSpdt_allowance()));

        staffTable.getColumns().addAll(colID, colName, colCity, colRole, colAllowance, colInvigAllow, colSpdtAllow);
        staffTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    // Action Handlers mapping UML methods 1:1
    private void handleSetAllowance() {
        Employee selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("No Selection", "Set Allowance Error", "Please select an employee from the table.");
            return;
        }

        try {
            double invigVal = Double.parseDouble(txtInvigAllowance.getText().trim());
            double spdtVal = Double.parseDouble(txtSpdtAllowance.getText().trim());

            selected.setInvig_allowance(invigVal);
            selected.setSpdt_allowance(spdtVal);
            selected.setAllowance();

            staffTable.refresh();
            DialogHelper.showInformation("AllowanceManagement Interface", "setAllowance() Executed", "Allowances set successfully for " + selected.getName() + ":\nInvigilator: Rs. " + invigVal + "\nSuperintendent: Rs. " + spdtVal);
        } catch (NumberFormatException e) {
            DialogHelper.showError("Input Error", "Invalid Numeric Format", "Please enter valid numeric values for allowances.");
        }
    }

    private void handleGetAllowance() {
        Employee selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("No Selection", "Get Allowance Error", "Please select an employee from the table.");
            return;
        }

        selected.getAllowance();
        DialogHelper.showInformation("AllowanceManagement Interface", "getAllowance() Executed", "Employee: " + selected.getName() + "\nInvigilator Allowance: Rs. " + selected.getInvig_allowance() + "\nSuperintendent Allowance: Rs. " + selected.getSpdt_allowance());
    }

    private void handleUpdate() {
        Employee selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            // If not selected, save a new Employee
            try {
                int empID = Integer.parseInt(txtEmployeeID.getText().trim());
                String name = txtName.getText().trim();
                String fname = txtFname.getText().trim();
                String cnic = txtIdCard.getText().trim();
                String phone = txtPhoneNo.getText().trim();
                String city = txtEmployeeCity.getText().trim();
                float allow = Float.parseFloat(txtAllowance.getText().trim());
                int exp = Integer.parseInt(txtExperience.getText().trim());
                double invig = Double.parseDouble(txtInvigAllowance.getText().trim());
                double spdt = Double.parseDouble(txtSpdtAllowance.getText().trim());

                Employee emp;
                if (rbInvigilator.isSelected()) {
                    String des = txtDesignation.getText().trim();
                    String supName = txtSuperintendentName.getText().trim();
                    Long supPhone = Long.parseLong(txtSupdtPhone.getText().trim());
                    emp = new Invigilator(name, fname, cnic, phone, empID, city, allow, exp, invig, spdt, des, supName, supPhone);
                } else {
                    int interval = Integer.parseInt(txtInterval.getText().trim());
                    emp = new Superintendent(name, fname, cnic, phone, empID, city, allow, exp, invig, spdt, interval);
                }

                staffData.add(emp);
                staffTable.getSelectionModel().select(emp);
                DialogHelper.showInformation("Staff Management", "update() Executed", "New staff member saved successfully.");
            } catch (Exception e) {
                DialogHelper.showError("Input Error", "Invalid Form Data", e.getMessage());
            }
            return;
        }

        try {
            selected.setName(txtName.getText().trim());
            selected.setFname(txtFname.getText().trim());
            selected.setIdCard(txtIdCard.getText().trim());
            selected.setPhoneNo(txtPhoneNo.getText().trim());
            selected.setEmployeeID(Integer.parseInt(txtEmployeeID.getText().trim()));
            selected.setEmployeeCity(txtEmployeeCity.getText().trim());
            selected.setAllowanceValue(Float.parseFloat(txtAllowance.getText().trim()));
            selected.setExperience(Integer.parseInt(txtExperience.getText().trim()));
            selected.setInvig_allowance(Double.parseDouble(txtInvigAllowance.getText().trim()));
            selected.setSpdt_allowance(Double.parseDouble(txtSpdtAllowance.getText().trim()));

            if (selected instanceof Invigilator inv) {
                inv.setDesignation(txtDesignation.getText().trim());
                inv.setSuperintendentName(txtSuperintendentName.getText().trim());
                inv.setSupdtPhone(Long.parseLong(txtSupdtPhone.getText().trim()));
            } else if (selected instanceof Superintendent spd) {
                spd.setInterval(Integer.parseInt(txtInterval.getText().trim()));
            }

            selected.update();
            staffTable.refresh();
            DialogHelper.showInformation("Staff Management", "update() Executed", "Staff member updated successfully.");
        } catch (Exception e) {
            DialogHelper.showError("Input Error", "Invalid Form Data", e.getMessage());
        }
    }

    private void handleDelete() {
        Employee selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("No Selection", "Delete Error", "Please select a staff member to delete.");
            return;
        }

        DialogHelper.showConfirmation("Confirm Deletion", "Delete Staff Record", "Remove staff member " + selected.getName() + " (ID: " + selected.getEmployeeID() + ")?", () -> {
            if (selected instanceof Invigilator inv) {
                inv.delete();
            } else if (selected instanceof Superintendent spd) {
                spd.delete();
            }
            staffData.remove(selected);
            clearFormInputs();
            DialogHelper.showInformation("Staff Management", "delete() Executed", "Staff record deleted successfully.");
        });
    }

    private void populateForm(Employee emp) {
        txtEmployeeID.setText(String.valueOf(emp.getEmployeeID()));
        txtName.setText(emp.getName() != null ? emp.getName() : "");
        txtFname.setText(emp.getFname() != null ? emp.getFname() : "");
        txtIdCard.setText(emp.getIdCard() != null ? emp.getIdCard() : "");
        txtPhoneNo.setText(emp.getPhoneNo() != null ? emp.getPhoneNo() : "");
        txtEmployeeCity.setText(emp.getEmployeeCity() != null ? emp.getEmployeeCity() : "");
        txtAllowance.setText(String.valueOf(emp.getAllowanceValue()));
        txtExperience.setText(String.valueOf(emp.getExperience()));
        txtInvigAllowance.setText(String.valueOf(emp.getInvig_allowance()));
        txtSpdtAllowance.setText(String.valueOf(emp.getSpdt_allowance()));

        if (emp instanceof Invigilator inv) {
            rbInvigilator.setSelected(true);
            toggleSpecificFields(true);
            txtDesignation.setText(inv.getDesignation() != null ? inv.getDesignation() : "");
            txtSuperintendentName.setText(inv.getSuperintendentName() != null ? inv.getSuperintendentName() : "");
            txtSupdtPhone.setText(String.valueOf(inv.getSupdtPhone() != null ? inv.getSupdtPhone() : 0L));
        } else if (emp instanceof Superintendent spd) {
            rbSuperintendent.setSelected(true);
            toggleSpecificFields(false);
            txtInterval.setText(String.valueOf(spd.getInterval()));
        }
    }

    private void clearFormInputs() {
        txtEmployeeID.clear();
        txtName.clear();
        txtFname.clear();
        txtIdCard.clear();
        txtPhoneNo.clear();
        txtEmployeeCity.clear();
        txtAllowance.clear();
        txtExperience.clear();
        txtInvigAllowance.clear();
        txtSpdtAllowance.clear();
        txtDesignation.clear();
        txtSuperintendentName.clear();
        txtSupdtPhone.clear();
        txtInterval.clear();
    }

    private void refreshStaffData() {
        if (staffData.isEmpty()) {
            Invigilator inv1 = new Invigilator("Saima Rashid", "Rashid Ahmed", "35202-5551122-1", "03215551122", 601, "Lahore", 500.0f, 4, 8500.0, 0.0, "Senior Invigilator", "Dr. Hamza Malik", 3001234567L);
            Superintendent spd1 = new Superintendent("Dr. Hamza Malik", "Muhammad Malik", "35202-1234567-1", "03001234567", 501, "Lahore", 1200.0f, 10, 0.0, 15000.0, 6);

            staffData.add(inv1);
            staffData.add(spd1);
        }
    }
}
