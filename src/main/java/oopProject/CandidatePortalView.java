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
 * CandidatePortalView implements Phase 2 of the NTS Management System JavaFX upgrade.
 * Features a minimalist CRUD form UI cleanly positioned beside a TableView for candidate records.
 */
public class CandidatePortalView extends VBox {

    // Form Controls
    private final TextField txtFormNo = new TextField();
    private final TextField txtName = new TextField();
    private final TextField txtFatherName = new TextField();
    private final TextField txtCnic = new TextField();
    private final TextField txtPhone = new TextField();
    private final TextField txtEmail = new TextField();
    private final ComboBox<String> cbTestType = new ComboBox<>();

    // Search Controls
    private final TextField txtSearchFormNo = new TextField();

    // TableView & Observable Data Model
    private final TableView<Candidate> candidateTable = new TableView<>();
    private final ObservableList<Candidate> candidateData = FXCollections.observableArrayList();

    public CandidatePortalView() {
        setSpacing(20);
        setPadding(new Insets(0));

        // Build Master Layout: Left (Apply/CRUD Form Pane) | Right (Data Directory Table)
        HBox mainContent = new HBox(20);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        VBox formCard = createCandidateFormCard();
        formCard.setPrefWidth(380);
        formCard.setMinWidth(340);

        VBox tableCard = createCandidateTableCard();
        HBox.setHgrow(tableCard, Priority.ALWAYS);

        mainContent.getChildren().addAll(formCard, tableCard);
        getChildren().add(mainContent);

        // Load Initial Seed Data / Sync with Candidate.candidates
        refreshTableData();
    }

