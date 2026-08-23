package oopProject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * CandidateLoginView constructs the authentic NTS Candidate Portal login and entry interface.
 * Adheres strictly to Phase 2 UI/UX constraints:
 * - Crisp White Background (#FFFFFF)
 * - Centered VBox Form layout with maximum 420px width
 * - Large NTS Action Orange (#F28221) header reading "Candidate Portal"
 * - Thin light grey divider with "PLEASE ENTER YOUR CNIC" embedded
 * - Inputs styled with '.nts-form-label' and '.nts-input-field'
 * - Full-width primary button using '.nts-primary-button' with arrow symbol (→)
 * - Event handler stubs for Login and Sign up actions.
 */
public class CandidateLoginView extends StackPane {

    private final TextField txtLoginId;
    private final PasswordField txtPassword;
    private final Button btnLogin;
    private final Hyperlink linkSignUp;
    private final Label lblMessage;

    // Optional callbacks for navigation / controller integration
    private BiConsumer<String, String> onLoginHandler;
    private Runnable onSignUpHandler;

    public CandidateLoginView() {
        // Set Crisp White Background (#FFFFFF)
        this.setStyle("-fx-background-color: #FFFFFF;");
        this.setPadding(new Insets(40));

        // Outer Centered Wrapper
        VBox cardContainer = new VBox(24);
        cardContainer.setAlignment(Pos.CENTER);
        cardContainer.setMaxWidth(420);
        cardContainer.setPadding(new Insets(36, 32, 36, 32));
        cardContainer.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E2E8F0; -fx-border-width: 1px; -fx-border-radius: 4px; -fx-background-radius: 4px;");

        // 1. Header: Large text reading "Candidate Portal" in NTS Action Orange
        VBox headerBox = new VBox(6);
        headerBox.setAlignment(Pos.CENTER);

        Label subHeader = new Label("NATIONAL TESTING SERVICE PAKISTAN");
        subHeader.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #2A4D7C; -fx-letter-spacing: 1px;");

        Label titleHeader = new Label("Candidate Portal");
        titleHeader.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #F28221;");

        headerBox.getChildren().addAll(subHeader, titleHeader);

        // 2. Embedded Divider: Thin light grey horizontal line with text embedded in center
        HBox dividerBox = createDivider("PLEASE ENTER YOUR CNIC");

        // 3. Form Input Fields
        VBox formBox = new VBox(16);
        formBox.setAlignment(Pos.CENTER_LEFT);

        // LOGIN ID Field
        VBox idBox = new VBox(6);
        Label lblLoginId = new Label("LOGIN ID");
        lblLoginId.getStyleClass().add("nts-form-label");

        txtLoginId = new TextField();
        txtLoginId.setPromptText("Enter CNIC e.g. 35202-1234567-1");
        txtLoginId.getStyleClass().add("nts-input-field");
        txtLoginId.setMaxWidth(Double.MAX_VALUE);
        idBox.getChildren().addAll(lblLoginId, txtLoginId);

        // PASSWORD Field
        VBox passBox = new VBox(6);
        Label lblPassword = new Label("PASSWORD");
        lblPassword.getStyleClass().add("nts-form-label");

        txtPassword = new PasswordField();
        txtPassword.setPromptText("Enter your portal password");
        txtPassword.getStyleClass().add("nts-input-field");
        txtPassword.setMaxWidth(Double.MAX_VALUE);
        passBox.getChildren().addAll(lblPassword, txtPassword);

        formBox.getChildren().addAll(idBox, passBox);

        // 4. Action: Full-width Login button using '.nts-primary-button' with right arrow (→)
        btnLogin = new Button("LOGIN  →");
        btnLogin.getStyleClass().add("nts-primary-button");
        btnLogin.setMaxWidth(Double.MAX_VALUE);
        btnLogin.setOnAction(e -> handleLogin());

        // Feedback Message Label
        lblMessage = new Label();
        lblMessage.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        lblMessage.setVisible(false);

        // 5. Footer: "Sign up" hyperlink and options
        HBox signUpBox = new HBox(6);
        signUpBox.setAlignment(Pos.CENTER);

        Label lblNoAccount = new Label("Don't have a candidate account?");
        lblNoAccount.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B;");

        linkSignUp = new Hyperlink("Sign up");
        linkSignUp.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #F28221; -fx-border-color: transparent;");
        linkSignUp.setOnAction(e -> handleSignUp());

        signUpBox.getChildren().addAll(lblNoAccount, linkSignUp);

        // Assemble Form
        cardContainer.getChildren().addAll(
                headerBox,
                dividerBox,
                formBox,
                btnLogin,
                lblMessage,
                signUpBox
        );

        this.getChildren().add(cardContainer);
        StackPane.setAlignment(cardContainer, Pos.CENTER);
    }

    /**
     * Constructs a thin light grey line divider with centered embedded text.
     */
    private HBox createDivider(String text) {
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER);

        Region leftLine = new Region();
        leftLine.setStyle("-fx-background-color: #E2E8F0; -fx-min-height: 1px; -fx-max-height: 1px;");
        HBox.setHgrow(leftLine, Priority.ALWAYS);

        Label centerText = new Label(text);
        centerText.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: #94A3B8; -fx-letter-spacing: 0.5px;");

        Region rightLine = new Region();
        rightLine.setStyle("-fx-background-color: #E2E8F0; -fx-min-height: 1px; -fx-max-height: 1px;");
        HBox.setHgrow(rightLine, Priority.ALWAYS);

        container.getChildren().addAll(leftLine, centerText, rightLine);
        return container;
    }

    /* ==========================================================================
       EVENT HANDLER STUBS & CONTROLLER CALLBACK HOOKS
       ========================================================================== */

    /**
     * Login Event Handler Stub.
     */
    private void handleLogin() {
        String loginId = txtLoginId.getText() != null ? txtLoginId.getText().trim() : "";
        String password = txtPassword.getText() != null ? txtPassword.getText().trim() : "";

        if (loginId.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Please enter both Login ID and Password.");
            lblMessage.setStyle("-fx-text-fill: #ED6B6B; -fx-font-weight: bold;");
            lblMessage.setVisible(true);
            return;
        }

        lblMessage.setText("Authenticating candidate...");
        lblMessage.setStyle("-fx-text-fill: #34B878; -fx-font-weight: bold;");
        lblMessage.setVisible(true);

        // Invoke callback stub if registered
        if (onLoginHandler != null) {
            onLoginHandler.accept(loginId, password);
        }
    }

    /**
     * Sign Up Event Handler Stub.
     */
    private void handleSignUp() {
        lblMessage.setText("Redirecting to Candidate Registration...");
        lblMessage.setStyle("-fx-text-fill: #2A4D7C; -fx-font-weight: bold;");
        lblMessage.setVisible(true);

        // Invoke callback stub if registered
        if (onSignUpHandler != null) {
            onSignUpHandler.run();
        }
    }

    // Handlers Registration API for MainController
    public void setOnLoginHandler(BiConsumer<String, String> handler) {
        this.onLoginHandler = handler;
    }

    public void setOnSignUpHandler(Runnable handler) {
        this.onSignUpHandler = handler;
    }

    public String getLoginId() {
        return txtLoginId.getText();
    }

    public String getPassword() {
        return txtPassword.getText();
    }
}
