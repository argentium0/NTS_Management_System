package oopProject;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * MainLayout constructs the JavaFX UI Shell for the NTS Management System.
 * Connects the Admin Authentication, Candidate Portal, Staff Management, Test Management,
 * and Test Centre Allocation views mapped 1:1 to UML class architecture.
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

        // 1. Construct Top Header Bar
        Node topBar = createTopBar();
        rootPane.setTop(topBar);

        // 2. Controller Initialization
        controller = new MainController(contentArea, pageTitleLabel, pageSubtitleLabel);

        // 3. Construct Navigation Sidebar
        VBox sidebar = createSidebar();
        rootPane.setLeft(sidebar);

        // 4. Set Central Content Pane
        rootPane.setCenter(contentArea);

        // 5. Construct Bottom Footer Area
        Node footerArea = createFooterArea();
        rootPane.setBottom(footerArea);

        // 6. Register Views & Set Default Route
        registerViews();
        controller.navigateTo("admin_login");
    }

    public BorderPane getRootPane() {
        return rootPane;
    }

    public MainController getController() {
        return controller;
    }

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

        Button btnRefresh = new Button("Refresh View");
        btnRefresh.getStyleClass().add("btn-secondary");

        HBox actionBox = new HBox(10, btnRefresh);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        topBar.getChildren().addAll(titleBox, spacer, actionBox);
        return topBar;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");

        VBox brandBox = new VBox();
        brandBox.getStyleClass().add("sidebar-header");

        Label brandLabel = new Label("NTS PORTAL");
        brandLabel.getStyleClass().add("sidebar-brand");

        Label subbrandLabel = new Label("Testing Management System");
        subbrandLabel.getStyleClass().add("sidebar-subbrand");

        brandBox.getChildren().addAll(brandLabel, subbrandLabel);

        VBox navContainer = new VBox();
        navContainer.getStyleClass().add("nav-container");

        Label sectionLabel = new Label("UML ARCHITECTURE VIEWS");
        sectionLabel.getStyleClass().add("nav-section-label");
        navContainer.getChildren().add(sectionLabel);

        Button btnAdminLogin = createNavButton("Admin Authentication");
        Button btnAdminDashboard = createNavButton("Admin Dashboard");
        Button btnCandidates = createNavButton("Candidate Portal");
        Button btnStaff = createNavButton("Staff Management");
        Button btnTestMgmt = createNavButton("Test Management");
        Button btnTestCentres = createNavButton("Test Centre Allocation");

        navContainer.getChildren().addAll(
                btnAdminLogin, btnAdminDashboard, btnCandidates, btnStaff, btnTestMgmt, btnTestCentres
        );

        controller.registerNavButton("admin_login", btnAdminLogin);
        controller.registerNavButton("admin_dashboard", btnAdminDashboard);
        controller.registerNavButton("candidates", btnCandidates);
        controller.registerNavButton("staff", btnStaff);
        controller.registerNavButton("test_mgmt", btnTestMgmt);
        controller.registerNavButton("test_centres", btnTestCentres);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox userProfileBox = new VBox(2);
        userProfileBox.getStyleClass().add("sidebar-footer");

        Label userName = new Label("Active Session");
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

    private Node createFooterArea() {
        HBox footer = new HBox();
        footer.getStyleClass().add("footer-bar");
        footer.setAlignment(Pos.CENTER_LEFT);

        Label copyrightLabel = new Label("© 2026 National Testing Service (NTS). All rights reserved.");
        copyrightLabel.getStyleClass().add("footer-text");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label statusLabel = new Label("● System Operational — UML Architecture Mirror v1.0");
        statusLabel.getStyleClass().add("footer-status-indicator");

        footer.getChildren().addAll(copyrightLabel, spacer, statusLabel);
        return footer;
    }

    private void registerViews() {
        AdminLoginView adminLoginView = new AdminLoginView();
        AdminDashboard adminDashboard = new AdminDashboard();
        CandidatePortalView candidatePortalView = new CandidatePortalView();
        StaffManagementView staffManagementView = new StaffManagementView();
        TestManagementView testManagementView = new TestManagementView();
        TestCentreAllocationView testCentreAllocationView = new TestCentreAllocationView();

        adminLoginView.setOnLoginSuccessListener(username -> {
            DialogHelper.showSuccess("NTS Authentication", "Login Successful", "Welcome back, " + username + "! Navigating to Executive Dashboard.");
            controller.navigateTo("admin_dashboard");
        });

        controller.registerView("admin_login", adminLoginView);
        controller.registerView("admin_dashboard", adminDashboard);
        controller.registerView("candidates", candidatePortalView);
        controller.registerView("staff", staffManagementView);
        controller.registerView("test_mgmt", testManagementView);
        controller.registerView("test_centres", testCentreAllocationView);
    }
}
