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

    public void registerNavButton(String routeKey, Button button) {
        navButtons.put(routeKey, button);
        button.setOnAction(e -> navigateTo(routeKey));
    }

    public void registerView(String routeKey, Node viewNode) {
        viewRegistry.put(routeKey, viewNode);
    }

    public void navigateTo(String routeKey) {
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
            case "admin_login" -> setHeaderTitle("Admin Authentication", "Administrator Sign-In & Verification");
            case "admin_dashboard" -> setHeaderTitle("Admin Dashboard", "System Overview & Settings");
            case "candidates" -> setHeaderTitle("Candidate Portal", "Mapped to Person -> Candidate Architecture");
            case "staff" -> setHeaderTitle("Staff Management", "Mapped to Person -> Employee -> Invigilator/Superintendent");
            case "test_mgmt" -> setHeaderTitle("Test Management", "Mapped to Test Entity Architecture");
            case "test_centres" -> setHeaderTitle("Test Centre Allocation", "Mapped to TestCentre Entity & Team Management");
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
