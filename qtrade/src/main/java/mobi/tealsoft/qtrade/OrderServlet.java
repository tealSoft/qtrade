package mobi.tealsoft.qtrade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@WebServlet(name = "OrderServlet", urlPatterns = { "/orders" }, loadOnStartup = 1)
public class OrderServlet extends HttpServlet {
	private static final Logger LOG = Logger.getLogger(OrderServlet.class);

	private static final long serialVersionUID = 4745092472678765997L;
	private static final int BUFFER_SIZE = 1024;

	private Gson gson;

	@Override
	public void destroy() {
		Order order = new Order();
		OrderQueue.INSTANCE.send(order);

		super.destroy();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String jsonBody = getBody(request);
		System.out.println("++++ json=" + jsonBody);

		if (!StringUtils.isEmpty(jsonBody)) {
			try {
				Order order = gson.fromJson(jsonBody, Order.class);
				OrderQueue.INSTANCE.send(order);
			} catch (JsonSyntaxException e) {
				LOG.error("Request body: " + jsonBody, e);
			}
		}
	}

	public String getBody(HttpServletRequest request) throws IOException {
		StringBuilder body = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
			if (reader != null) {
				char[] buffer = new char[BUFFER_SIZE];
				int byteCount = -1;
				while ((byteCount = reader.read(buffer)) > 0) {
					body.append(buffer, 0, byteCount);
				}
			}
		}

		return body.toString();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		BasicConfigurator.configure();
		gson = new Gson();

		Thread orderProcessor = new Thread(new OrderProcessor());
		orderProcessor.start();
	}

}
