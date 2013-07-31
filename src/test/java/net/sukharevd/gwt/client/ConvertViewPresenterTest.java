package net.sukharevd.gwt.client;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ConvertViewPresenterTest {

    private ConverterViewPresenter presenter;
    private ConverterView view;

    @Before
    public void prepare() {
        view = mock(ConverterView.class);
        presenter = new ConverterViewPresenter(view);
    }

    @Test
    public void shouldSetValidXmlWhenNoRouteRules() {
        when(view.getSourceRules()).thenReturn("");
        presenter.onConvertButtonClicked();
        verify(view).setResultingRules("<?xml version=\"1.0\"?>\n<RoutingRules>\n</RoutingRules>");
    }

    @Test
    public void shouldConvertRouteRule() {
        when(view.getSourceRules()).thenReturn("from/index.php   to/index.html");
        presenter.onConvertButtonClicked();
        verify(view).setResultingRules("<?xml version=\"1.0\"?>\n" +
                "<RoutingRules>\n" +
                "<RoutingRule>" +
                "<Condition><KeyPrefixEquals>from/index.php</KeyPrefixEquals></Condition>" +
                "<Redirect><ReplaceKeyPrefixWith>to/index.html</ReplaceKeyPrefixWith></Redirect>" +
                "</RoutingRule>\n" +
                "</RoutingRules>");
    }

    @Test
    public void shouldConvertRouteRuleToAnotherHost() {
        when(view.getSourceRules()).thenReturn("external.xhtml   http://example.com/");
        presenter.onConvertButtonClicked();
        verify(view).setResultingRules("<?xml version=\"1.0\"?>\n" +
                "<RoutingRules>\n" +
                "<RoutingRule>" +
                "<Condition><KeyPrefixEquals>external.xhtml</KeyPrefixEquals></Condition>" +
                "<Redirect><HostName>example.com</HostName>" +
                "<Protocol>http</Protocol>" +
                "<ReplaceKeyPrefixWith></ReplaceKeyPrefixWith></Redirect></RoutingRule>\n" +
                "</RoutingRules>");
    }

    @Test
    public void shouldShowSucceedNotificationWhenNoIssues() {
        when(view.getSourceRules()).thenReturn("");
        presenter.onConvertButtonClicked();
        verify(view).setState(ConverterView.State.SUCCESS);
    }

    @Test
    public void shouldShowWarningNotificationWhenIncorrectRule() {
        when(view.getSourceRules()).thenReturn("rule should contain only 2 blocks");
        presenter.onConvertButtonClicked();
        verify(view).setState(ConverterView.State.WARNING);
    }

    @Test
    public void shouldShowErrorNotificationWhenTooManyRules() {
        StringBuilder rules = new StringBuilder();
        for (int i = 0; i < ConverterViewPresenter.MAX_ALLOWED_ROUTES+1; i++)
            rules.append("from").append(i).append(" to").append(i).append("\n");
        when(view.getSourceRules()).thenReturn(rules.toString());
        presenter.onConvertButtonClicked();
        verify(view).setState(ConverterView.State.ERROR);
    }
}
