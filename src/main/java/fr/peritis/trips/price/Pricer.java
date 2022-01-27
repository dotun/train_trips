package fr.peritis.trips.price;

import fr.peritis.trips.models.Customer;
import fr.peritis.trips.models.Output;
import fr.peritis.trips.models.Tap;
import fr.peritis.trips.models.Trip;
import fr.peritis.trips.stations.Station;
import fr.peritis.trips.stations.Stations;
import fr.peritis.trips.zones.Zone;
import fr.peritis.trips.zones.ZonePair;
import fr.peritis.trips.zones.Zones;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Processes the pricing of retrieved user 'taps'.
 */
public class Pricer {

	private static final String EQUAL_TIMESTAMP_EXCEPTION_MSG = "For customer %d, entry timestamp at station %s is equal to exit timestamp at station %s.";
	private static final String BAD_ENTRY_EXIT_SEQUENCE_MSG = "For customer %d, entry timestamp at station %s is greater than exit timestamp at station %s.";
	private static final String SAME_ENTRY_EXIT_MSG = "For customer %d, entry and exit stations %s.";

	private Map<Integer, List<Tap>> tapsPerCustomer;

	public Pricer(List<Tap> taps) {
		groupByCustomerId(taps);
	}

	/**
	 * Groups the list of taps by customer id.
	 * @param taps list of taps for all customers.
	 */
	private void groupByCustomerId(List<Tap> taps) {
		tapsPerCustomer = taps.stream().collect(Collectors.groupingBy(Tap::getCustomerId));
	}

	/**
	 * For each customer and respective taps, validates that the given data is valid according to the following rules for
	 * derived entry and exit tap pair per trip:
	 * <ul>
	 *     <li>entry and exit timestamp are not equal</li>
	 *     <li>exit tap timestamp is after entry tap timestamp</li>
	 *     <li>not done in same station</li>
	 * </ul>
	 *
	 *
	 * If any of the validation steps fail, an {@link java.lang.IllegalArgumentException} is thrown with an appropriate message.
	 *
	 * @return true if all validations pass
	 */
	public boolean isInputValid() {
		tapsPerCustomer.forEach((id, taps) -> {
			for (int i = 0; i < taps.size() - 1; i += 2) {

				Tap entryTap = taps.get(i);
				Tap exitTap = taps.get(i + 1);

				if (entryTap.getUnixTimestamp() == exitTap.getUnixTimestamp()) {
					throw new IllegalArgumentException(String.format(EQUAL_TIMESTAMP_EXCEPTION_MSG, id, entryTap.getStation(), exitTap.getStation()));
				}

				if (entryTap.getUnixTimestamp() > exitTap.getUnixTimestamp()) {
					throw new IllegalArgumentException(String.format(BAD_ENTRY_EXIT_SEQUENCE_MSG, id, entryTap.getStation(), exitTap.getStation()));
				}

				if (entryTap.getStation().equals(exitTap.getStation())) {
					throw new IllegalArgumentException(String.format(SAME_ENTRY_EXIT_MSG, id, entryTap.getStation()));
				}
			}
		});

		return true;
	}

	/**
	 * <p>
	 *     For each group of customer taps, it is assumed that the taps can be divided into pairs,
	 *     where the first of the pair is the entry tap, and the second is the exit tap.
	 * </p>
	 * <p>
	 *     The entry and exit stations, as well as the corresponding zones they belong to are extracted based on type system
	 *     which was derived from the given info.
	 * </p>
	 * <p>
	 *     The entry->exit zones form a maximum of four combinations. The shortest distance between the zone combination
	 *     is determined by the difference in zone positions (1 - 4), and this gives the lowest price for the journey.
	 * </p>
	 * <p>
	 *     The required data, including journey cost is extracted from the already initialized data in the type system,
	 *     which is all packaged within the {@link fr.peritis.trips.models.Output} POJO.
	 * </p>
	 *
	 * @return instance of type {@link fr.peritis.trips.models.Output}.
	 */
	public Output generatePricesForCustomers() {
		final Output output = new Output();

		for (Map.Entry<Integer, List<Tap>> customerEntry : tapsPerCustomer.entrySet()) {
			final int customerId = customerEntry.getKey();

			final Customer customer = new Customer(customerId);
			long totalCostInCents = 0;

			final List<Tap> taps = customerEntry.getValue();

			for (int i = 0; i < taps.size() - 1; i += 2) {
				Tap entryTap = taps.get(i);
				Tap exitTap = taps.get(i + 1);

				Station entryStation = Stations.getStationByLabel(entryTap.getStation());
				Station exitStation = Stations.getStationByLabel(exitTap.getStation());

				List<Zone> entryZones = Arrays.stream(entryStation.getZones()).filter(zone -> zone != null && zone.getPosition() != null).collect(Collectors.toList());
				List<Zone> exitZones = Arrays.stream(exitStation.getZones()).filter(zone -> zone != null && zone.getPosition() != null).collect(Collectors.toList());

				long tripPrice;
				Zone from = new Zone(-1);
				Zone to = new Zone(-1);

				int distance = Integer.MAX_VALUE;

				for (Zone entryZone : entryZones) {
					for (Zone exitZone : exitZones) {
						int diff = Math.abs(entryZone.getPosition() - exitZone.getPosition());

						if (diff < distance) {
							distance = diff;
							from = entryZone;
							to = exitZone;
						}
					}
				}

				ZonePair zonePair = findInternalZonePair(from, to);

				tripPrice = zonePair.getPrice();

				Trip trip = Trip.Builder.aTrip()
						.stationStart(entryStation.getLabel())
						.stationEnd(exitStation.getLabel())
						.startedJourneyAt(entryTap.getUnixTimestamp())
						.costInCents(tripPrice)
						.zoneFrom(from.getPosition())
						.zoneTo(to.getPosition())
						.build();

				totalCostInCents += tripPrice;

				customer.getTrips().add(trip);
			}

			customer.setTotalCostInCents(totalCostInCents);

			output.getCustomerSummaries().add(customer);
		}

		return output;
	}

	/**
	 * Find the {@link ZonePair} instance which corresponds to the from and to zones.
	 * @param from start zone
	 * @param to destination zone
	 * @return {@link ZonePair} instance which corresponds to the given zones.
	 */
	private ZonePair findInternalZonePair(Zone from, Zone to) {
		return Zones.getZonePairs().stream().filter(zp -> {
			List<Integer> zonePairList = Arrays.stream(zp.getZones()).map(Zone::getPosition).sorted().collect(Collectors.toList());

			List<Integer> tripPairList = Arrays.asList(from.getPosition(), to.getPosition());
			Collections.sort(tripPairList);

			return zonePairList.equals(tripPairList);
		}).findFirst().orElseThrow();
	}
}
