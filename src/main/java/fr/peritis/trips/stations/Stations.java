package fr.peritis.trips.stations;

import fr.peritis.trips.zones.Zones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Contains logic to initialize stations and associate them with their respective zones, as detailed
 * in the requirement document.
 */
public class Stations {

	/**
	 * Container for initialized stations.
	 */
	private static final List<Station> stations = new ArrayList<>();

	/**
	 * Populates the internal list of stations and corresponding zone mappings.
	 */
	public static void mapStationsToZones() {
		Map<String, int[]> stationToZoneMapping = new HashMap<>();
		stationToZoneMapping.put("A", new int[]{1});
		stationToZoneMapping.put("B", new int[]{1});
		stationToZoneMapping.put("C", new int[]{2, 3});
		stationToZoneMapping.put("D", new int[]{2});
		stationToZoneMapping.put("E", new int[]{2, 3});
		stationToZoneMapping.put("F", new int[]{3, 4});
		stationToZoneMapping.put("G", new int[]{4});
		stationToZoneMapping.put("H", new int[]{4});
		stationToZoneMapping.put("I", new int[]{4});

		stationToZoneMapping.forEach(
				(s, z) -> {
					Station station = new Station(s);
					for (int i = 0; i < z.length; i++) {
						station.getZones()[i] = Zones.getZoneByPosition(z[i]);
					}
					stations.add(station);
				});
	}

	/**
	 * Retrieves station instance corresponding to given label.
	 * @param label unique identifier for station
	 * @return instance of station corresponding to given label. If not found, {@link NoSuchElementException} is thrown.
	 */
	public static Station getStationByLabel(String label) {
		return stations.stream().filter(station -> station.getLabel().equals(label)).findFirst().orElseThrow();
	}
}
