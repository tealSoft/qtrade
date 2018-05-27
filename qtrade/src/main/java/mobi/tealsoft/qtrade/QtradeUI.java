package mobi.tealsoft.qtrade;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ProgressBar;
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
		private static final long serialVersionUID = 1773407890951977885L;
	}

	private static final long serialVersionUID = -3397955958954838533L;

	private Map<String, ProgressBar> ordersCountryBars;

	private boolean containsNewMapKey(Map<String, Integer> dataMap, Map<String, ProgressBar> barMap) {
		for (String key : dataMap.keySet()) {
			if (!barMap.containsKey(key)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		ordersCountryBars = new TreeMap<>();

		final VerticalLayout layout = new VerticalLayout();
		final Label title = new Label("Qtrade FOREX statistics");

		VerticalLayout layoutOrdersCountry = new VerticalLayout();

		Button button = new Button("Refresh");
		button.addClickListener(e -> {
			refreshOrdersCountry(layoutOrdersCountry);
		});

		layout.addComponents(title, layoutOrdersCountry, button);

		refreshOrdersCountry(layoutOrdersCountry);

		setContent(layout);
	}

	private void refreshBarLayout(Layout layout, Map<String, Integer> dataMap, Map<String, ProgressBar> barMap) {
		layout.removeAllComponents();

		barMap.clear();
		for (Map.Entry<String, Integer> entry : dataMap.entrySet()) {
			ProgressBar bar = new ProgressBar();
			bar.setCaption(entry.getKey());
			bar.setHeight(20, Unit.PIXELS);
			bar.setWidth(200, Unit.PIXELS);
			bar.setVisible(true);
			barMap.put(entry.getKey(), bar);
			layout.addComponent(bar);
		}
	}

	private void refreshBars(int totalOrders, Map<String, Integer> dataMap, Map<String, ProgressBar> barMap) {
		for (Map.Entry<String, ProgressBar> entry : barMap.entrySet()) {
			Integer value = dataMap.get(entry.getKey());
			if (value != null && totalOrders != 0) {
				ProgressBar bar = entry.getValue();
				bar.setValue(value / totalOrders);
			}
		}
	}

	private void refreshOrdersCountry(Layout layout) {
		int totalOrders = Statistics.INSTANCE.readTotalOrders();
		Map<String, Integer> ordersCountry = Statistics.INSTANCE.readOrdersCountry();
		if (containsNewMapKey(ordersCountry, ordersCountryBars)) {
			refreshBarLayout(layout, ordersCountry, ordersCountryBars);
		}
		refreshBars(totalOrders, ordersCountry, ordersCountryBars);
	}
}
