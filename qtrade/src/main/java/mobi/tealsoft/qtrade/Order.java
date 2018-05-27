package mobi.tealsoft.qtrade;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Order {
	public static final String DATE_FORMAT = "dd-MM-yy HH:mm:ss";

	private int userId;
	private String currencyFrom;
	private String currencyTo;
	private float amountSell;
	private float amountBuy;
	private float rate;
	private String timePlaced;
	private String originatingCountry;

	public float getAmountBuy() {
		return amountBuy;
	}

	public float getAmountSell() {
		return amountSell;
	}

	public String getCurrencyFrom() {
		return currencyFrom;
	}

	public String getCurrencyTo() {
		return currencyTo;
	}

	public String getOriginatingCountry() {
		return originatingCountry;
	}

	public float getRate() {
		return rate;
	}

	public String getTimePlaced() {
		return timePlaced;
	}

	public int getUserId() {
		return userId;
	}

	public void setAmountBuy(float amountBuy) {
		this.amountBuy = amountBuy;
	}

	public void setAmountSell(float amountSell) {
		this.amountSell = amountSell;
	}

	public void setCurrencyFrom(String currencyFrom) {
		this.currencyFrom = currencyFrom;
	}

	public void setCurrencyTo(String currencyTo) {
		this.currencyTo = currencyTo;
	}

	public void setOriginatingCountry(String originatingCountry) {
		this.originatingCountry = originatingCountry;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public void setTimePlaced(String timePlaced) {
		this.timePlaced = timePlaced;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
