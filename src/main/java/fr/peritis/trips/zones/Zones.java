package fr.peritis.trips.zones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Contains initialization utility methods for zones given in the problem statement, as well as pairing them
 * and associating the prices for each pair.
 */
public class Zones {

	public static final int ZONE_POSITION_1 = 1;
	public static final int ZONE_POSITION_2 = 2;
	public static final int ZONE_POSITION_3 = 3;
	public static final int ZONE_POSITION_4 = 4;

	/**
	 * Collection of zones
	 */
	private static final List<Zone> zones = new ArrayList<>();

	/**
	 * Collection of zone pairs.
	 */
	private static final Set<ZonePair> zonePairs = new TreeSet<>();

	/**
	 * Initializes zone-related data.
	 */
	public static void init() {
		initZones();
		initDestinationsPricing();
	}

	/**
	 * Initialize zones for each position and add to collection.
	 */
	private static void initZones() {
		Zone zone1 = new Zone(ZONE_POSITION_1);
		zones.add(zone1);

		Zone zone2 = new Zone(ZONE_POSITION_2);
		zones.add(zone2);

		Zone zone3 = new Zone(ZONE_POSITION_3);
		zones.add(zone3);

		Zone zone4 = new Zone(ZONE_POSITION_4);
		zones.add(zone4);
	}

	/**
	 * Initialize pricing for zone pairs, as documented.
	 */
	private static void initDestinationsPricing() {
		zones.forEach(zone -> {
			switch (zone.getPosition()) {
				case ZONE_POSITION_1:
				case ZONE_POSITION_2:
					setupZonePairs(zone, Arrays.asList(240L, 240L, 280L, 300L));
					break;

				case ZONE_POSITION_3:
					setupZonePairs(zone, Arrays.asList(280L, 280L, 200L, 200L));
					break;

				case ZONE_POSITION_4:
					setupZonePairs(zone, Arrays.asList(300L, 300L, 200L, 200L));
					break;
			}

		});
	}

	private static void setupZonePairs(Zone zone, List<Long> prices) {
		createPairs(ZONE_POSITION_1, zone, prices, 0);

		createPairs(ZONE_POSITION_2, zone, prices, 1);

		createPairs(ZONE_POSITION_3, zone, prices, 2);

		createPairs(ZONE_POSITION_4, zone, prices, 3);
	}

	private static void createPairs(int zonePosition, Zone zone, List<Long> prices, int i) {
		ZonePair zonePair = new ZonePair();
		zonePair.getZones()[0] = zone;
		zonePair.getZones()[1] = getZoneByPosition(zonePosition);
		zonePair.setPrice(prices.get(i));
		zonePairs.add(zonePair);
	}

	/**
	 * Get zone by position
	 * @param position 1-4
	 * @return Corresponding {@link Zone} instance or throws {@link NoSuchElementException} if not found.
	 */
	public static Zone getZoneByPosition(int position) {
		return zones.stream().filter(zone -> zone.getPosition() == position).findFirst().orElseThrow();
	}
	
	public static Set<ZonePair> getZonePairs() {
		return zonePairs;
	}
}
