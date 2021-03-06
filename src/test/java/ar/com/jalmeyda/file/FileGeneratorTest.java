package ar.com.jalmeyda.file;

import ar.com.jalmeyda.model.Location;
import ar.com.jalmeyda.model.builder.LocationBuilder;
import org.junit.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Juan Almeyda on 9/4/2016.
 */
public class FileGeneratorTest {

	@Test
	public void testLineGeneration() {
		// GIVEN
		Location location = getDefaultLocation();

		// WHEN
		FileLine fileLine = new FileLine(location);

		// THEN
		Assert.assertEquals("377078,Potsdam,location,52.39886,13.06566", fileLine.generateLine());
	}

	@Test
	public void testOneLineFileGeneration() throws Exception {
		// GIVEN
		String cityName = "Berlin";
		Location location = getDefaultLocation();
		List<Location> locations = Arrays.asList(location);

		// WHEN
		new FileGenerator().generateFile(cityName, locations);

		// THEN
		BufferedReader reader = new BufferedReader(new FileReader(new File(cityName + ".csv")));
		Assert.assertEquals(new FileLine(location).generateLine(), reader.readLine());
		reader.close();
	}

	@Test
	public void testManyLinesFileGeneration() throws Exception {
		// GIVEN
		String cityName = "Hamburgo";
		Location location1 = getDefaultLocation();
		Location location2 = getAnotherLocation();
		List<Location> locations = Arrays.asList(location1, location2);

		// WHEN
		new FileGenerator().generateFile(cityName, locations);

		// THEN
		BufferedReader reader = new BufferedReader(new FileReader(new File(cityName + ".csv")));
		Assert.assertEquals(new FileLine(location1).generateLine(), reader.readLine());
		Assert.assertEquals(new FileLine(location2).generateLine(), reader.readLine());
		reader.close();
	}

	@Test
	public void testNoLocationsThenEmptyFile() throws Exception {
		// GIVEN
		String cityName = "NonExistent";
		List<Location> locations = new LinkedList<>();

		// WHEN
		new FileGenerator().generateFile(cityName, locations);

		// THEN
		BufferedReader reader = new BufferedReader(new FileReader(new File(cityName + ".csv")));
		Assert.assertEquals("", reader.readLine());
		reader.close();
	}

	@After
	public void deleteGeneratedFiles() throws Exception {
		final File folder = new File(".");
		final File[] files = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(
					final File dir,
					final String name) {
				return name.matches(".*\\.csv");
			}
		});
		for (final File file : files) {
			if (!file.delete()) {
				System.err.println("Can't remove " + file.getAbsolutePath());
			}
		}
	}

	private Location getAnotherLocation() {
		return new LocationBuilder()
				.with_id(410978L)
				.withName("Potsdam")
				.withType("location")
				.withLatitude(new BigDecimal("44.66978"))
				.withLongitude(new BigDecimal("-74.98131"))
				.build();
	}

	private Location getDefaultLocation() {
		return new LocationBuilder()
				.with_id(377078L)
				.withName("Potsdam")
				.withType("location")
				.withLatitude(new BigDecimal("52.39886"))
				.withLongitude(new BigDecimal("13.06566"))
				.build();
	}
}
