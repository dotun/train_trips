package fr.peritis.trips;

import fr.peritis.trips.json.Parser;
import fr.peritis.trips.models.Input;
import fr.peritis.trips.models.Output;
import fr.peritis.trips.price.Pricer;
import fr.peritis.trips.stations.Stations;
import fr.peritis.trips.zones.Zones;

import java.io.IOException;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws IOException {
		String inputFilePath, outputFilePath;

		if (args.length < 2) {
			Scanner scanner = new Scanner(System.in);

			System.out.println("Enter file path for input data...");
			inputFilePath = scanner.nextLine();

			System.out.println("Enter file path for result output...");
			outputFilePath = scanner.nextLine();
		} else {
			inputFilePath = args[0];
			outputFilePath = args[1];
		}

		Zones.init();
		Stations.mapStationsToZones();

		Input input = Parser.fromFile(inputFilePath);
		Pricer pricer = new Pricer(input.getTaps());
		Output output = pricer.generatePricesForCustomers();

		Parser.toFile(outputFilePath, output);
	}
}
