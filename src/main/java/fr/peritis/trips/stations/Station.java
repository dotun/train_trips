package fr.peritis.trips.stations;

import fr.peritis.trips.zones.Zone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A station representation.
 */
public class Station {

	/**
	 * Uniquely identifies a station.
	 */
	private final String label;

	/**
	 * A station is linked to a maximum of 2 zones.
	 * This is populated in the {@link Stations#mapStationsToZones()} initialization method.
	 */
	private final Zone[] zones = new Zone[2];

	public Station(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public Zone[] getZones() {
		return zones;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Station station = (Station) o;
		return getLabel().equals(station.getLabel()) && Arrays.equals(getZones(), station.getZones());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getLabel(), Arrays.hashCode(getZones()));
	}
}
