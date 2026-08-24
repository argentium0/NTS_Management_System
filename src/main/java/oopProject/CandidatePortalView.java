package oopProject;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;

/**
 * CandidatePortalView mapped 1:1 to Person -> Candidate UML architecture.
 * Form Fields: name, fname, id card (numeric), phoneNo (numeric), candidateEmail, candidatePass, formNo.
 * Associated Data: TableView displaying candidate's ArrayList<Test> relation.
 * Action Buttons: Add Basic Data, Update Candidate Info, Delete Candidate, Apply Test, Check Status.
 */
public class CandidatePortalView extends VBox {

    // Form Field Controls
    private final TextField txtName = new TextField();
    private final TextField txtFname = new TextField();
    private final TextField txtIdCard = new TextField();
    private final TextField txtPhoneNo = new TextField();
    private final TextField txtCandidateEmail = new TextField();
    private final PasswordField txtCandidatePass = new PasswordField();
    private final TextField txtFormNo = new TextField();

    // TableView & Candidate Master / Associated Tests Data
    private final TableView<Candidate> candidateMasterTable = new TableView<>();
    private final ObservableList<Candidate> candidateData = FXCollections.observableArrayList();

    private final TableView<Test> testTableView = new TableView<>();
    private final ObservableList<Test> testData = FXCollections.observableArrayList();

