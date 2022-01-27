package fr.peritis.trips.pricer;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import fr.peritis.trips.json.Parser;
import fr.peritis.trips.models.Input;
import fr.peritis.trips.models.Output;
import fr.peritis.trips.price.Pricer;
import fr.peritis.trips.stations.Stations;
import fr.peritis.trips.zones.Zones;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PricerTests {

	private static final String OUTPUT_FILE = "src/test/resources/TestOutputFile.txt";

	@Test
	public void givenFileWithValidDataValidateInput() throws FileNotFoundException {
		Input input = inputFromFile("CandidateInputExample.txt");

		Pricer pricer = new Pricer(input.getTaps());

		assertTrue(pricer.isInputValid());
	}

	@Test
	public void givenTripWithEqualEntryExitTimestampExpectError() throws FileNotFoundException {
		Exception exception = validationException("CandidateInputExample_EqualTimestampError.txt");

		assertTrue(exception.getMessage().contains("equal to exit"));
	}

	@Test
	public void givenTripWithWrongSequenceExpectError() throws FileNotFoundException {
		Exception exception = validationException("CandidateInputExample_ExitBeforeEntryError.txt");

		assertTrue(exception.getMessage().contains("greater than exit"));
	}

	@Test
	public void givenTripWithEntryExitSameStationExpectError() throws FileNotFoundException {
		Exception exception = validationException("CandidateInputExample_SameStationEntryExit.txt");

		assertTrue(exception.getMessage().contains("entry and exit"));
	}

	@Test
	public void givenInputWithTapsValidateOutputFileJson() throws IOException {
		Input input = inputFromFile("CandidateInputExample.txt");

		Zones.init();
		Stations.mapStationsToZones();

		Pricer pricer = new Pricer(input.getTaps());

		Output output = pricer.generatePricesForCustomers();

		Parser.toFile(OUTPUT_FILE, output);

		DocumentContext jsonContext = JsonPath.parse(new File(OUTPUT_FILE));
		int customerCount = jsonContext.read("$.customerSummaries.length()");

		int customer1Id = jsonContext.read("$.customerSummaries[0].customerId");
		int customer1TotalCostInCents = jsonContext.read("$.customerSummaries[0].totalCostInCents");
		String customer1Trip1StationStart = jsonContext.read("$.customerSummaries[0].trips[0].stationStart");
		String customer1Trip1StationEnd = jsonContext.read("$.customerSummaries[0].trips[0].stationEnd");
		int customer1Trip1startedJourneyAt = jsonContext.read("$.customerSummaries[0].trips[0].startedJourneyAt");
		int customer1Trip1CostInCents = jsonContext.read("$.customerSummaries[0].trips[0].costInCents");
		int customer1Trip1ZoneFrom = jsonContext.read("$.customerSummaries[0].trips[0].zoneFrom");
		int customer1Trip1ZoneTo = jsonContext.read("$.customerSummaries[0].trips[0].zoneTo");

		assertEquals(1, customer1Id);
		assertEquals(240, customer1TotalCostInCents);
		assertEquals("A", customer1Trip1StationStart);
		assertEquals("D", customer1Trip1StationEnd);
		assertEquals(1, customer1Trip1startedJourneyAt);
		assertEquals(240, customer1Trip1CostInCents);
		assertEquals(1, customer1Trip1ZoneFrom);
		assertEquals(2, customer1Trip1ZoneTo);

		assertEquals(4, customerCount);
	}

	@AfterAll
	static void deleteOutputFile() throws IOException {
		Files.delete(Paths.get(OUTPUT_FILE));
	}

	private Exception validationException(String fileName) throws FileNotFoundException {
		Input input = inputFromFile(fileName);

		return assertThrows(IllegalArgumentException.class, () -> new Pricer(input.getTaps()).isInputValid());
	}

	private Input inputFromFile(String fileName) throws FileNotFoundException {
		ClassLoader classLoader = getClass().getClassLoader();

		return Parser.fromFile(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
	}
}
