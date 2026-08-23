package oopProject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * MainLayout constructs the JavaFX UI Shell for the NTS Management System.
 * Adheres strictly to the NTS Design System:
 * - Primary Brand: NTS Action Orange (#F28221), NTS Label Navy (#2A4D7C)
 * - Secondary: Charcoal Slate (#2C3238), Coral Red (#ED6B6B), Emerald Green (#34B878)
 * - Layout: Pure JavaFX BorderPane with White Top Header, Navy Sidebar, Central Content Pane, and Charcoal Footer.
 */
public class MainLayout {

    private final BorderPane rootPane;
    private final StackPane contentArea;
    private final MainController controller;

    private Label pageTitleLabel;
    private Label pageSubtitleLabel;

    public MainLayout() {
        rootPane = new BorderPane();
        contentArea = new StackPane();
        contentArea.getStyleClass().add("content-area");

        // 1. Construct Top Header Bar (White background)
        Node topBar = createTopBar();
        rootPane.setTop(topBar);

        // 2. Controller Initialization
        controller = new MainController(contentArea, pageTitleLabel, pageSubtitleLabel);

        // 3. Construct Navigation Sidebar (Navy background)
        VBox sidebar = createSidebar();
        rootPane.setLeft(sidebar);

        // 4. Set Central Content Pane
        rootPane.setCenter(contentArea);

        // 5. Construct Bottom Footer Area (Charcoal background)
        Node footerArea = createFooterArea();
        rootPane.setBottom(footerArea);

        // 6. Register Views & Set Default Route
        registerViews();
        controller.navigateTo("dashboard");
    }

    public BorderPane getRootPane() {
        return rootPane;
    }

    public MainController getController() {
        return controller;
    }

    /* ==========================================================================
       1. TOP HEADER BAR CREATION (White Background)
       ========================================================================== */
    private Node createTopBar() {
        HBox topBar = new HBox();
        topBar.getStyleClass().add("top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(2);
        pageTitleLabel = new Label("Dashboard");
        pageTitleLabel.getStyleClass().add("page-title");

        pageSubtitleLabel = new Label("System Overview & Operations");
        pageSubtitleLabel.getStyleClass().add("page-subtitle");

        titleBox.getChildren().addAll(pageTitleLabel, pageSubtitleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Quick Action Buttons using strict NTS Design System classes
        Button btnNewCandidate = new Button("+ Add Candidate");
        btnNewCandidate.getStyleClass().add("nts-primary-button");

        Button btnRefresh = new Button("Refresh");
        btnRefresh.getStyleClass().add("btn-secondary");

        HBox actionBox = new HBox(10, btnRefresh, btnNewCandidate);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        topBar.getChildren().addAll(titleBox, spacer, actionBox);
        return topBar;
    }

    /* ==========================================================================
       2. SIDEBAR NAVIGATION CREATION (Navy #2A4D7C Background)
       ========================================================================== */
    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");

        // Brand Header
        VBox brandBox = new VBox();
        brandBox.getStyleClass().add("sidebar-header");

        Label brandLabel = new Label("NTS PORTAL");
        brandLabel.getStyleClass().add("sidebar-brand");

        Label subbrandLabel = new Label("Testing Management System");
        subbrandLabel.getStyleClass().add("sidebar-subbrand");

        brandBox.getChildren().addAll(brandLabel, subbrandLabel);

        // Navigation Items Container
        VBox navContainer = new VBox();
        navContainer.getStyleClass().add("nav-container");

        Label sectionLabel = new Label("MAIN NAVIGATION");
        sectionLabel.getStyleClass().add("nav-section-label");
        navContainer.getChildren().add(sectionLabel);

        // Navigation Buttons
        Button btnDashboard = createNavButton("Admin Matrix");
        Button btnLogin = createNavButton("Candidate Login");
        Button btnCandidates = createNavButton("Candidates");
        Button btnStaff = createNavButton("Staff & Duty");
        Button btnTestCentres = createNavButton("Test Centres");

        navContainer.getChildren().addAll(btnDashboard, btnLogin, btnCandidates, btnStaff, btnTestCentres);

        // Register Buttons with Controller
        controller.registerNavButton("dashboard", btnDashboard);
        controller.registerNavButton("login", btnLogin);
        controller.registerNavButton("candidates", btnCandidates);
        controller.registerNavButton("staff", btnStaff);
        controller.registerNavButton("test_centres", btnTestCentres);

        // Spacer to push footer to bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // Sidebar Footer User Profile Area
        VBox userProfileBox = new VBox(2);
        userProfileBox.getStyleClass().add("sidebar-footer");

        Label userName = new Label("Admin Administrator");
        userName.getStyleClass().add("user-name");

        Label userRole = new Label("NTS Operations Center");
        userRole.getStyleClass().add("user-role");

        userProfileBox.getChildren().addAll(userName, userRole);

        sidebar.getChildren().addAll(brandBox, navContainer, spacer, userProfileBox);
        return sidebar;
    }

    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("nav-button");
        return btn;
    }

    /* ==========================================================================
       3. BOTTOM FOOTER AREA CREATION (Charcoal #2C3238 Background)
       ========================================================================== */
    private Node createFooterArea() {
        HBox footer = new HBox();
        footer.getStyleClass().add("footer-bar");
        footer.setAlignment(Pos.CENTER_LEFT);

        Label copyrightLabel = new Label("© 2026 National Testing Service (NTS). All rights reserved.");
        copyrightLabel.getStyleClass().add("footer-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label statusLabel = new Label("● System Operational — Engine v1.0.0");
        statusLabel.getStyleClass().add("footer-status-indicator");

        footer.getChildren().addAll(copyrightLabel, spacer, statusLabel);
        return footer;
    }

    /* ==========================================================================
       4. VIEW REGISTRATION & DASHBOARD GENERATION
       ========================================================================== */
    private void registerViews() {
        AdminDashboardView adminDashboard = new AdminDashboardView();
        CandidateLoginView candidateLogin = new CandidateLoginView();

        candidateLogin.setOnLoginHandler((cnic, password) -> {
            DialogHelper.showSuccess("NTS Authentication", "Login Successful", "Candidate logged in with CNIC: " + cnic);
            controller.navigateTo("candidates");
        });

        candidateLogin.setOnSignUpHandler(() -> {
            DialogHelper.showInformation("NTS Portal", "Registration Redirect", "Navigating to Candidate Application & Registration Portal.");
            controller.navigateTo("candidates");
        });

        controller.registerView("dashboard", adminDashboard);
        controller.registerView("admin_matrix", adminDashboard);
        controller.registerView("login", candidateLogin);
        controller.registerView("candidates", createCandidatesView());
        controller.registerView("staff", createStaffView());
        controller.registerView("test_centres", createTestCentresView());
    }

    private Node createDashboardView() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(10));

        // Metric Cards Grid
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        grid.add(createMetricCard("Total Candidates", "1,248", "+12% this month"), 0, 0);
        grid.add(createMetricCard("Active Test Centres", "42", "Nationwide operational"), 1, 0);
        grid.add(createMetricCard("Assigned Staff", "310", "Invigilators & Superintendents"), 2, 0);
        grid.add(createMetricCard("Scheduled Exams", "18", "NAT, GAT & TOEIC"), 3, 0);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(25);
            grid.getColumnConstraints().add(col);
        }

        // Recent Activity Table Card
        VBox tableCard = new VBox(12);
        tableCard.getStyleClass().add("flat-card");

        Label cardHeader = new Label("Recent Registered Candidates");
        cardHeader.getStyleClass().add("card-title");

        TableView<String[]> table = createMinimalTable(
                new String[]{"Form #", "Candidate Name", "CNIC", "City", "Test Applied", "Status"},
                new String[][]{
                        {"1001", "Ali Ahmed", "35202-1234567-1", "Lahore", "NAT-I", "Confirmed"},
                        {"1002", "Fatima Khan", "61101-9876543-2", "Islamabad", "GAT General", "Pending"},
                        {"1003", "Usman Raza", "42101-5554443-3", "Karachi", "TOEIC", "Confirmed"},
                        {"1004", "Zainab Bibi", "31202-7778889-4", "Multan", "NAT-II", "Confirmed"}
                }
        );

        tableCard.getChildren().addAll(cardHeader, table);
        layout.getChildren().addAll(grid, tableCard);

        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return scrollPane;
    }

    private Node createCandidatesView() {
        return new CandidatePortalView();
    }

    private Node createStaffView() {
        return new StaffView();
    }

    private Node createTestCentresView() {
        return new TestCentreView();
    }

    private VBox createMetricCard(String title, String value, String subtitle) {
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

    private TableView<String[]> createMinimalTable(String[] headers, String[][] data) {
        TableView<String[]> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        for (int i = 0; i < headers.length; i++) {
            final int colIdx = i;
            TableColumn<String[], String> col = new TableColumn<>(headers[i]);
            col.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[colIdx]));
            table.getColumns().add(col);
        }

        java.util.Collections.addAll(table.getItems(), data);
        table.setPrefHeight(240);
        return table;
    }
}
