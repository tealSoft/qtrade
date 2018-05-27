package mobi.tealsoft.qtrade;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.annotation.Generated;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

@Generated(value = "org.junit-tools-1.0.6")
public class OrderServletTest {
	private static final Logger LOG = Logger.getLogger(OrderServletTest.class);

	private static final String QTRADE_URL = "http://localhost:8080/qtrade/orders";

	private static final int ORDER_COUNT = 100000;
	private static final String[] COUNTRIES = new String[] { "FR", "BE", "DE", "ES", "PT", "IT", "NL", "LU", "CH", "UK",
			"IR", "US", "JP" };
	private static final String[] CURRENCIES = new String[] { "EUR", "USD", "GBP", "CHF", "JPY" };

	private Random random;
	private Gson gson;

	private SimpleDateFormat sdf;

	private Order getNewOrder() {
		Order order = new Order();

		order.setUserId(random.nextInt(1000000));
		order.setOriginatingCountry(COUNTRIES[random.nextInt(COUNTRIES.length)]);

		order.setCurrencyFrom(CURRENCIES[random.nextInt(CURRENCIES.length)]);
		do {
			order.setCurrencyTo(CURRENCIES[random.nextInt(CURRENCIES.length)]);
		} while (order.getCurrencyFrom().equals(order.getCurrencyTo()));

		order.setAmountBuy(random.nextDouble() * 10000);
		order.setAmountSell(random.nextDouble() * 10000);
		order.setRate(random.nextDouble());

		order.setTimePlaced(sdf.format(new Date()));

		return order;
	}

	private int sendRequest(String json) throws Exception {
		int response = HttpURLConnection.HTTP_INTERNAL_ERROR;

		URL url = new URL(QTRADE_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);

		OutputStreamWriter output = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
		output.write(json);
		output.flush();
		output.close();

		response = connection.getResponseCode();
		connection.disconnect();

		return response;
	}

	@Before
	public void setUp() throws Exception {
		BasicConfigurator.configure();

		random = new Random();
		gson = new Gson();

		sdf = new SimpleDateFormat(Order.DATE_FORMAT);
	}

	// @MethodRef(name = "destroy", signature = "()V")
	@Test
	public void testPost() throws Exception {
		LOG.info("Sending " + ORDER_COUNT + " orders.");
		for (int i = 0; i < ORDER_COUNT; i++) {
			Order order = getNewOrder();
			String json = gson.toJson(order);

			// LOG.info("Order: " + json);
			sendRequest(json);
		}
	}
}