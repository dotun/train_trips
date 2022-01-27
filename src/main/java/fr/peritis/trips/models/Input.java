package fr.peritis.trips.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Type representation of the JSON path that encompasses 'taps'.
 */
public class Input {
	/**
	 * represents JSON path 'taps'
	 */
	private final List<Tap> taps = new ArrayList<>();

	public List<Tap> getTaps() {
		return taps;
	}
}