    /* ==========================================================================
       1. FORM SECTION: "APPLY FOR TEST & CANDIDATE REGISTRATION"
       ========================================================================== */
    private VBox createCandidateFormCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(20));

        // Section Title
        Label titleLabel = new Label("Candidate & Test Registration");
        titleLabel.getStyleClass().add("card-title");

        // Form Fields (Strict minimal UI with explicit Labels above fields)
        VBox formGrid = new VBox(10);

        formGrid.getChildren().addAll(
                createFormField("Form Number", txtFormNo),
                createFormField("Full Name", txtName),
                createFormField("Father Name", txtFatherName),
                createFormField("CNIC Number", txtCnic),
                createFormField("Phone Number", txtPhone),
                createFormField("Email Address", txtEmail)
        );

        // Test Type Selection ComboBox
        VBox testBox = new VBox(4);
        Label lblTestType = new Label("Select Test Type");
        lblTestType.setStyle("-fx-font-weight: bold; -fx-text-fill: #334155; -fx-font-size: 11px;");
        cbTestType.getItems().addAll("NAT (National Aptitude Test)", "GAT (Graduate Assessment Test)", "TOEIC (Test of English)");
        cbTestType.setValue("NAT (National Aptitude Test)");
        cbTestType.setMaxWidth(Double.MAX_VALUE);
        testBox.getChildren().addAll(lblTestType, cbTestType);

        formGrid.getChildren().add(testBox);

        // Action Buttons
        HBox buttonBar1 = new HBox(10);
        Button btnApply = new Button("Apply For Test");
        btnApply.getStyleClass().add("btn-accent");
        btnApply.setOnAction(this::handleApplyForTest);
        btnApply.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnApply, Priority.ALWAYS);

        Button btnAdd = new Button("Save Candidate");
        btnAdd.getStyleClass().add("btn-primary");
        btnAdd.setOnAction(this::handleAddCandidate);

        buttonBar1.getChildren().addAll(btnApply, btnAdd);

        HBox buttonBar2 = new HBox(10);
        Button btnUpdate = new Button("Update");
        btnUpdate.getStyleClass().add("btn-secondary");
        btnUpdate.setOnAction(this::handleUpdateCandidate);

        Button btnDelete = new Button("Delete");
        btnDelete.getStyleClass().add("btn-secondary");
        btnDelete.setStyle("-fx-text-fill: #DC2626; -fx-border-color: #FCA5A5;");
        btnDelete.setOnAction(this::handleDeleteCandidate);

        Button btnClear = new Button("Clear Form");
        btnClear.getStyleClass().add("btn-secondary");
        btnClear.setOnAction(this::handleClearForm);

        buttonBar2.getChildren().addAll(btnUpdate, btnDelete, btnClear);

        card.getChildren().addAll(titleLabel, formGrid, buttonBar1, buttonBar2);
        return card;
    }

    private VBox createFormField(String labelText, TextField textField) {
        VBox box = new VBox(4);
        Label label = new Label(labelText);
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #334155; -fx-font-size: 11px;");
        textField.setMaxWidth(Double.MAX_VALUE);
        box.getChildren().addAll(label, textField);
        return box;
    }

    /* ==========================================================================
       2. DATA DISPLAY SECTION: TABLEVIEW & SEARCH BAR
       ========================================================================== */
    private VBox createCandidateTableCard() {
        VBox card = new VBox(12);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(20));

        // Header & Search Filter Bar
        HBox headerBar = new HBox(12);
        headerBar.setAlignment(Pos.CENTER_LEFT);

        Label cardHeader = new Label("Candidate Records Directory");
        cardHeader.getStyleClass().add("card-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        txtSearchFormNo.setPromptText("Enter Form No...");
        txtSearchFormNo.setPrefWidth(160);

        Button btnSearch = new Button("Search");
        btnSearch.getStyleClass().add("btn-secondary");
        btnSearch.setOnAction(this::handleSearchCandidate);

        Button btnReset = new Button("Show All");
        btnReset.getStyleClass().add("btn-secondary");
        btnReset.setOnAction(e -> refreshTableData());

        headerBar.getChildren().addAll(cardHeader, spacer, txtSearchFormNo, btnSearch, btnReset);

        // Setup TableView Columns bound to Candidate model
        setupTableColumns();

        candidateTable.setItems(candidateData);
        candidateTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(candidateTable, Priority.ALWAYS);

        // Selection Listener for populating form
        candidateTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateForm(newVal);
            }
        });

        card.getChildren().addAll(headerBar, candidateTable);
        return card;
    }

    /**
     * Binds TableView columns directly to the Candidate entity properties.
     */
    @SuppressWarnings("unchecked")
    private void setupTableColumns() {
        TableColumn<Candidate, Integer> colFormNo = new TableColumn<>("Form #");
        colFormNo.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getFormNo()).asObject());
        colFormNo.setPrefWidth(70);

        TableColumn<Candidate, String> colName = new TableColumn<>("Candidate Name");
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Candidate, String> colFatherName = new TableColumn<>("Father Name");
        colFatherName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFname()));

        TableColumn<Candidate, String> colCnic = new TableColumn<>("CNIC");
        colCnic.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdCard()));

        TableColumn<Candidate, String> colPhone = new TableColumn<>("Phone");
        colPhone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhoneNo()));

        TableColumn<Candidate, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCandidateEmail()));

        TableColumn<Candidate, String> colTest = new TableColumn<>("Test Type");
        colTest.setCellValueFactory(data -> {
            Test t = data.getValue().getTest();
            return new SimpleStringProperty((t != null && t.getTestName() != null) ? t.getTestName() : "Unassigned");
        });

        TableColumn<Candidate, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(data -> {
            Boolean status = data.getValue().getStatus();
            return new SimpleStringProperty((status != null && status) ? "Applied" : "Not Applied");
        });

        candidateTable.getColumns().addAll(colFormNo, colName, colFatherName, colCnic, colPhone, colEmail, colTest, colStatus);
    }

    /* ==========================================================================
       3. EVENT HANDLERS & BUSINESS LOGIC HOOKS
       ========================================================================== */

    /**
     * Stub for applying for a test via the Candidate domain model and ApplyTest interface.
     */
    public void handleApplyForTest(ActionEvent event) {
        Candidate selected = candidateTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a candidate from the table or fill form details first.");
            return;
        }

        String selectedTest = cbTestType.getValue();
        if (selected.getTest() == null) {
            selected.setTest(new Test());
        }
        selected.getTest().setTestName(selectedTest != null ? selectedTest.split(" ")[0] : "NAT");
        selected.setStatus(true);

        refreshTableData();
        DialogHelper.showInformation("Test Application", "Candidate Registered", "Candidate " + selected.getName() + " successfully registered for " + selectedTest);
    }

    public void handleAddCandidate(ActionEvent event) {
        try {
            int formNo = Integer.parseInt(txtFormNo.getText().trim());
            String name = txtName.getText().trim();
            String fname = txtFatherName.getText().trim();
            String cnic = txtCnic.getText().trim();
            String phone = txtPhone.getText().trim();
            String email = txtEmail.getText().trim();

            Candidate candidate = new Candidate(name, fname, cnic, phone, formNo, email, "pass123", "NAT", 90, 850, 60.0f, null, false);
            Candidate.candidates.add(candidate);

            refreshTableData();
            handleClearForm(null);
            DialogHelper.showInformation("Success", "Candidate Registered", "Candidate record added successfully.");
        } catch (NumberFormatException e) {
            DialogHelper.showError("Input Error", "Invalid Form Number", "Please enter a valid numeric Form Number.");
        }
    }

    public void handleSearchCandidate(ActionEvent event) {
        String searchStr = txtSearchFormNo.getText().trim();
        if (searchStr.isEmpty()) {
            refreshTableData();
            return;
        }

        try {
            int targetFormNo = Integer.parseInt(searchStr);
            candidateData.clear();
            for (Candidate c : Candidate.candidates) {
                if (c.getFormNo() == targetFormNo) {
                    candidateData.add(c);
                }
            }
        } catch (NumberFormatException e) {
            DialogHelper.showError("Search Error", "Invalid Form Number", "Please enter a valid numeric Form Number to search.");
        }
    }

    public void handleUpdateCandidate(ActionEvent event) {
        Candidate selected = candidateTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("Selection Required", "No Candidate Selected", "Please select a candidate to update.");
            return;
        }

        selected.setName(txtName.getText().trim());
        selected.setFname(txtFatherName.getText().trim());
        selected.setIdCard(txtCnic.getText().trim());
        selected.setPhoneNo(txtPhone.getText().trim());
        selected.setCandidateEmail(txtEmail.getText().trim());

        refreshTableData();
        DialogHelper.showInformation("Updated", "Candidate Updated", "Candidate details updated successfully.");
    }

    public void handleDeleteCandidate(ActionEvent event) {
        Candidate selected = candidateTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("Selection Required", "No Candidate Selected", "Please select a candidate to delete.");
            return;
        }

        DialogHelper.showConfirmation("Confirm Delete", "Remove Candidate Record",
                "Are you sure you want to delete candidate " + selected.getName() + " (Form #" + selected.getFormNo() + ")?",
                () -> {
                    Candidate.candidates.remove(selected);
                    refreshTableData();
                    handleClearForm(null);
                    DialogHelper.showInformation("Deleted", "Candidate Removed", "Candidate record deleted successfully.");
                });
    }


    public void handleClearForm(ActionEvent event) {
        txtFormNo.clear();
        txtName.clear();
        txtFatherName.clear();
        txtCnic.clear();
        txtPhone.clear();
        txtEmail.clear();
        cbTestType.getSelectionModel().selectFirst();
        candidateTable.getSelectionModel().clearSelection();
    }

    private void populateForm(Candidate c) {
        txtFormNo.setText(String.valueOf(c.getFormNo()));
        txtName.setText(c.getName() != null ? c.getName() : "");
        txtFatherName.setText(c.getFname() != null ? c.getFname() : "");
        txtCnic.setText(c.getIdCard() != null ? c.getIdCard() : "");
        txtPhone.setText(c.getPhoneNo() != null ? c.getPhoneNo() : "");
        txtEmail.setText(c.getCandidateEmail() != null ? c.getCandidateEmail() : "");
    }

    private void refreshTableData() {
        // If empty, inject initial demonstration candidates
        if (Candidate.candidates.isEmpty()) {
            Candidate.candidates.add(new Candidate("Ali Ahmed", "Muhammad Ahmed", "35202-1234567-1", "0300-1234567", 1001, "ali@nts.org.pk", "pass123", "NAT", 90, 850, 60.0f, null, true));
            Candidate.candidates.add(new Candidate("Fatima Khan", "Tariq Khan", "61101-9876543-2", "0312-9876543", 1002, "fatima@nts.org.pk", "pass123", "GAT", 100, 1350, 50.0f, null, true));
            Candidate.candidates.add(new Candidate("Usman Raza", "Raza Hussain", "42101-5554443-3", "0333-5554443", 1003, "usman@nts.org.pk", "pass123", "TOEIC", 990, 22000, 45.0f, null, false));
        }

        candidateData.setAll(Candidate.candidates);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
