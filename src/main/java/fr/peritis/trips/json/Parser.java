package fr.peritis.trips.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import fr.peritis.trips.models.Input;
import fr.peritis.trips.models.Output;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Utility class for reading and writing JSON.
 */
public class Parser {

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Reads the content of file located at the given filePath and attempts to convert it to an object
	 * of type {@link fr.peritis.trips.models.Input}.
	 * @param filePath path to input file
	 * @return object of type {@link fr.peritis.trips.models.Input}
	 * @throws FileNotFoundException if file at given path cannot be accessed as a file
	 */
	public static Input fromFile(String filePath) throws FileNotFoundException {
		JsonReader reader = new JsonReader(new FileReader(filePath));
		return gson.fromJson(reader, Input.class);
	}

	/**
	 * Attempts to write the content of the given {@link fr.peritis.trips.models.Output} object in a corresponding JSON
	 * format to a file at the given file path.
	 * @param filePath file path to the output file
	 * @param output object to be converted to JSON format.
	 * @throws IOException
	 */
	public static void toFile(String filePath, Output output) throws IOException {
		Writer writer = new FileWriter(filePath);
		gson.toJson(output, writer);
		writer.flush();
		writer.close();
	}

}
