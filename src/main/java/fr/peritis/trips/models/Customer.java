package fr.peritis.trips.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * POJO representation of the JSON path that encompasses 'customerId', 'totalCostInCents' and 'trips'.
 */
public class Customer {
	/**
	 * represents JSON path 'customerId'
	 */
	private final int customerId;

	/**
	 * represents JSON path 'totalCostInCents'
	 */
	private long totalCostInCents;

	/**
	 * represents JSON path 'trips'
	 */
	private final List<Trip> trips = new ArrayList<>();

	public Customer(int customerId) {
		this.customerId = customerId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public long getTotalCostInCents() {
		return totalCostInCents;
	}

	public void setTotalCostInCents(long totalCostInCents) {
		this.totalCostInCents = totalCostInCents;
	}

	public List<Trip> getTrips() {
		return trips;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Customer customer = (Customer) o;
		return getCustomerId() == customer.getCustomerId() && getTotalCostInCents() == customer.getTotalCostInCents() && getTrips().equals(customer.getTrips());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCustomerId(), getTotalCostInCents(), getTrips());
	}
}
