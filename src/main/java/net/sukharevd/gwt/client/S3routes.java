package net.sukharevd.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class S3routes implements EntryPoint {

  // private final Messages messages = GWT.create(Messages.class);

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
      ConverterViewImpl view = new ConverterViewImpl();
      new ConverterViewPresenter(view);
      RootPanel.get("converterContainer").add(view);
  }
}
