package oopProject;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;

/**
 * MainLayout constructs the JavaFX UI Shell for the NTS Management System.
 * Displays the official NTS logo prominently across the application header, sidebar, and window taskbar.
 * Enforces strict authentication access control:
 * - All feature views remain locked until successful Admin login.
 * - Sidebar navigation items are disabled when unauthenticated.
 * - Includes a dedicated Logout handler and dynamic authentication state updates.
 */
public class MainLayout {

    private final BorderPane rootPane;
    private final StackPane contentArea;
    private final MainController controller;

    private Label pageTitleLabel;
    private Label pageSubtitleLabel;
    private Label lblUserSessionName;
    private Label lblUserSessionRole;
    private Button btnLogoutHeader;

    private Button btnAdminLogin;
    private Button btnAdminDashboard;
    private Button btnCandidates;
    private Button btnStaff;
    private Button btnTestMgmt;
    private Button btnTestCentres;

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

        // 6. Auth State Change Listener
        controller.setAuthStateChangeListener(this::updateAuthStateUI);

        // 7. Register Views & Set Default Route
        registerViews();

        // 8. Initial Lockout Setup
        updateAuthStateUI();
        controller.navigateTo("admin_login");
    }

    public BorderPane getRootPane() {
        return rootPane;
    }

    public MainController getController() {
        return controller;
    }

    private Node createTopBar() {
        HBox topBar = new HBox(14);
        topBar.getStyleClass().add("top-bar");
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Header Brand Emblem Logo
        try {
            URL logoUrl = getClass().getResource("logo.png");
            if (logoUrl == null) {
                logoUrl = getClass().getResource("/logo.png");
            }
            if (logoUrl != null) {
                ImageView logoView = new ImageView(new Image(logoUrl.toExternalForm()));
                logoView.setFitHeight(32);
                logoView.setPreserveRatio(true);
                topBar.getChildren().add(logoView);
            }
        } catch (Exception ignored) {}

        VBox titleBox = new VBox(2);
        pageTitleLabel = new Label("Dashboard");
        pageTitleLabel.getStyleClass().add("page-title");

        pageSubtitleLabel = new Label("System Overview & Operations");
        pageSubtitleLabel.getStyleClass().add("page-subtitle");

        titleBox.getChildren().addAll(pageTitleLabel, pageSubtitleLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        btnLogoutHeader = new Button("Sign Out / Logout");
        btnLogoutHeader.getStyleClass().add("btn-secondary");
        btnLogoutHeader.setStyle("-fx-text-fill: #DC2626; -fx-border-color: #FCA5A5; -fx-font-weight: bold;");
        btnLogoutHeader.setOnAction(e -> controller.logout());

        HBox actionBox = new HBox(10, btnLogoutHeader);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        topBar.getChildren().addAll(titleBox, spacer, actionBox);
        return topBar;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");

        VBox brandBox = new VBox(6);
        brandBox.getStyleClass().add("sidebar-header");

        // Top Sidebar Official Brand Logo
        try {
            URL logoUrl = getClass().getResource("logo.png");
            if (logoUrl == null) {
                logoUrl = getClass().getResource("/logo.png");
            }
            if (logoUrl != null) {
                ImageView logoView = new ImageView(new Image(logoUrl.toExternalForm()));
                logoView.setFitHeight(44);
                logoView.setPreserveRatio(true);
                brandBox.getChildren().add(logoView);
            }
        } catch (Exception ignored) {}

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

        btnAdminLogin = createNavButton("Admin Authentication");
        btnAdminDashboard = createNavButton("Admin Dashboard");
        btnCandidates = createNavButton("Candidate Portal");
        btnStaff = createNavButton("Staff Management");
        btnTestMgmt = createNavButton("Test Management");
        btnTestCentres = createNavButton("Test Centre Allocation");

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

        lblUserSessionName = new Label("○ Locked Session");
        lblUserSessionName.getStyleClass().add("user-name");

        lblUserSessionRole = new Label("Sign-In Required");
        lblUserSessionRole.getStyleClass().add("user-role");

        userProfileBox.getChildren().addAll(lblUserSessionName, lblUserSessionRole);

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

        Label statusLabel = new Label("● System Security Operational — Auth Lock Active");
        statusLabel.getStyleClass().add("footer-status-indicator");

        footer.getChildren().addAll(copyrightLabel, spacer, statusLabel);
        return footer;
    }

    private void updateAuthStateUI() {
        boolean isAuth = controller.isAuthenticated();

        btnAdminDashboard.setDisable(!isAuth);
        btnCandidates.setDisable(!isAuth);
        btnStaff.setDisable(!isAuth);
        btnTestMgmt.setDisable(!isAuth);
        btnTestCentres.setDisable(!isAuth);

        btnLogoutHeader.setVisible(isAuth);
        btnLogoutHeader.setManaged(isAuth);

        if (isAuth) {
            lblUserSessionName.setText("● Admin: " + controller.getAuthenticatedUser());
            lblUserSessionRole.setText("Executive Access Granted");
        } else {
            lblUserSessionName.setText("○ Session Locked");
            lblUserSessionRole.setText("Authentication Required");
        }
    }

    private void registerViews() {
        AdminLoginView adminLoginView = new AdminLoginView();
        AdminDashboard adminDashboard = new AdminDashboard();
        CandidatePortalView candidatePortalView = new CandidatePortalView();
        StaffManagementView staffManagementView = new StaffManagementView();
        TestManagementView testManagementView = new TestManagementView();
        TestCentreAllocationView testCentreAllocationView = new TestCentreAllocationView();

        adminLoginView.setOnLoginSuccessListener(username -> {
            controller.setAuthenticated(true, username);
            DialogHelper.showSuccess("NTS Authentication", "Access Granted", "Welcome back, " + username + "! Unlocking system features.");
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