    public CandidatePortalView() {
        setSpacing(20);
        setPadding(new Insets(0));

        HBox mainContent = new HBox(20);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        VBox formCard = createCandidateFormCard();
        formCard.setPrefWidth(380);
        formCard.setMinWidth(340);

        VBox dataCard = createCandidateDataCard();
        HBox.setHgrow(dataCard, Priority.ALWAYS);

        mainContent.getChildren().addAll(formCard, dataCard);
        getChildren().add(mainContent);

        refreshCandidateData();

        candidateMasterTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateForm(newVal);
                updateAssociatedTestsTable(newVal);
            }
        });

        if (!candidateData.isEmpty()) {
            candidateMasterTable.getSelectionModel().selectFirst();
        }
    }

    private VBox createCandidateFormCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(20));

        Label titleLabel = new Label("Candidate Information Form");
        titleLabel.getStyleClass().add("card-title");

        VBox formGrid = new VBox(10);
        formGrid.getChildren().addAll(
                createFormField("Candidate Name (name)", txtName, "Enter full name..."),
                createFormField("Father Name (fname)", txtFname, "Enter father name..."),
                createFormField("ID Card - Numeric (id card)", txtIdCard, "e.g. 3520212345671"),
                createFormField("Phone No - Numeric (phoneNo)", txtPhoneNo, "e.g. 03001234567"),
                createFormField("Candidate Email (candidateEmail)", txtCandidateEmail, "name@domain.com"),
                createFormField("Candidate Password (candidatePass)", txtCandidatePass, "Password..."),
                createFormField("Form Number (formNo)", txtFormNo, "Numeric form number...")
        );

        // Action Buttons mapped 1:1 to UML methods
        VBox buttonBox = new VBox(10);

        HBox row1 = new HBox(10);
        Button btnAddBasicData = new Button("Add Basic Data");
        btnAddBasicData.getStyleClass().add("btn-primary");
        btnAddBasicData.setOnAction(e -> handleAddBasicData());
        btnAddBasicData.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnAddBasicData, Priority.ALWAYS);

        Button btnUpdateCandidateInfo = new Button("Update Candidate Info");
        btnUpdateCandidateInfo.getStyleClass().add("btn-secondary");
        btnUpdateCandidateInfo.setOnAction(e -> handleUpdateCandidateInfo());

        row1.getChildren().addAll(btnAddBasicData, btnUpdateCandidateInfo);

        HBox row2 = new HBox(10);
        Button btnDeleteCandidate = new Button("Delete Candidate");
        btnDeleteCandidate.getStyleClass().add("btn-secondary");
        btnDeleteCandidate.setStyle("-fx-text-fill: #DC2626; -fx-border-color: #FCA5A5;");
        btnDeleteCandidate.setOnAction(e -> handleDeleteCandidate());

        Button btnApplyTest = new Button("Apply Test");
        btnApplyTest.getStyleClass().add("btn-accent");
        btnApplyTest.setOnAction(e -> handleApplyTest());

        Button btnCheckStatus = new Button("Check Status");
        btnCheckStatus.getStyleClass().add("btn-secondary");
        btnCheckStatus.setOnAction(e -> handleCheckStatus());

        row2.getChildren().addAll(btnDeleteCandidate, btnApplyTest, btnCheckStatus);

        buttonBox.getChildren().addAll(row1, row2);
        card.getChildren().addAll(titleLabel, formGrid, buttonBox);
        return card;
    }

    private VBox createCandidateDataCard() {
        VBox card = new VBox(16);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(20));

        // Master Candidate Directory Table
        Label lblMasterHeader = new Label("Candidate Records Directory");
        lblMasterHeader.getStyleClass().add("card-title");

        setupCandidateMasterTable();
        candidateMasterTable.setItems(candidateData);
        candidateMasterTable.setPrefHeight(180);

        // Relational TableView displaying ArrayList<Test> relation
        Label lblRelationalHeader = new Label("Associated Tests Relation (ArrayList<Test>)");
        lblRelationalHeader.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2A4D7C;");

        setupAssociatedTestTable();
        testTableView.setItems(testData);
        testTableView.setPrefHeight(160);

        card.getChildren().addAll(lblMasterHeader, candidateMasterTable, lblRelationalHeader, testTableView);
        return card;
    }

    private VBox createFormField(String labelText, Control inputControl, String promptText) {
        VBox box = new VBox(4);
        Label label = new Label(labelText);
        label.getStyleClass().add("nts-form-label");
        if (inputControl instanceof TextField tf) {
            tf.getStyleClass().add("nts-input-field");
            tf.setPromptText(promptText);
        } else if (inputControl instanceof PasswordField pf) {
            pf.getStyleClass().add("nts-input-field");
            pf.setPromptText(promptText);
        }
        inputControl.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().addAll(label, inputControl);
        return box;
    }

    @SuppressWarnings("unchecked")
    private void setupCandidateMasterTable() {
        TableColumn<Candidate, Integer> colFormNo = new TableColumn<>("Form #");
        colFormNo.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getFormNo()).asObject());

        TableColumn<Candidate, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Candidate, String> colFname = new TableColumn<>("Father Name");
        colFname.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFname()));

        TableColumn<Candidate, String> colIdCard = new TableColumn<>("ID Card");
        colIdCard.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdCard()));

        TableColumn<Candidate, String> colPhone = new TableColumn<>("Phone No");
        colPhone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNo()));

        TableColumn<Candidate, String> colEmail = new TableColumn<>("Candidate Email");
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCandidateEmail()));

        TableColumn<Candidate, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus() != null && data.getValue().getStatus() ? "Applied" : "Not Applied"));

        candidateMasterTable.getColumns().addAll(colFormNo, colName, colFname, colIdCard, colPhone, colEmail, colStatus);
        candidateMasterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    @SuppressWarnings("unchecked")
    private void setupAssociatedTestTable() {
        TableColumn<Test, Integer> colTestID = new TableColumn<>("Test ID");
        colTestID.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getTestID()).asObject());

        TableColumn<Test, String> colTestName = new TableColumn<>("Test Name");
        colTestName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTestName()));

        TableColumn<Test, Integer> colMarks = new TableColumn<>("Marks");
        colMarks.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getMarks()).asObject());

        TableColumn<Test, Double> colCharges = new TableColumn<>("Charges (PKR)");
        colCharges.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCharges()).asObject());

        TableColumn<Test, Float> colPassing = new TableColumn<>("Passing %");
        colPassing.setCellValueFactory(data -> new SimpleFloatProperty(data.getValue().getPassingPer()).asObject());

        testTableView.getColumns().addAll(colTestID, colTestName, colMarks, colCharges, colPassing);
        testTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    // 1:1 Action Button Handlers mapping UML methods
    private void handleAddBasicData() {
        try {
            String name = txtName.getText().trim();
            String fname = txtFname.getText().trim();
            String idCard = validateNumericField(txtIdCard.getText().trim(), "ID Card");
            String phoneNo = validateNumericField(txtPhoneNo.getText().trim(), "Phone No");
            String email = txtCandidateEmail.getText().trim();
            String pass = txtCandidatePass.getText().trim();
            int formNo = Integer.parseInt(txtFormNo.getText().trim());

            Candidate c = new Candidate();
            c.addBasicData(name, fname, idCard, phoneNo, formNo, email, pass);
            Candidate.candidates.add(c);

            refreshCandidateData();
            candidateMasterTable.getSelectionModel().select(c);
            DialogHelper.showInformation("Candidate Portal", "Add Basic Data Executed", "Candidate basic data created successfully for Form #" + formNo);
        } catch (Exception e) {
            DialogHelper.showError("Validation Error", "Invalid Input", e.getMessage());
        }
    }

    private void handleUpdateCandidateInfo() {
        Candidate selected = candidateMasterTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("No Selection", "Update Error", "Please select a candidate from the table first.");
            return;
        }

        try {
            String name = txtName.getText().trim();
            String fname = txtFname.getText().trim();
            String idCard = validateNumericField(txtIdCard.getText().trim(), "ID Card");
            String phoneNo = validateNumericField(txtPhoneNo.getText().trim(), "Phone No");
            String email = txtCandidateEmail.getText().trim();
            String pass = txtCandidatePass.getText().trim();
            int formNo = Integer.parseInt(txtFormNo.getText().trim());

            selected.updateCandidateInfo(name, fname, idCard, phoneNo, formNo, email, pass);

            candidateMasterTable.refresh();
            DialogHelper.showInformation("Candidate Portal", "Update Candidate Info Executed", "Candidate info updated successfully for Form #" + formNo);
        } catch (Exception e) {
            DialogHelper.showError("Validation Error", "Invalid Input", e.getMessage());
        }
    }

    private void handleDeleteCandidate() {
        Candidate selected = candidateMasterTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("No Selection", "Delete Error", "Please select a candidate to delete.");
            return;
        }

        DialogHelper.showConfirmation("Confirm Deletion", "Delete Candidate", "Delete candidate record for Form #" + selected.getFormNo() + "?", () -> {
            selected.deleteCandidate();
            refreshCandidateData();
            clearFormInputs();
            DialogHelper.showInformation("Candidate Portal", "Delete Candidate Executed", "Candidate deleted successfully.");
        });
    }

    private void handleApplyTest() {
        Candidate selected = candidateMasterTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("No Selection", "Apply Test Error", "Please select a candidate to apply test for.");
            return;
        }

        // Apply test logic: add a test to the Candidate's ArrayList<Test>
        Test newTest = new Test(101, "NAT-I Aptitude", 90, 850.0, 60.0f);
        if (selected.getTest() == null) {
            selected.setTest(new ArrayList<>());
        }
        selected.getTest().add(newTest);
        selected.applyTest();

        candidateMasterTable.refresh();
        updateAssociatedTestsTable(selected);

        DialogHelper.showInformation("ApplyTest Interface", "applyTest() Executed", "Candidate " + selected.getName() + " successfully applied for test: " + newTest.getTestName());
    }

    private void handleCheckStatus() {
        Candidate selected = candidateMasterTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("No Selection", "Status Error", "Please select a candidate to check status.");
            return;
        }

        selected.checkstatus();
        String statusStr = (selected.getStatus() != null && selected.getStatus()) ? "Applied for Test(s)" : "Not Applied";
        DialogHelper.showInformation("ApplyTest Interface", "checkStatus() Executed", "Candidate " + selected.getName() + " Status: " + statusStr + "\nTotal Registered Tests: " + (selected.getTest() != null ? selected.getTest().size() : 0));
    }

    private String validateNumericField(String input, String fieldName) throws IllegalArgumentException {
        if (input == null || input.isEmpty() || !input.matches("\\d+")) {
            throw new IllegalArgumentException(fieldName + " must contain numeric digits only.");
        }
        return input;
    }

    private void populateForm(Candidate c) {
        txtName.setText(c.getName() != null ? c.getName() : "");
        txtFname.setText(c.getFname() != null ? c.getFname() : "");
        txtIdCard.setText(c.getIdCard() != null ? c.getIdCard() : "");
        txtPhoneNo.setText(c.getPhoneNo() != null ? c.getPhoneNo() : "");
        txtCandidateEmail.setText(c.getCandidateEmail() != null ? c.getCandidateEmail() : "");
        txtCandidatePass.setText(c.getCandidatePass() != null ? c.getCandidatePass() : "");
        txtFormNo.setText(String.valueOf(c.getFormNo()));
    }

    private void updateAssociatedTestsTable(Candidate c) {
        testData.clear();
        if (c != null && c.getTest() != null) {
            testData.addAll(c.getTest());
        }
    }

    private void clearFormInputs() {
        txtName.clear();
        txtFname.clear();
        txtIdCard.clear();
        txtPhoneNo.clear();
        txtCandidateEmail.clear();
        txtCandidatePass.clear();
        txtFormNo.clear();
        testData.clear();
    }

    private void refreshCandidateData() {
        if (Candidate.candidates.isEmpty()) {
            Candidate c1 = new Candidate();
            c1.addBasicData("Ali Ahmed", "Muhammad Ahmed", "3520212345671", "03001234567", 1001, "ali@nts.org.pk", "pass123");
            ArrayList<Test> tList1 = new ArrayList<>();
            tList1.add(new Test(101, "NAT-I Aptitude", 90, 850.0, 60.0f));
            c1.setTest(tList1);
            c1.applyTest();

            Candidate c2 = new Candidate();
            c2.addBasicData("Fatima Khan", "Tariq Khan", "6110198765432", "03129876543", 1002, "fatima@nts.org.pk", "pass123");
            ArrayList<Test> tList2 = new ArrayList<>();
            tList2.add(new Test(102, "GAT General", 100, 1350.0, 50.0f));
            c2.setTest(tList2);
            c2.applyTest();

            Candidate.candidates.add(c1);
            Candidate.candidates.add(c2);
        }

        candidateData.setAll(Candidate.candidates);
    }
}
