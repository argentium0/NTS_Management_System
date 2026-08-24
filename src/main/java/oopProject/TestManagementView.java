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
import java.util.List;

/**
 * TestManagementView mapped 1:1 to Test UML class architecture.
 * Form Fields: testID, testName, marks, charges, passingPer.
 * Action Buttons: Update Test Info, Delete Test Info, Check Test Details.
 */
public class TestManagementView extends VBox {

    // Form Controls
    private final TextField txtTestID = new TextField();
    private final TextField txtTestName = new TextField();
    private final TextField txtMarks = new TextField();
    private final TextField txtCharges = new TextField();
    private final TextField txtPassingPer = new TextField();

    // TableView & Data Model
    private final TableView<Test> testTable = new TableView<>();
    private final ObservableList<Test> testData = FXCollections.observableArrayList();

    public TestManagementView() {
        setSpacing(20);
        setPadding(new Insets(0));

        HBox mainContent = new HBox(20);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        VBox formCard = createTestFormCard();
        formCard.setPrefWidth(380);
        formCard.setMinWidth(340);

        VBox tableCard = createTestTableCard();
        HBox.setHgrow(tableCard, Priority.ALWAYS);

        mainContent.getChildren().addAll(formCard, tableCard);
        getChildren().add(mainContent);

        refreshTestData();

        testTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateForm(newVal);
            }
        });

        if (!testData.isEmpty()) {
            testTable.getSelectionModel().selectFirst();
        }
    }

    private VBox createTestFormCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(20));

        Label titleLabel = new Label("Test Entity Management Form");
        titleLabel.getStyleClass().add("card-title");

        VBox formGrid = new VBox(10);
        formGrid.getChildren().addAll(
                createFormField("Test ID (testID)", txtTestID, "Numeric test ID..."),
                createFormField("Test Name (testName)", txtTestName, "e.g. NAT-I Aptitude"),
                createFormField("Total Marks (marks)", txtMarks, "e.g. 100"),
                createFormField("Test Charges (charges)", txtCharges, "e.g. 850.0"),
                createFormField("Passing Percentage (passingPer)", txtPassingPer, "e.g. 60.0")
        );

        // Action Buttons mapped 1:1 to UML methods
        VBox buttonBox = new VBox(10);

        HBox row1 = new HBox(10);
        Button btnUpdateTestInfo = new Button("Update Test Info");
        btnUpdateTestInfo.getStyleClass().add("btn-primary");
        btnUpdateTestInfo.setOnAction(e -> handleUpdateTestInfo());
        btnUpdateTestInfo.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnUpdateTestInfo, Priority.ALWAYS);

        Button btnDeleteTestInfo = new Button("Delete Test Info");
        btnDeleteTestInfo.getStyleClass().add("btn-secondary");
        btnDeleteTestInfo.setStyle("-fx-text-fill: #DC2626; -fx-border-color: #FCA5A5;");
        btnDeleteTestInfo.setOnAction(e -> handleDeleteTestInfo());

        row1.getChildren().addAll(btnUpdateTestInfo, btnDeleteTestInfo);

        Button btnCheckTestDetails = new Button("Check Test Details");
        btnCheckTestDetails.getStyleClass().add("btn-accent");
        btnCheckTestDetails.setMaxWidth(Double.MAX_VALUE);
        btnCheckTestDetails.setOnAction(e -> handleCheckTestDetails());

        buttonBox.getChildren().addAll(row1, btnCheckTestDetails);
        card.getChildren().addAll(titleLabel, formGrid, buttonBox);
        return card;
    }

    private VBox createTestTableCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(20));

        Label cardHeader = new Label("NTS Test Directory");
        cardHeader.getStyleClass().add("card-title");

        setupTestTable();
        testTable.setItems(testData);
        VBox.setVgrow(testTable, Priority.ALWAYS);

        card.getChildren().addAll(cardHeader, testTable);
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

    @SuppressWarnings("unchecked")
    private void setupTestTable() {
        TableColumn<Test, Integer> colID = new TableColumn<>("Test ID");
        colID.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getTestID()).asObject());

        TableColumn<Test, String> colName = new TableColumn<>("Test Name");
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTestName()));

        TableColumn<Test, Integer> colMarks = new TableColumn<>("Marks");
        colMarks.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getMarks()).asObject());

        TableColumn<Test, Double> colCharges = new TableColumn<>("Charges (PKR)");
        colCharges.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCharges()).asObject());

        TableColumn<Test, Float> colPassing = new TableColumn<>("Passing %");
        colPassing.setCellValueFactory(data -> new SimpleFloatProperty(data.getValue().getPassingPer()).asObject());

        testTable.getColumns().addAll(colID, colName, colMarks, colCharges, colPassing);
        testTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private final TestDAO testDAO = new TestDAO();

    // Action Handlers mapping UML methods 1:1
    private void handleUpdateTestInfo() {
        Test selected = testTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            // Save new Test to SQLite
            try {
                int testID = Integer.parseInt(txtTestID.getText().trim());
                String name = txtTestName.getText().trim();
                int marks = Integer.parseInt(txtMarks.getText().trim());
                double charges = Double.parseDouble(txtCharges.getText().trim());
                float passing = Float.parseFloat(txtPassingPer.getText().trim());

                Test newTest = new Test(testID, name, marks, charges, passing);
                testDAO.addTest(newTest);
                refreshTestData();

                DialogHelper.showInformation("Test Management", "updateTestInfo() Executed", "New test created in SQLite: " + name + " (ID: " + testID + ")");
            } catch (Exception e) {
                DialogHelper.showError("Input Error", "Invalid Form Data", e.getMessage());
            }
            return;
        }

        try {
            int testID = Integer.parseInt(txtTestID.getText().trim());
            String name = txtTestName.getText().trim();
            int marks = Integer.parseInt(txtMarks.getText().trim());
            double charges = Double.parseDouble(txtCharges.getText().trim());
            float passing = Float.parseFloat(txtPassingPer.getText().trim());

            selected.setTestID(testID);
            selected.updateTestInfo(name, marks, charges, passing);
            testDAO.updateTest(selected);
            refreshTestData();

            DialogHelper.showInformation("Test Management", "updateTestInfo() Executed", "Test info updated in SQLite for: " + name);
        } catch (Exception e) {
            DialogHelper.showError("Input Error", "Invalid Form Data", e.getMessage());
        }
    }

    private void handleDeleteTestInfo() {
        Test selected = testTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("No Selection", "Delete Error", "Please select a test to delete.");
            return;
        }

        DialogHelper.showConfirmation("Confirm Deletion", "Delete Test Record", "Delete test: " + selected.getTestName() + " (ID: " + selected.getTestID() + ")?", () -> {
            testDAO.deleteTest(selected.getTestID());
            refreshTestData();
            clearFormInputs();
            DialogHelper.showInformation("Test Management", "deleteTestInfo() Executed", "Test record deleted from SQLite successfully.");
        });
    }

    private void handleCheckTestDetails() {
        Test selected = testTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("No Selection", "Check Details Error", "Please select a test from the table.");
            return;
        }

        selected.checkTestDetails();
        DialogHelper.showInformation("TestManagement Interface", "checkTestDetails() Executed",
                "Test ID: " + selected.getTestID() + "\n" +
                        "Test Name: " + selected.getTestName() + "\n" +
                        "Total Marks: " + selected.getMarks() + "\n" +
                        "Charges: PKR " + selected.getCharges() + "\n" +
                        "Passing Percentage: " + selected.getPassingPer() + "%");
    }

    private void populateForm(Test t) {
        txtTestID.setText(String.valueOf(t.getTestID()));
        txtTestName.setText(t.getTestName() != null ? t.getTestName() : "");
        txtMarks.setText(String.valueOf(t.getMarks()));
        txtCharges.setText(String.valueOf(t.getCharges()));
        txtPassingPer.setText(String.valueOf(t.getPassingPer()));
    }

    private void clearFormInputs() {
        txtTestID.clear();
        txtTestName.clear();
        txtMarks.clear();
        txtCharges.clear();
        txtPassingPer.clear();
    }

    private void refreshTestData() {
        List<Test> dbTests = testDAO.getAllTests();
        if (dbTests.isEmpty()) {
            testDAO.addTest(new Test(101, "NAT-I Aptitude Test", 90, 850.0, 60.0f));
            testDAO.addTest(new Test(102, "GAT General Test", 100, 1350.0, 50.0f));
            testDAO.addTest(new Test(103, "TOEIC English Test", 990, 22000.0, 45.0f));
            dbTests = testDAO.getAllTests();
        }

        testData.setAll(dbTests);
    }
}
