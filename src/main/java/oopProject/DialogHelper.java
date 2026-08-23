package oopProject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.net.URL;

/**
 * DialogHelper constructs custom, un-decorated JavaFX modal dialogs overriding default OS alerts.
 * Features strict Phase 4 design requirements:
 * - Sharp, crisp white background (#FFFFFF)
 * - Flat NTS Navy top border (#2A4D7C)
 * - Action Orange dismissal button (.nts-primary-button / #F28221)
 */
public class DialogHelper {

    public enum DialogType {
        INFO, SUCCESS, ERROR, CONFIRM
    }

    public static void showInformation(String title, String header, String message) {
        showCustomModal(DialogType.INFO, title, header, message, null);
    }

    public static void showSuccess(String title, String header, String message) {
        showCustomModal(DialogType.SUCCESS, title, header, message, null);
    }

    public static void showError(String title, String header, String message) {
        showCustomModal(DialogType.ERROR, title, header, message, null);
    }

    public static void showConfirmation(String title, String header, String message, Runnable onConfirm) {
        showCustomModal(DialogType.CONFIRM, title, header, message, onConfirm);
    }

    private static void showCustomModal(DialogType type, String title, String header, String message, Runnable onConfirm) {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.initStyle(StageStyle.UTILITY);
        modalStage.setTitle(title);
        modalStage.setResizable(false);

        // Root Container
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-width: 1px;");

        // 1. Flat NTS Navy Top Border Bar (#2A4D7C)
        Region navyTopBorder = new Region();
        navyTopBorder.setStyle("-fx-background-color: #2A4D7C; -fx-min-height: 6px; -fx-max-height: 6px;");

        // 2. Dialog Main Body Container (Sharp, White Background)
        VBox bodyBox = new VBox(16);
        bodyBox.setPadding(new Insets(24, 24, 20, 24));
        bodyBox.setStyle("-fx-background-color: #FFFFFF;");

        // Header Title Label
        Label lblHeader = new Label(header);
        lblHeader.setWrapText(true);

        switch (type) {
            case ERROR -> lblHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ED6B6B;");
            case SUCCESS -> lblHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #34B878;");
            case INFO -> lblHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2A4D7C;");
            default -> lblHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2C3238;");
        }

        // Message Body Label
        Label lblMessage = new Label(message);
        lblMessage.setWrapText(true);
        lblMessage.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748B; -fx-line-spacing: 2px;");
        lblMessage.setMaxWidth(380);

        // 3. Action Buttons (Orange dismissal button using .nts-primary-button)
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(8, 0, 0, 0));

        if (type == DialogType.CONFIRM) {
            Button btnCancel = new Button("Cancel");
            btnCancel.getStyleClass().add("btn-secondary");
            btnCancel.setOnAction(e -> modalStage.close());

            Button btnConfirm = new Button("Confirm");
            btnConfirm.getStyleClass().add("nts-primary-button");
            btnConfirm.setOnAction(e -> {
                modalStage.close();
                if (onConfirm != null) {
                    onConfirm.run();
                }
            });

            buttonBox.getChildren().addAll(btnCancel, btnConfirm);
        } else {
            // Action Orange dismissal button (.nts-primary-button)
            Button btnDismiss = new Button("Dismiss");
            btnDismiss.getStyleClass().add("nts-primary-button");
            btnDismiss.setMinWidth(100);
            btnDismiss.setOnAction(e -> modalStage.close());
            buttonBox.getChildren().add(btnDismiss);
        }

        bodyBox.getChildren().addAll(lblHeader, lblMessage, buttonBox);
        root.getChildren().addAll(navyTopBorder, bodyBox);

        Scene scene = new Scene(root, 420, 200);

        // Attach global stylesheet for design system classes
        URL cssResource = DialogHelper.class.getResource("styles.css");
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        }

        modalStage.setScene(scene);
        modalStage.showAndWait();
    }
}
