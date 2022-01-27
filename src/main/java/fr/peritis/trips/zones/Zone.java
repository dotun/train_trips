package fr.peritis.trips.zones;

import java.util.Objects;

/**
 * Zone type consists of a position as indicated in requirement docs.
 */
public class Zone implements Comparable<Zone> {

	private final Integer position;

	public Zone(Integer position) {
		this.position = position;
	}

	public Integer getPosition() {
		return position;
	}


	@Override public boolean equals( Object o ) {
		if ( this == o )
			return true;
		if ( o == null || getClass() != o.getClass() )
			return false;
		Zone zone = (Zone) o;
		return getPosition().equals( zone.getPosition() );
	}



	@Override public int hashCode() {
		return Objects.hash( getPosition() );
	}

	@Override
	public int compareTo(Zone otherZone) {
		return this.position.compareTo(otherZone.getPosition());
	}
}
