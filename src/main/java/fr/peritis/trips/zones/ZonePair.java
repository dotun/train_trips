package fr.peritis.trips.zones;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * <p>
 *    Represents the relationship between a pair of zones and the price (in cents) of a journey between stations in
 *    each {@link Zone}.
 * </p>
 *
 * <p>
 *     Within this problem domain, a pair of zones are considered equal regardless of direction.
 * </p>
 *
 * <p>
 *     This implements {@link Comparable} in order to enforce sorting collections of this type by price and the pair of
 *     zones within.
 * </p>
 */
public class ZonePair implements Comparable<ZonePair> {
	private final Zone[] zones = new Zone[2];
	private long price;

	public Zone[] getZones() {
		return zones;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ZonePair zonePair = (ZonePair) o;

		Zone[] zonesCopy = Arrays.copyOf(getZones(), getZones().length);
		Arrays.sort(zonesCopy);

		Zone[] otherCopy = Arrays.copyOf(zonePair.getZones(), zonePair.getZones().length);
		Arrays.sort(otherCopy);

		return Arrays.equals(zonesCopy, otherCopy);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(getPrice());
		result = 31 * result + Arrays.hashCode(getZones());
		return result;
	}

	@Override
	public int compareTo(ZonePair otherZonePair) {

		return Comparator
				.comparing(ZonePair::getPrice)
				.thenComparing((x,y) -> {
					Zone[] zonesCopy = Arrays.copyOf(x.getZones(), x.getZones().length);
					Arrays.sort(zonesCopy);

					Zone[] otherCopy = Arrays.copyOf(y.getZones(), y.getZones().length);
					Arrays.sort(otherCopy);

					return Arrays.compare(zonesCopy, otherCopy);
				})
				.compare(this, otherZonePair);
	}
}
