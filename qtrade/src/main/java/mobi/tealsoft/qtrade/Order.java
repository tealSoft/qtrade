package mobi.tealsoft.qtrade;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Order {
	public static final String DATE_FORMAT = "dd-MM-yy HH:mm:ss";

	private long userId;
	private String currencyFrom;
	private String currencyTo;
	private double amountSell;
	private double amountBuy;
	private double rate;
	private String timePlaced;
	private String originatingCountry;

	public double getAmountBuy() {
		return amountBuy;
	}

	public double getAmountSell() {
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

	public double getRate() {
		return rate;
	}

	public String getTimePlaced() {
		return timePlaced;
	}

	public long getUserId() {
		return userId;
	}

	public void setAmountBuy(double amountBuy) {
		this.amountBuy = amountBuy;
	}

	public void setAmountSell(double amountSell) {
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

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void setTimePlaced(String timePlaced) {
		this.timePlaced = timePlaced;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
