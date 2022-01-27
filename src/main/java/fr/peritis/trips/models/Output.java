package fr.peritis.trips.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Type representation of the JSON path that encompasses 'customerSummaries'.
 */
public class Output {

	/**
	 * represents JSON path 'customerSummaries'
	 */
	private final List<Customer> customerSummaries = new ArrayList<>();

	public List<Customer> getCustomerSummaries() {
		return customerSummaries;
	}
}
