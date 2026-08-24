package oopProject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;

/**
 * TestCentreAllocationView mapped 1:1 to TestCentre UML class architecture.
 * Form Fields: testCentreNo, testCentreBuilding, testCentreAdress, allocationDate.
 * Relational UI: Selection mechanisms (ComboBox/ListView) to populate invigilators: ArrayList<Invigilator> and superintendents: ArrayList<Superintendent>, as well as associated Test.
 * Action Buttons: Add Superintendent, Add Invigilators, Update, Delete.
 */
public class TestCentreAllocationView extends VBox {

    // TestCentre Form Fields
    private final TextField txtTestCentreNo = new TextField();
    private final TextField txtTestCentreBuilding = new TextField();
    private final TextField txtTestCentreAdress = new TextField();
    private final TextField txtAllocationDate = new TextField();

    // Relational Selection UI Controls
    private final ComboBox<Superintendent> cbSuperintendentSelection = new ComboBox<>();
    private final ComboBox<Invigilator> cbInvigilatorSelection = new ComboBox<>();
    private final ComboBox<Test> cbTestSelection = new ComboBox<>();

    // Master Table & Relational Data Lists
    private final TableView<TestCentre> centreMasterTable = new TableView<>();
    private final ObservableList<TestCentre> centreData = FXCollections.observableArrayList();

    private final ListView<Superintendent> listSuperintendents = new ListView<>();
    private final ObservableList<Superintendent> assignedSuperintendents = FXCollections.observableArrayList();

    private final ListView<Invigilator> listInvigilators = new ListView<>();
    private final ObservableList<Invigilator> assignedInvigilators = FXCollections.observableArrayList();

    private final Label lblAssociatedTest = new Label("Associated Test: Unassigned");

