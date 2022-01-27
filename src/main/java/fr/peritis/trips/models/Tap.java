package fr.peritis.trips.models;

import java.util.Objects;

/**
 * Type representation of the JSON path that encompasses 'unixTimestamp', 'customerId' and 'station'.
 */
public class Tap {

	/**
	 * represents JSON path 'unixTimestamp'
	 */
	private long unixTimestamp;

	/**
	 * represents JSON path 'customerId'
	 */
	private int customerId;

	/**
	 * represents JSON path 'station'
	 */
	private String station;

	public long getUnixTimestamp() {
		return unixTimestamp;
	}

	public void setUnixTimestamp(long unixTimestamp) {
		this.unixTimestamp = unixTimestamp;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tap tap = (Tap) o;
		return getUnixTimestamp() == tap.getUnixTimestamp() && getCustomerId() == tap.getCustomerId() && getStation().equals(tap.getStation());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUnixTimestamp(), getCustomerId(), getStation());
	}
}
