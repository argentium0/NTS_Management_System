package oopProject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

/**
 * NTSFooter is a reusable JavaFX component constructing the authentic 3-column NTS footer block.
 * Adheres strictly to Phase 3 UI/UX constraints:
 * - Background: Charcoal Slate (#2C3238)
 * - Layout: HBox containing three distinct VBox columns
 * - Column Headers (0px border radius, flat flush blocks):
 *   - Column 1 Header: Coral Red (#ED6B6B) reading "ABOUT US"
 *   - Column 2 Header: Coral Red (#ED6B6B) reading "QUICK LINKS"
 *   - Column 3 Header: Emerald Green (#34B878) reading "HEAD OFFICE LOCATION"
 */
public class NTSFooter extends HBox {

    public NTSFooter() {
        this.getStyleClass().add("nts-footer-bg");
        this.setStyle("-fx-background-color: #2C3238; -fx-padding: 24 28;");
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);

        // Column 1: ABOUT US (Coral Red Header)
        VBox col1 = createColumn(
                "ABOUT US",
                "#ED6B6B",
                createAboutUsContent()
        );

        // Column 2: QUICK LINKS (Coral Red Header)
        VBox col2 = createColumn(
                "QUICK LINKS",
                "#ED6B6B",
                createQuickLinksContent()
        );

        // Column 3: HEAD OFFICE LOCATION (Emerald Green Header)
        VBox col3 = createColumn(
                "HEAD OFFICE LOCATION",
                "#34B878",
                createLocationContent()
        );

        // Equal width expansion across all 3 columns
        HBox.setHgrow(col1, Priority.ALWAYS);
        HBox.setHgrow(col2, Priority.ALWAYS);
        HBox.setHgrow(col3, Priority.ALWAYS);

        this.getChildren().addAll(col1, col2, col3);
    }

    /**
     * Creates a flush footer column card with a 0px border-radius colored header block.
     */
    private VBox createColumn(String headerTitle, String headerBgColor, VBox bodyContent) {
        VBox card = new VBox(0);
        card.setStyle("-fx-background-color: #23282D; -fx-border-color: #384048; -fx-border-width: 1px; -fx-background-radius: 0px; -fx-border-radius: 0px;");

        // 0px border-radius, flat flush header block
        HBox header = new HBox();
        header.setPadding(new Insets(10, 16, 10, 16));
        header.setStyle("-fx-background-color: " + headerBgColor + "; -fx-background-radius: 0px; -fx-border-radius: 0px;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label lblHeader = new Label(headerTitle);
        lblHeader.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-font-size: 12px; -fx-letter-spacing: 0.5px;");

        header.getChildren().add(lblHeader);

        // Column Body
        bodyContent.setPadding(new Insets(16));
        card.getChildren().addAll(header, bodyContent);
        return card;
    }

    private VBox createAboutUsContent() {
        VBox box = new VBox(10);

        Label lblDesc = new Label(
                "National Testing Service (NTS) is Pakistan's premier testing and assessment organization. " +
                "Established to promote quality education and transparent candidate evaluations for admissions, " +
                "recruitment, and professional certifications nationwide."
        );
        lblDesc.setWrapText(true);
        lblDesc.setStyle("-fx-text-fill: #CBD5E1; -fx-font-size: 12px; -fx-line-spacing: 2px;");

        Label lblAccreditation = new Label("Accredited by Higher Education Commission (HEC)");
        lblAccreditation.setStyle("-fx-text-fill: #F28221; -fx-font-size: 11px; -fx-font-weight: bold;");

        box.getChildren().addAll(lblDesc, lblAccreditation);
        return box;
    }

    private VBox createQuickLinksContent() {
        VBox box = new VBox(6);

        Hyperlink link1 = createLink("● Candidate Registration Portal");
        Hyperlink link2 = createLink("● Test Schedule & Roll No. Slip");
        Hyperlink link3 = createLink("● Duty Allocation & Staff Management");
        Hyperlink link4 = createLink("● Test Centres Directory & Map");
        Hyperlink link5 = createLink("● Downloads, Answer Keys & Results");

        box.getChildren().addAll(link1, link2, link3, link4, link5);
        return box;
    }

    private Hyperlink createLink(String text) {
        Hyperlink link = new Hyperlink(text);
        link.setStyle("-fx-text-fill: #CBD5E1; -fx-font-size: 12px; -fx-border-color: transparent; -fx-padding: 2 0;");
        link.setOnMouseEntered(e -> link.setStyle("-fx-text-fill: #F28221; -fx-font-size: 12px; -fx-underline: true; -fx-border-color: transparent; -fx-padding: 2 0;"));
        link.setOnMouseExited(e -> link.setStyle("-fx-text-fill: #CBD5E1; -fx-font-size: 12px; -fx-border-color: transparent; -fx-padding: 2 0;"));
        return link;
    }

    private VBox createLocationContent() {
        VBox box = new VBox(8);

        Label lblAddress = new Label("NTS Head Office: Plot #96, Street 4, H-8/1, Islamabad, Pakistan");
        lblAddress.setWrapText(true);
        lblAddress.setStyle("-fx-text-fill: #CBD5E1; -fx-font-size: 12px;");

        Label lblPhone = new Label("UAN: +92-51-8444441");
        lblPhone.setStyle("-fx-text-fill: #34B878; -fx-font-size: 12px; -fx-font-weight: bold;");

        Label lblEmail = new Label("Email: query@nts.org.pk");
        lblEmail.setStyle("-fx-text-fill: #CBD5E1; -fx-font-size: 12px;");

        Label lblTiming = new Label("Timings: Mon - Fri: 8:30 AM - 4:30 PM");
        lblTiming.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 11px;");

        box.getChildren().addAll(lblAddress, lblPhone, lblEmail, lblTiming);
        return box;
    }
}
