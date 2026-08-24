package oopProject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;

/**
 * AdminLoginView provides a flat, minimalist administrator login interface.
 * Adheres strictly to NTS Brand Aesthetics (Navy #2A4D7C and Action Orange #F28221).
 * Features the official NTS brand logo.
 */
public class AdminLoginView extends VBox {

    private final TextField txtUsername = new TextField();
    private final PasswordField txtPassword = new PasswordField();
    private final Label lblErrorMessage = new Label();

    private OnLoginSuccessListener loginSuccessListener;

    @FunctionalInterface
    public interface OnLoginSuccessListener {
        void onLoginSuccess(String username);
    }

    public AdminLoginView() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: #F8FAFC;");

        // Central Login Card Container
        VBox card = new VBox(20);
        card.setMaxWidth(420);
        card.getStyleClass().add("flat-card");
        card.setPadding(new Insets(32));

        // Header Title & Subtitle with Official NTS Logo
        VBox titleBox = new VBox(8);
        titleBox.setAlignment(Pos.CENTER);

        try {
            URL logoUrl = getClass().getResource("logo.png");
            if (logoUrl == null) {
                logoUrl = getClass().getResource("/logo.png");
            }
            if (logoUrl != null) {
                ImageView logoView = new ImageView(new Image(logoUrl.toExternalForm()));
                logoView.setFitHeight(54);
                logoView.setPreserveRatio(true);
                titleBox.getChildren().add(logoView);
            }
        } catch (Exception ignored) {}

        Label lblBrand = new Label("NTS ADMIN PORTAL");
        lblBrand.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2A4D7C;");

        Label lblSub = new Label("Secure Administrative Access & Operations");
        lblSub.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B;");

        titleBox.getChildren().addAll(lblBrand, lblSub);

        // Form Fields
        VBox formGrid = new VBox(14);

        VBox usernameBox = createInputGroup("Admin Username", txtUsername, "Enter admin username...");
        VBox passwordBox = createInputGroup("Password", txtPassword, "Enter admin password...");

        lblErrorMessage.setStyle("-fx-text-fill: #ED6B6B; -fx-font-size: 12px; -fx-font-weight: bold;");
        lblErrorMessage.setVisible(false);

        formGrid.getChildren().addAll(usernameBox, passwordBox, lblErrorMessage);

        // Submit Button
        Button btnLogin = new Button("Sign In to Dashboard");
        btnLogin.getStyleClass().add("nts-primary-button");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setOnAction(e -> handleLogin());

        // Default Credentials Info Box
        VBox infoBox = new VBox(4);
        infoBox.setStyle("-fx-background-color: #F1F5F9; -fx-padding: 10; -fx-border-color: #CBD5E1; -fx-border-width: 1px;");
        Label lblInfoTitle = new Label("Default Credentials:");
        lblInfoTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 11px; -fx-text-fill: #2A4D7C;");
        Label lblCreds = new Label("Username: admin  |  Password: admin123");
        lblCreds.setStyle("-fx-font-size: 11px; -fx-text-fill: #475569;");
        infoBox.getChildren().addAll(lblInfoTitle, lblCreds);

        card.getChildren().addAll(titleBox, formGrid, btnLogin, infoBox);
        getChildren().add(card);
    }

    public void setOnLoginSuccessListener(OnLoginSuccessListener listener) {
        this.loginSuccessListener = listener;
    }

    private VBox createInputGroup(String labelText, TextField textField, String prompt) {
        VBox group = new VBox(4);
        Label label = new Label(labelText);
        label.getStyleClass().add("nts-form-label");
        textField.getStyleClass().add("nts-input-field");
        textField.setPromptText(prompt);
        group.getChildren().addAll(label, textField);
        return group;
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        // Validate credentials (default: admin / admin123 or active password)
        if (AdminDashboard.validateAdminCredentials(username, password)) {
            lblErrorMessage.setVisible(false);
            if (loginSuccessListener != null) {
                loginSuccessListener.onLoginSuccess(username);
            }
        } else {
            showError("Invalid admin username or password.");
        }
    }

    private void showError(String msg) {
        lblErrorMessage.setText(msg);
        lblErrorMessage.setVisible(true);
    }
}