    public TestCentreAllocationView() {
        setSpacing(20);
        setPadding(new Insets(0));

        HBox mainContent = new HBox(20);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        VBox formCard = createCentreFormCard();
        formCard.setPrefWidth(420);
        formCard.setMinWidth(380);

        VBox tableCard = createCentreTableCard();
        HBox.setHgrow(tableCard, Priority.ALWAYS);

        mainContent.getChildren().addAll(formCard, tableCard);
        getChildren().add(mainContent);

        refreshMasterData();

        centreMasterTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateForm(newVal);
                updateRelationalViews(newVal);
            }
        });

        if (!centreData.isEmpty()) {
            centreMasterTable.getSelectionModel().selectFirst();
        }
    }

    private VBox createCentreFormCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(20));

        Label titleLabel = new Label("Test Centre Form & Relational Team Allocation");
        titleLabel.getStyleClass().add("card-title");

        // Basic Fields Grid
        VBox formGrid = new VBox(8);
        formGrid.getChildren().addAll(
                createFormField("Test Centre No (testCentreNo)", txtTestCentreNo, "Numeric centre number..."),
                createFormField("Test Centre Building (testCentreBuilding)", txtTestCentreBuilding, "e.g. University Hall A"),
                createFormField("Test Centre Address (testCentreAdress)", txtTestCentreAdress, "City / Address..."),
                createFormField("Allocation Date (allocationDate)", txtAllocationDate, "e.g. 2026-09-15")
        );

        // Relational ComboBox Controls
        VBox relationalSelectionBox = new VBox(10);
        relationalSelectionBox.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 12; -fx-border-color: #CBD5E1; -fx-border-width: 1px;");

        Label lblRelHeader = new Label("Relational Entity Selection Mechanisms");
        lblRelHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 11px; -fx-text-fill: #2A4D7C;");

        // Superintendent Selector & Action
        HBox spdRow = new HBox(8);
        cbSuperintendentSelection.setPromptText("Select Superintendent...");
        cbSuperintendentSelection.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cbSuperintendentSelection, Priority.ALWAYS);

        Button btnAddSuperintendent = new Button("Add Superintendent");
        btnAddSuperintendent.getStyleClass().add("btn-primary");
        btnAddSuperintendent.setOnAction(e -> handleAddSuperintendent());

        spdRow.getChildren().addAll(cbSuperintendentSelection, btnAddSuperintendent);

        // Invigilators Selector & Action
        HBox invRow = new HBox(8);
        cbInvigilatorSelection.setPromptText("Select Invigilator...");
        cbInvigilatorSelection.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cbInvigilatorSelection, Priority.ALWAYS);

        Button btnAddInvigilators = new Button("Add Invigilators");
        btnAddInvigilators.getStyleClass().add("btn-accent");
        btnAddInvigilators.setOnAction(e -> handleAddInvigilators());

        invRow.getChildren().addAll(cbInvigilatorSelection, btnAddInvigilators);

        // Test Selector
        HBox testRow = new HBox(8);
        cbTestSelection.setPromptText("Select Test...");
        cbTestSelection.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cbTestSelection, Priority.ALWAYS);

        Button btnAssignTest = new Button("Assign Test");
        btnAssignTest.getStyleClass().add("btn-secondary");
        btnAssignTest.setOnAction(e -> handleAssignTest());

        testRow.getChildren().addAll(cbTestSelection, btnAssignTest);

        relationalSelectionBox.getChildren().addAll(lblRelHeader, spdRow, invRow, testRow);

        // Action Buttons mapped 1:1 to UML methods: Update, Delete
        HBox actionRow = new HBox(10);
        Button btnUpdate = new Button("Update");
        btnUpdate.getStyleClass().add("btn-primary");
        btnUpdate.setOnAction(e -> handleUpdate());
        btnUpdate.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnUpdate, Priority.ALWAYS);

        Button btnDelete = new Button("Delete");
        btnDelete.getStyleClass().add("btn-secondary");
        btnDelete.setStyle("-fx-text-fill: #DC2626; -fx-border-color: #FCA5A5;");
        btnDelete.setOnAction(e -> handleDelete());

        actionRow.getChildren().addAll(btnUpdate, btnDelete);

        card.getChildren().addAll(titleLabel, formGrid, relationalSelectionBox, actionRow);

        ScrollPane scroll = new ScrollPane(card);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        VBox wrapper = new VBox(scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        return wrapper;
    }

    private VBox createCentreTableCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(20));

        Label cardHeader = new Label("Test Centre Directory");
        cardHeader.getStyleClass().add("card-title");

        setupCentreTable();
        centreMasterTable.setItems(centreData);
        centreMasterTable.setPrefHeight(160);

        // Relational Lists Views for ArrayList<Superintendent> and ArrayList<Invigilator>
        GridPane relationalGrid = new GridPane();
        relationalGrid.setHgap(12);
        relationalGrid.setVgap(12);

        VBox spdBox = new VBox(6);
        Label lblSpd = new Label("Assigned Superintendents (ArrayList<Superintendent>)");
        lblSpd.setStyle("-fx-font-weight: bold; -fx-font-size: 11px; -fx-text-fill: #2A4D7C;");
        listSuperintendents.setItems(assignedSuperintendents);
        listSuperintendents.setPrefHeight(120);
        spdBox.getChildren().addAll(lblSpd, listSuperintendents);

        VBox invBox = new VBox(6);
        Label lblInv = new Label("Assigned Invigilators (ArrayList<Invigilator>)");
        lblInv.setStyle("-fx-font-weight: bold; -fx-font-size: 11px; -fx-text-fill: #2A4D7C;");
        listInvigilators.setItems(assignedInvigilators);
        listInvigilators.setPrefHeight(120);
        invBox.getChildren().addAll(lblInv, listInvigilators);

        relationalGrid.add(spdBox, 0, 0);
        relationalGrid.add(invBox, 1, 0);

        ColumnConstraints c1 = new ColumnConstraints();
        c1.setPercentWidth(50);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(50);
        relationalGrid.getColumnConstraints().addAll(c1, c2);

        lblAssociatedTest.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #2A4D7C; -fx-background-color: #F1F5F9; -fx-padding: 8;");

        card.getChildren().addAll(cardHeader, centreMasterTable, relationalGrid, lblAssociatedTest);
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
    private void setupCentreTable() {
        TableColumn<TestCentre, Integer> colNo = new TableColumn<>("Centre #");
        colNo.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getTestCentreNo()).asObject());

        TableColumn<TestCentre, String> colBuilding = new TableColumn<>("Building");
        colBuilding.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTestCentreBuilding()));

        TableColumn<TestCentre, String> colAdress = new TableColumn<>("Address / City");
        colAdress.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTestCentreAdress()));

        TableColumn<TestCentre, String> colDate = new TableColumn<>("Allocation Date");
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAllocationDate()));

        centreMasterTable.getColumns().addAll(colNo, colBuilding, colAdress, colDate);
        centreMasterTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    // Action Handlers mapping UML methods 1:1
    private void handleAddSuperintendent() {
        TestCentre selected = centreMasterTable.getSelectionModel().getSelectedItem();
        Superintendent spd = cbSuperintendentSelection.getValue();

        if (selected == null) {
            DialogHelper.showError("No Selection", "Add Superintendent Error", "Please select a Test Centre from the table.");
            return;
        }
        if (spd == null) {
            DialogHelper.showError("No Selection", "Add Superintendent Error", "Please select a Superintendent from the ComboBox dropdown.");
            return;
        }

        selected.addSuperintendent(spd);
        selected.addSuperintendent(); // Call UML interface method
        updateRelationalViews(selected);
        DialogHelper.showInformation("TestCentreTeamManagement Interface", "addSuperintendent() Executed", "Superintendent " + spd.getName() + " added to Test Centre #" + selected.getTestCentreNo());
    }

    private void handleAddInvigilators() {
        TestCentre selected = centreMasterTable.getSelectionModel().getSelectedItem();
        Invigilator inv = cbInvigilatorSelection.getValue();

        if (selected == null) {
            DialogHelper.showError("No Selection", "Add Invigilators Error", "Please select a Test Centre from the table.");
            return;
        }
        if (inv == null) {
            DialogHelper.showError("No Selection", "Add Invigilators Error", "Please select an Invigilator from the ComboBox dropdown.");
            return;
        }

        selected.addInvigilator(inv);
        selected.addInvigilators(); // Call UML interface method
        updateRelationalViews(selected);
        DialogHelper.showInformation("TestCentreTeamManagement Interface", "addInvigilators() Executed", "Invigilator " + inv.getName() + " added to Test Centre #" + selected.getTestCentreNo());
    }

    private void handleAssignTest() {
        TestCentre selected = centreMasterTable.getSelectionModel().getSelectedItem();
        Test test = cbTestSelection.getValue();

        if (selected == null || test == null) {
            DialogHelper.showError("No Selection", "Assign Test Error", "Please select both a Test Centre and a Test.");
            return;
        }

        selected.setTest(test);
        updateRelationalViews(selected);
        DialogHelper.showInformation("Test Centre", "Test Associated", "Test " + test.getTestName() + " assigned to Centre #" + selected.getTestCentreNo());
    }

    private final TestCentreDAO testCentreDAO = new TestCentreDAO();

    private void handleUpdate() {
        TestCentre selected = centreMasterTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            // Save new TestCentre to SQLite
            try {
                int no = Integer.parseInt(txtTestCentreNo.getText().trim());
                String building = txtTestCentreBuilding.getText().trim();
                String adress = txtTestCentreAdress.getText().trim();
                String date = txtAllocationDate.getText().trim();

                TestCentre tc = new TestCentre(no, building, adress, date);
                testCentreDAO.addTestCentre(tc);
                refreshMasterData();

                DialogHelper.showInformation("Test Centre", "update() Executed", "New Test Centre #" + no + " created in SQLite successfully.");
            } catch (Exception e) {
                DialogHelper.showError("Input Error", "Invalid Form Data", e.getMessage());
            }
            return;
        }

        try {
            int no = Integer.parseInt(txtTestCentreNo.getText().trim());
            String building = txtTestCentreBuilding.getText().trim();
            String adress = txtTestCentreAdress.getText().trim();
            String date = txtAllocationDate.getText().trim();

            selected.update(no, building, adress, date);
            testCentreDAO.updateTestCentre(selected);
            refreshMasterData();

            DialogHelper.showInformation("Test Centre", "update() Executed", "Test Centre #" + no + " updated in SQLite successfully.");
        } catch (Exception e) {
            DialogHelper.showError("Input Error", "Invalid Form Data", e.getMessage());
        }
    }

    private void handleDelete() {
        TestCentre selected = centreMasterTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            DialogHelper.showError("No Selection", "Delete Error", "Please select a Test Centre to delete.");
            return;
        }

        DialogHelper.showConfirmation("Confirm Deletion", "Delete Test Centre", "Delete Test Centre #" + selected.getTestCentreNo() + "?", () -> {
            testCentreDAO.deleteTestCentre(selected.getTestCentreNo());
            refreshMasterData();
            clearFormInputs();
            DialogHelper.showInformation("Test Centre", "delete() Executed", "Test Centre deleted from SQLite successfully.");
        });
    }

    private void populateForm(TestCentre tc) {
        txtTestCentreNo.setText(String.valueOf(tc.getTestCentreNo()));
        txtTestCentreBuilding.setText(tc.getTestCentreBuilding() != null ? tc.getTestCentreBuilding() : "");
        txtTestCentreAdress.setText(tc.getTestCentreAdress() != null ? tc.getTestCentreAdress() : "");
        txtAllocationDate.setText(tc.getAllocationDate() != null ? tc.getAllocationDate() : "");
    }

    private void updateRelationalViews(TestCentre tc) {
        assignedSuperintendents.clear();
        if (tc != null && tc.getSuperintendents() != null) {
            assignedSuperintendents.addAll(tc.getSuperintendents());
        }

        assignedInvigilators.clear();
        if (tc != null && tc.getInvigilators() != null) {
            assignedInvigilators.addAll(tc.getInvigilators());
        }

        if (tc != null && tc.getTest() != null && tc.getTest().getTestName() != null && !tc.getTest().getTestName().isEmpty()) {
            lblAssociatedTest.setText("Associated Test: " + tc.getTest().getTestName() + " (ID: " + tc.getTest().getTestID() + ")");
        } else {
            lblAssociatedTest.setText("Associated Test: Unassigned");
        }
    }

    private void clearFormInputs() {
        txtTestCentreNo.clear();
        txtTestCentreBuilding.clear();
        txtTestCentreAdress.clear();
        txtAllocationDate.clear();
        assignedSuperintendents.clear();
        assignedInvigilators.clear();
        lblAssociatedTest.setText("Associated Test: Unassigned");
    }

    private void refreshMasterData() {
        List<TestCentre> dbCentres = testCentreDAO.getAllTestCentres();
        if (dbCentres.isEmpty()) {
            TestCentre tc1 = new TestCentre(101, "University Campus Hall A", "Lahore", "2026-09-15");
            tc1.addSuperintendent(new Superintendent("Dr. Hamza Malik", "Muhammad Malik", "35202-1234567-1", "03001234567", 501, "Lahore", 1200.0f, 10, 0.0, 15000.0, 6));
            tc1.addInvigilator(new Invigilator("Saima Rashid", "Rashid Ahmed", "35202-5551122-1", "03215551122", 601, "Lahore", 500.0f, 4, 8500.0, 0.0, "Senior Invigilator", "Dr. Hamza Malik", 3001234567L));
            tc1.setTest(new Test(101, "NAT-I Aptitude Test", 90, 850.0, 60.0f));
            testCentreDAO.addTestCentre(tc1);

            TestCentre tc2 = new TestCentre(102, "Federal College Complex", "Islamabad", "2026-09-15");
            tc2.addSuperintendent(new Superintendent("Prof. Tariq Mahmood", "Mahmood Khan", "61101-7778899-3", "03127778899", 502, "Islamabad", 1500.0f, 12, 0.0, 18000.0, 12));
            tc2.addInvigilator(new Invigilator("Usman Ali", "Ali Asghar", "61101-4445566-4", "03004445566", 603, "Islamabad", 600.0f, 5, 7500.0, 0.0, "Invigilator", "Prof. Tariq Mahmood", 3127778899L));
            tc2.setTest(new Test(102, "GAT General Test", 100, 1350.0, 50.0f));
            testCentreDAO.addTestCentre(tc2);

            dbCentres = testCentreDAO.getAllTestCentres();
        }

        centreData.setAll(dbCentres);

        // Populate ComboBox dropdown options
        cbSuperintendentSelection.getItems().clear();
        cbSuperintendentSelection.getItems().add(new Superintendent("Dr. Hamza Malik", "Muhammad Malik", "35202-1234567-1", "03001234567", 501, "Lahore", 1200.0f, 10, 0.0, 15000.0, 6));
        cbSuperintendentSelection.getItems().add(new Superintendent("Prof. Tariq Mahmood", "Mahmood Khan", "61101-7778899-3", "03127778899", 502, "Islamabad", 1500.0f, 12, 0.0, 18000.0, 12));

        cbInvigilatorSelection.getItems().clear();
        cbInvigilatorSelection.getItems().add(new Invigilator("Saima Rashid", "Rashid Ahmed", "35202-5551122-1", "03215551122", 601, "Lahore", 500.0f, 4, 8500.0, 0.0, "Senior Invigilator", "Dr. Hamza Malik", 3001234567L));
        cbInvigilatorSelection.getItems().add(new Invigilator("Usman Ali", "Ali Asghar", "61101-4445566-4", "03004445566", 603, "Islamabad", 600.0f, 5, 7500.0, 0.0, "Invigilator", "Prof. Tariq Mahmood", 3127778899L));

        cbTestSelection.getItems().clear();
        cbTestSelection.getItems().add(new Test(101, "NAT-I Aptitude Test", 90, 850.0, 60.0f));
        cbTestSelection.getItems().add(new Test(102, "GAT General Test", 100, 1350.0, 50.0f));
        cbTestSelection.getItems().add(new Test(103, "TOEIC English Test", 990, 22000.0, 45.0f));
    }
}
