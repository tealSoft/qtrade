package mobi.tealsoft.qtrade;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

public enum Statistics {

	INSTANCE;

	private AtomicInteger totalOrders;
	private ConcurrentHashMap<String, Integer> ordersCountry;

	private Statistics() {
		totalOrders = new AtomicInteger(0);
		ordersCountry = new ConcurrentHashMap<>();
	}

	public void addOrder(Order order) {
		totalOrders.incrementAndGet();

		addOrdersCountry(order);

	}

	private void addOrdersCountry(Order order) {
		if (!StringUtils.isEmpty(order.getOriginatingCountry())) {
			Integer count = ordersCountry.get(order.getOriginatingCountry());
			if (count == null) {
				count = Integer.valueOf(0);
			}
			ordersCountry.put(order.getOriginatingCountry(), Integer.valueOf(count + 1));
		}
	}

	public Map<String, Integer> readOrdersCountry() {
		Map<String, Integer> result = new TreeMap<>();
		result.putAll(ordersCountry);
		return result;
	}

	public int readTotalOrders() {
		return totalOrders.get();
	}

}
