package mobi.tealsoft.qtrade;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("QtradeTheme")
public class QtradeUI extends UI {

	@WebServlet(urlPatterns = "/*", name = "QtradeUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = QtradeUI.class, productionMode = false)
	public static class QtradeUIServlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		final VerticalLayout layout = new VerticalLayout();

		final TextField name = new TextField();
		name.setCaption("Type your name here:");

		Button button = new Button("Click Me");
		button.addClickListener(e -> {
			layout.addComponent(new Label("Thanks " + name.getValue() + ", it works!"));
		});

		layout.addComponents(name, button);

		setContent(layout);
	}
}
