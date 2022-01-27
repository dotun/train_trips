package fr.peritis.trips.models;

import java.util.Objects;

/**
 * Type representation of the JSON path that encompasses 'stationStart', 'stationEnd', 'startedJourneyAt', 'costInCents'
 * 'zoneFrom' and 'zoneTo'.
 */
public class Trip {

	/**
	 * represents JSON path 'stationStart'
	 */
	private String stationStart;

	/**
	 * represents JSON path 'stationEnd'
	 */
	private String stationEnd;

	/**
	 * represents JSON path 'startedJourneyAt'
	 */
	private long startedJourneyAt;

	/**
	 * represents JSON path 'costInCents'
	 */
	private long costInCents;

	/**
	 * represents JSON path 'zoneFrom'
	 */
	private int zoneFrom;

	/**
	 * represents JSON path 'zoneTo'
	 */
	private int zoneTo;

	private Trip(){}

	public String getStationStart() {
		return stationStart;
	}

	void setStationStart(String stationStart) {
		this.stationStart = stationStart;
	}

	public String getStationEnd() {
		return stationEnd;
	}

	void setStationEnd(String stationEnd) {
		this.stationEnd = stationEnd;
	}

	public long getStartedJourneyAt() {
		return startedJourneyAt;
	}

	void setStartedJourneyAt(long startedJourneyAt) {
		this.startedJourneyAt = startedJourneyAt;
	}

	public long getCostInCents() {
		return costInCents;
	}

	void setCostInCents(long costInCents) {
		this.costInCents = costInCents;
	}

	public int getZoneFrom() {
		return zoneFrom;
	}

	void setZoneFrom(int zoneFrom) {
		this.zoneFrom = zoneFrom;
	}

	public int getZoneTo() {
		return zoneTo;
	}

	void setZoneTo(int zoneTo) {
		this.zoneTo = zoneTo;
	}

	public static final class Builder {
		private String stationStart;
		private String stationEnd;
		private long startedJourneyAt;
		private long costInCents;
		private int zoneFrom;
		private int zoneTo;

		private Builder() {
		}

		public static Builder aTrip() {
			return new Builder();
		}

		public Builder stationStart(String stationStart) {
			this.stationStart = stationStart;
			return this;
		}

		public Builder stationEnd(String stationEnd) {
			this.stationEnd = stationEnd;
			return this;
		}

		public Builder startedJourneyAt(long startedJourneyAt) {
			this.startedJourneyAt = startedJourneyAt;
			return this;
		}

		public Builder costInCents(long costInCents) {
			this.costInCents = costInCents;
			return this;
		}

		public Builder zoneFrom(int zoneFrom) {
			this.zoneFrom = zoneFrom;
			return this;
		}

		public Builder zoneTo(int zoneTo) {
			this.zoneTo = zoneTo;
			return this;
		}

		public Trip build() {
			Trip trip = new Trip();
			trip.setStationStart(stationStart);
			trip.setStationEnd(stationEnd);
			trip.setStartedJourneyAt(startedJourneyAt);
			trip.setCostInCents(costInCents);
			trip.setZoneFrom(zoneFrom);
			trip.setZoneTo(zoneTo);
			return trip;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Trip trip = (Trip) o;
		return getStartedJourneyAt() == trip.getStartedJourneyAt() && getCostInCents() == trip.getCostInCents() && getZoneFrom() == trip.getZoneFrom() && getZoneTo() == trip.getZoneTo() && getStationStart().equals(trip.getStationStart()) && getStationEnd().equals(trip.getStationEnd());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getStationStart(), getStationEnd(), getStartedJourneyAt(), getCostInCents(), getZoneFrom(), getZoneTo());
	}
}
