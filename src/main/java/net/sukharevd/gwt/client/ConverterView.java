package net.sukharevd.gwt.client;

/**
 * The interface represents view that enables users to convert routing rules
 * from plain text to valid XML format.
 */
public interface ConverterView {

    /**
     * The enumeration represents possible states of conversion.
     */
    public static enum State { SUCCESS, WARNING, ERROR }

    void setPresenter(ConverterViewPresenter presenter);
    String getSourceRules();
    void setSourceRules(String text);
    void setResultingRules(String text);
    void setNotifications(String text);
    void setState(State state);
}
