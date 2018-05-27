package mobi.tealsoft.qtrade;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ProgressBar;
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
@Push
public class QtradeUI extends UI {
	@WebServlet(urlPatterns = "/*", name = "QtradeUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = QtradeUI.class, productionMode = false)
	public static class QtradeUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1773407890951977885L;
	}

	private static final long serialVersionUID = -3397955958954838533L;
	private static final long REFRESH_WAIT = 50L;

	private TextField fieldTotalOrders;
	private TextField fieldAverageAmount;

	private Map<String, ProgressBar> ordersCountryBars;
	private GridLayout gridOrdersCountry;

	private Map<String, ProgressBar> ordersCurrencyBars;
	private GridLayout gridOrdersCurrency;

	private Button buttonRefresh;
	private Button buttonStop;

	private AtomicBoolean refresh;

	private boolean containsNewMapKey(Map<String, Float> dataMap, Map<String, ProgressBar> barMap) {
		for (String key : dataMap.keySet()) {
			if (!barMap.containsKey(key)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	private Layout getButtons() {
		refresh = new AtomicBoolean(false);

		final HorizontalLayout layoutButtons = new HorizontalLayout();
		buttonRefresh = new Button("Refresh");
		buttonRefresh.addClickListener(e -> {
			buttonRefresh.setEnabled(false);
			buttonStop.setEnabled(true);

			refresh.set(true);
			launchUpdater(UI.getCurrent());
		});

		buttonStop = new Button("Stop");
		buttonStop.setEnabled(false);
		buttonStop.addClickListener(e -> {
			buttonRefresh.setEnabled(true);
			buttonStop.setEnabled(false);
			refresh.set(false);
		});

		layoutButtons.addComponents(buttonRefresh, buttonStop);

		return layoutButtons;
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		ordersCountryBars = new TreeMap<>();
		ordersCurrencyBars = new TreeMap<>();

		final VerticalLayout layout = new VerticalLayout();
		final Label title = new Label("Qtrade FOREX statistics");

		final GridLayout grid = new GridLayout(2, 2);
		grid.setSpacing(true);

		fieldTotalOrders = new TextField("Total Orders");
		fieldTotalOrders.setWidth(200, Unit.PIXELS);
		fieldTotalOrders.setVisible(true);
		fieldTotalOrders.setReadOnly(true);
		grid.addComponent(fieldTotalOrders);

		fieldAverageAmount = new TextField("Average Amount");
		fieldAverageAmount.setWidth(200, Unit.PIXELS);
		fieldAverageAmount.setVisible(true);
		fieldAverageAmount.setReadOnly(true);
		grid.addComponent(fieldAverageAmount);

		gridOrdersCountry = new GridLayout();
		gridOrdersCountry.setCaption("Orders by Country");
		grid.addComponent(gridOrdersCountry);

		gridOrdersCurrency = new GridLayout();
		gridOrdersCurrency.setCaption("Orders by Currency");
		grid.addComponent(gridOrdersCurrency);

		layout.addComponents(title, grid, getButtons());

		setContent(layout);
		buttonRefresh.click();
	}

	private void launchUpdater(UI ui) {
		new Thread(() -> {
			while (refresh.get()) {
				try {
					Thread.sleep(REFRESH_WAIT);
				} catch (final InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				updateUI(ui);
			}
		}).start();
	}

	private void refreshBarLayout(GridLayout layout, Map<String, Float> dataMap, Map<String, ProgressBar> barMap) {
		barMap.clear();
		layout.removeAllComponents();
		layout.setColumns(2);

		if (dataMap.isEmpty()) {
			layout.setRows(1);

			Label blank = new Label();
			layout.addComponent(blank);

			Label label = new Label("Sorry, no data available yet.");
			layout.addComponent(label);
			layout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);

		} else {
			layout.setRows(dataMap.size());

			for (Map.Entry<String, Float> entry : dataMap.entrySet()) {
				Label label = new Label(entry.getKey());
				layout.addComponent(label);
				layout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);

				ProgressBar bar = new ProgressBar();
				bar.setWidth(200, Unit.PIXELS);
				bar.setVisible(true);
				barMap.put(entry.getKey(), bar);
				layout.addComponent(bar);
				layout.setComponentAlignment(bar, Alignment.MIDDLE_LEFT);
			}
		}
	}

	private void refreshBars(Map<String, Float> dataMap, Map<String, ProgressBar> barMap) {
		float maxValue = 0.0F;
		for (Float value : dataMap.values()) {
			maxValue = Math.max(maxValue, value);
		}

		for (Map.Entry<String, ProgressBar> entry : barMap.entrySet()) {
			Float value = dataMap.get(entry.getKey());
			if (value != null && maxValue != 0) {
				ProgressBar bar = entry.getValue();
				bar.setValue(value / maxValue);
			}
		}
	}

	private void refreshOrdersCountry() {
		Map<String, Float> ordersCountry = Statistics.INSTANCE.readOrdersCountry();
		if (ordersCountry.isEmpty() || containsNewMapKey(ordersCountry, ordersCountryBars)) {
			refreshBarLayout(gridOrdersCountry, ordersCountry, ordersCountryBars);
		}
		refreshBars(ordersCountry, ordersCountryBars);
	}

	private void refreshOrdersCurrency() {
		Map<String, Float> ordersCurrency = Statistics.INSTANCE.readOrdersCurrency();
		if (ordersCurrency.isEmpty() || containsNewMapKey(ordersCurrency, ordersCurrencyBars)) {
			refreshBarLayout(gridOrdersCurrency, ordersCurrency, ordersCurrencyBars);
		}
		refreshBars(ordersCurrency, ordersCurrencyBars);
	}

	private void updateUI(UI ui) {
		ui.access(() -> {
			fieldTotalOrders.setValue(Integer.toString(Statistics.INSTANCE.readTotalOrders()));
			fieldAverageAmount.setValue(Float.toString(Statistics.INSTANCE.readAverageAmount()));

			refreshOrdersCountry();
			refreshOrdersCurrency();
			ui.push();
		});
	}
}
