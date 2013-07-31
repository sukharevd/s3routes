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

/**
 * The class represents view that enables users to convert routing rules
 * from plain text to valid XML format.
 */
public class ConverterViewImpl extends Composite implements ConverterView {

    @UiTemplate("ConverterViewImpl.ui.xml")
    interface ConverterViewUiBinder extends UiBinder<FlowPanel, ConverterViewImpl> {}

    private static ConverterViewUiBinder uiBinder = GWT.create(ConverterViewUiBinder.class);

    @UiField TextArea srcTextArea;
    @UiField TextArea dstTextArea;
    @UiField AlertBlock notifications;

    private ConverterViewPresenter presenter;

    public ConverterViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("convertButton")
    void handleConvertButtonClick(@SuppressWarnings("unused") ClickEvent event) {
        if (presenter != null) {
            presenter.onConvertButtonClicked();
        }
    }

    @Override
    public void setPresenter(ConverterViewPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public String getSourceRules() {
        return srcTextArea.getText();
    }

    @Override
    public void setSourceRules(String text) {
        srcTextArea.setText(text);
    }

    @Override
    public void setResultingRules(String text) {
        dstTextArea.setText(text);
    }

    @Override
    public void setNotifications(String text) {
        notifications.setText(text);
        notifications.setVisible(text != null && !text.isEmpty());
    }

    @Override
    public void setState(State state) {
        notifications.setType(convertState(state));
    }

    private AlertType convertState(State state) {
        switch (state) {
            case SUCCESS: return AlertType.SUCCESS;
            case WARNING: return AlertType.WARNING;
            case ERROR: return AlertType.ERROR;
            default: return AlertType.INFO;
        }
    }

}
