package oopProject;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller responsible for view routing, sidebar state management,
 * authentication access control, and header title updates.
 */
public class MainController {

    private final StackPane contentArea;
    private final Label pageTitleLabel;
    private final Label pageSubtitleLabel;

    private final Map<String, Button> navButtons = new HashMap<>();
    private final Map<String, Node> viewRegistry = new HashMap<>();

    private Button activeNavButton;
    private boolean authenticated = false;
    private String authenticatedUser = null;
    private Runnable authStateChangeListener;

    public MainController(StackPane contentArea, Label pageTitleLabel, Label pageSubtitleLabel) {
        this.contentArea = contentArea;
        this.pageTitleLabel = pageTitleLabel;
        this.pageSubtitleLabel = pageSubtitleLabel;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthStateChangeListener(Runnable listener) {
        this.authStateChangeListener = listener;
    }

    public void setAuthenticated(boolean status, String username) {
        this.authenticated = status;
        this.authenticatedUser = status ? username : null;
        if (authStateChangeListener != null) {
            authStateChangeListener.run();
        }
    }

    public void logout() {
        setAuthenticated(false, null);
        navigateTo("admin_login");
    }

    public void registerNavButton(String routeKey, Button button) {
        navButtons.put(routeKey, button);
        button.setOnAction(e -> navigateTo(routeKey));
    }

    public void registerView(String routeKey, Node viewNode) {
        viewRegistry.put(routeKey, viewNode);
    }

    public void navigateTo(String routeKey) {
        // Enforce Authentication Access Control Gatekeeping
        if (!"admin_login".equalsIgnoreCase(routeKey) && !authenticated) {
            DialogHelper.showError("Access Denied", "Authentication Required", "Administrative authentication is required to access system features. Please log in first.");
            routeKey = "admin_login";
        }

        Node targetView = viewRegistry.get(routeKey);
        if (targetView == null) {
            System.err.println("[MainController] View not registered for route: " + routeKey);
            return;
        }

        contentArea.getChildren().setAll(targetView);

        Button targetButton = navButtons.get(routeKey);
        if (targetButton != null) {
            if (activeNavButton != null) {
                activeNavButton.getStyleClass().remove("active");
            }
            if (!targetButton.getStyleClass().contains("active")) {
                targetButton.getStyleClass().add("active");
            }
            activeNavButton = targetButton;
        }

        updateHeaderTitles(routeKey);
    }

    private void updateHeaderTitles(String routeKey) {
        switch (routeKey.toLowerCase()) {
            case "admin_login" -> setHeaderTitle("Admin Authentication", "Administrative Sign-In & Entry Gateway");
            case "admin_dashboard" -> setHeaderTitle("Admin Dashboard", "Executive Overview & Security Settings");
            case "candidates" -> setHeaderTitle("Candidate Portal", "Student Registration & Test Applications");
            case "staff" -> setHeaderTitle("Staff Management", "Invigilator & Superintendent Duty Allocation");
            case "test_mgmt" -> setHeaderTitle("Test Management", "NTS Exam Catalog & Configuration");
            case "test_centres" -> setHeaderTitle("Test Centre Allocation", "Venue Operations & Duty Team Assignments");
            default -> setHeaderTitle("NTS Portal", "National Testing Service Administration");
        }
    }

    public void setHeaderTitle(String title, String subtitle) {
        if (pageTitleLabel != null) {
            pageTitleLabel.setText(title);
        }
        if (pageSubtitleLabel != null) {
            pageSubtitleLabel.setText(subtitle);
        }
    }
}
