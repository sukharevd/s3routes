package net.sukharevd.gwt.client;

/**
 * The class handles user's interactions with converter view.
 * @see <a href="http://www.gwtproject.org/articles/mvp-architecture.html">MVP Architcture</a>
 */
public class ConverterViewPresenter {

    public static final int MAX_ALLOWED_ROUTES = 50;
    public static final String REDIRECTION_RULE_EXAMPLES =
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
        "# internal/page.html            http://external.domain.com/index.html\n";

    private final ConverterView view;

    public ConverterViewPresenter(ConverterView view) {
        if (view == null) throw new NullPointerException();
        this.view = view;
        view.setSourceRules(REDIRECTION_RULE_EXAMPLES);
        view.setPresenter(this);
    }

    public void onConvertButtonClicked() {
        view.setResultingRules("");
        view.setNotifications("");

        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\"?>\n");
        sb.append("<RoutingRules>\n");

        StringBuilder notifications = new StringBuilder();

        String[] srcLines = view.getSourceRules().split("\n");
        if (srcLines.length > MAX_ALLOWED_ROUTES) {
            view.setState(ConverterView.State.ERROR);
            view.setNotifications("Error: More then 50 rules (" + srcLines.length + ").");
            return;
        }

        convertRules(srcLines, sb, notifications);
        sb.append("</RoutingRules>");

        if (notifications.length() > 0) {
            view.setState(ConverterView.State.WARNING);
            view.setNotifications(notifications.toString());
        } else {
            view.setState(ConverterView.State.SUCCESS);
            view.setNotifications("Converted");
        }
        view.setResultingRules(sb.toString());
    }

    private void convertRules(String[] lines, StringBuilder result, StringBuilder notifications) {
        for (String line : lines) {
            if (line.startsWith("#")) continue;
            String[] split = line.split("\\s+");
            if (line.trim().isEmpty()) {
                // empty line
                continue;
            }
            if (split.length == 1 || split.length > 2) {
                notifications.append("\nSKIPPED: Line has more or less than 2 blocks: ")
                        .append(line).append(" (").append(split.length).append(" blocks)\n");
                continue;
            }
            convertRule(result, split[0], split[1]);
        }
    }

    private void convertRule(StringBuilder result, String from, String to) {
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
        result.append("<RoutingRule>")
                .append("<Condition>")
                .append("<KeyPrefixEquals>")
                .append(from)
                .append("</KeyPrefixEquals>")
                .append("</Condition>")
                .append("<Redirect>");
        if (host != null) {
            result.append("<HostName>").append(host).append("</HostName>");
            result.append("<Protocol>").append(protocol).append("</Protocol>");
        }
        result.append("<ReplaceKeyPrefixWith>")
                .append(to)
                .append("</ReplaceKeyPrefixWith>")
                .append("</Redirect>")
                .append("</RoutingRule>\n");
    }
}
