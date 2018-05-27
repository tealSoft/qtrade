package mobi.tealsoft.qtrade;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class OrderProcessor implements Runnable {
	private static final Logger LOG = Logger.getLogger(OrderProcessor.class);

	@Override
	public void run() {
		LOG.info("START thread");

		boolean terminate = Boolean.FALSE;
		while (!terminate) {
			Order order = OrderQueue.INSTANCE.take();
			if (order.getUserId() == 0 && StringUtils.isEmpty(order.getOriginatingCountry())
					&& StringUtils.isEmpty(order.getCurrencyFrom()) && StringUtils.isEmpty(order.getCurrencyTo())) {
				terminate = Boolean.TRUE;

			} else {
				Statistics.INSTANCE.addOrder(order);
			}
		}

		LOG.info("END thread");
	}

}
