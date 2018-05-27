package mobi.tealsoft.qtrade;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

public enum Statistics {

	INSTANCE;

	private AtomicInteger totalOrders;
	private ConcurrentHashMap<String, Float> ordersCountry;

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
			Float count = ordersCountry.get(order.getOriginatingCountry());
			if (count == null) {
				count = Float.valueOf(0.0F);
			}
			ordersCountry.put(order.getOriginatingCountry(), Float.valueOf(count + 1.0F));
		}
	}

	public Map<String, Float> readOrdersCountry() {
		Map<String, Float> result = new TreeMap<>();
		result.putAll(ordersCountry);
		return result;
	}

	public int readTotalOrders() {
		return totalOrders.get();
	}

}
