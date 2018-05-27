package mobi.tealsoft.qtrade;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

public enum Statistics {

	INSTANCE;

	private AtomicInteger totalOrders;
	private AtomicInteger totalAmount;
	private ConcurrentHashMap<String, Float> ordersCountry;
	private ConcurrentHashMap<String, Float> ordersCurrency;

	private Statistics() {
		totalOrders = new AtomicInteger(0);
		totalAmount = new AtomicInteger(Float.floatToIntBits(0.0F));
		ordersCountry = new ConcurrentHashMap<>();
		ordersCurrency = new ConcurrentHashMap<>();
	}

	public void addOrder(Order order) {
		totalOrders.incrementAndGet();

		float total = Float.intBitsToFloat(totalAmount.get()) + order.getAmountBuy();
		totalAmount.set(Float.floatToIntBits(total));

		addOrdersCountry(order);
		addOrdersCurrency(order);

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

	private void addOrdersCurrency(Order order) {
		if (!StringUtils.isEmpty(order.getCurrencyTo())) {
			Float count = ordersCurrency.get(order.getCurrencyTo());
			if (count == null) {
				count = Float.valueOf(0.0F);
			}
			ordersCurrency.put(order.getCurrencyTo(), Float.valueOf(count + 1.0F));
		}
	}

	public float readAverageAmount() {
		float total = Float.intBitsToFloat(totalAmount.get());
		if (Float.isNaN(total) || totalOrders.get() == 0) {
			return 0.0F;

		} else {
			return total / totalOrders.get();
		}
	}

	public Map<String, Float> readOrdersCountry() {
		Map<String, Float> result = new TreeMap<>();
		result.putAll(ordersCountry);
		return result;
	}

	public Map<String, Float> readOrdersCurrency() {
		Map<String, Float> result = new TreeMap<>();
		result.putAll(ordersCurrency);
		return result;
	}

	public int readTotalOrders() {
		return totalOrders.get();
	}

}
