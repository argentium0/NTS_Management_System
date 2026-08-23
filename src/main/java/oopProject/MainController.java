package oopProject;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller responsible for view routing, sidebar state management,
 * and header title updates in the NTS JavaFX Desktop Application.
 */
public class MainController {

    private final StackPane contentArea;
    private final Label pageTitleLabel;
    private final Label pageSubtitleLabel;

    private final Map<String, Button> navButtons = new HashMap<>();
    private final Map<String, Node> viewRegistry = new HashMap<>();

    private Button activeNavButton;

    public MainController(StackPane contentArea, Label pageTitleLabel, Label pageSubtitleLabel) {
        this.contentArea = contentArea;
        this.pageTitleLabel = pageTitleLabel;
        this.pageSubtitleLabel = pageSubtitleLabel;
    }

    /**
     * Registers a navigation button associated with a route key.
     */
    public void registerNavButton(String routeKey, Button button) {
        navButtons.put(routeKey, button);
        button.setOnAction(e -> navigateTo(routeKey));
    }

    /**
     * Registers a view node associated with a route key.
     */
    public void registerView(String routeKey, Node viewNode) {
        viewRegistry.put(routeKey, viewNode);
    }

    /**
     * Navigates to the view identified by routeKey, updating UI state.
     */
    public void navigateTo(String routeKey) {
        Node targetView = viewRegistry.get(routeKey);
        if (targetView == null) {
            System.err.println("[MainController] View not registered for route: " + routeKey);
            return;
        }

        // Inject target view into content area
        contentArea.getChildren().setAll(targetView);

        // Update navigation button active state
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

        // Update header breadcrumb/titles based on route
        updateHeaderTitles(routeKey);
    }

    private void updateHeaderTitles(String routeKey) {
        switch (routeKey.toLowerCase()) {
            case "dashboard" -> setHeaderTitle("Dashboard", "System Overview & Analytics");
            case "login" -> setHeaderTitle("Candidate Portal", "Authentic Candidate Login & Entry System");
            case "admin_matrix" -> setHeaderTitle("Admin Allocation Matrix", "Staff & Test Centre Deployments Overview");
            case "candidates" -> setHeaderTitle("Candidate Management", "Manage Student Registrations & Applications");
            case "staff" -> setHeaderTitle("Staff & Duty Management", "Manage Invigilators, Superintendents & Stipends");
            case "test_centres" -> setHeaderTitle("Test Centre Operations", "Venue Allocation & Team Assignments");
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
