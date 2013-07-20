package net.sukharevd.gwt.client;

import com.github.gwtbootstrap.client.ui.AlertBlock;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;


public class ConverterView extends Composite {

    public static final int MAX_ALLOWED_ROUTES = 50;

    @UiTemplate("ConverterView.ui.xml")
    interface ConverterViewUiBinder extends UiBinder<FlowPanel, ConverterView> {}

    private static ConverterViewUiBinder uiBinder = GWT.create(ConverterViewUiBinder.class);

    @UiField TextArea srcTextArea;
    @UiField TextArea dstTextArea;
    @UiField AlertBlock alert;

    public ConverterView() {
        initWidget(uiBinder.createAndBindUi(this));
        srcTextArea.setText(
            "# This is a comment.\n" +
            "#\n" +
            "# Please insert your source redirection rules to this area after/instead of comments.\n" +
            "#\n" +
            "# Examples of redirection rules:\n" +
            "# old-url.html                       new-url.html\n" +
            "# index.php                          index.html\n" +
            "#\n" +
            "# old-directory/                     new-directory/\n" +
            "#\n" +
            "# internal/page.html            http://external.domain.com/index.html\n");
    }

    @UiHandler("convertButton")
    void handleConvertButtonClick(@SuppressWarnings("unused") ClickEvent event) {
        dstTextArea.setText("");
        alert.setText("");
        alert.setVisible(true);

        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\"?>\n");
        sb.append("<RoutingRules>\n");

        String[] srcLines = srcTextArea.getText().split("\n");
        if (srcLines.length > MAX_ALLOWED_ROUTES) {
            alert.setType(AlertType.ERROR);
            alert.setText("Error: More then 50 rules (" + srcLines.length + ").");
            return;
        }

        for (String line : srcLines) {
            if (line.startsWith("#")) continue;
            String[] split = line.split("\\s+");
            if (line.trim().isEmpty()) {
                // empty line
                continue;
            }
            if (split.length == 1 || split.length > 2) {
                alert.setType(AlertType.WARNING);
                alert.setText(alert.getText() + "\nSKIPPED: Line has more or less than 2 blocks: " +
                    line + " (" + split.length + " blocks)\n");
                continue;
            }
            String from = split[0];
            String to = split[1];
            String host = null;
            String protocol = null;
            if (to.contains("://")) {
                int hostStart = to.indexOf("://") + 3;
                int hostEnd = to.indexOf('/', hostStart);
                if (hostEnd == -1) hostEnd = to.length();
                int keyStart = hostEnd + 1;
                host = to.substring(hostStart, hostEnd);
                protocol = to.substring(0, hostStart-3);
                to = (to.length() > keyStart)? to.substring(keyStart) : "";
            }
            sb.append("<RoutingRule>")
              .append("<Condition>")
              .append("<KeyPrefixEquals>")
              .append(from)
              .append("</KeyPrefixEquals>")
              .append("</Condition>")
              .append("<Redirect>");
            if (host != null) {
                sb.append("<HostName>").append(host).append("</HostName>");
                sb.append("<Protocol>").append(protocol).append("</Protocol>");
            }
            sb.append("<ReplaceKeyPrefixWith>")
              .append(to)
              .append("</ReplaceKeyPrefixWith>")
              .append("</Redirect>")
              .append("</RoutingRule>\n");
        }
        sb.append("</RoutingRules>");

        dstTextArea.setText(sb.toString());
        if (alert.getText().isEmpty()) {
            alert.setType(AlertType.SUCCESS);
            alert.setText("Converted");
        }

    }

}
