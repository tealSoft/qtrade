package mobi.tealsoft.qtrade;

import java.util.concurrent.ArrayBlockingQueue;

public enum QtradeQueue {

	INSTANCE;

	public static final int QUEUE_SIZE = 10000;

	private ArrayBlockingQueue<Order> orderQueue;

	private QtradeQueue() {
		orderQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
	}

	public void send(Order order) {
		try {
			orderQueue.put(order);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public Order take() {
		try {
			return orderQueue.take();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return null;
		}
	}

}
