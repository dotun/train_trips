package fr.peritis.trips.parser;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import fr.peritis.trips.json.Parser;
import fr.peritis.trips.models.Input;
import fr.peritis.trips.models.Output;
import fr.peritis.trips.price.Pricer;
import fr.peritis.trips.stations.Stations;
import fr.peritis.trips.zones.Zones;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ParserTests {

	private static final String OUTPUT_FILE = "src/test/resources/TestOutputFile1.txt";

	@Test
	public void givenValidFileThenTapsNotEmpty() throws FileNotFoundException {
		String inputFileName = "CandidateInputExample.txt";
		ClassLoader classLoader = getClass().getClassLoader();

		Input input = Parser.fromFile(Objects.requireNonNull(classLoader.getResource(inputFileName)).getFile());

		assertFalse(input.getTaps().isEmpty());
	}

	@Test
	public void givenOutputFileThenVerifyOutputFile() throws IOException {
		String inputFileName = "CandidateInputExample.txt";
		ClassLoader classLoader = getClass().getClassLoader();

		Input input = Parser.fromFile(Objects.requireNonNull(classLoader.getResource(inputFileName)).getFile());

		Zones.init();
		Stations.mapStationsToZones();

		Pricer pricer = new Pricer(input.getTaps());

		Output output = pricer.generatePricesForCustomers();

		Parser.toFile(OUTPUT_FILE, output);

		DocumentContext jsonContext = JsonPath.parse(new File(OUTPUT_FILE));
		int customerCount = jsonContext.read("$.customerSummaries.length()");

		assertEquals(4, customerCount);
	}

	@AfterAll
	static void deleteOutputFile() throws IOException {
		Files.delete(Paths.get(OUTPUT_FILE));
	}

}
