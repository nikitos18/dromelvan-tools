package org.dromelvan.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.dromelvan.tools.parser.JSoupFileReader;
import org.dromelvan.tools.parser.JSoupURLReader;
import org.dromelvan.tools.parser.PlayerInformationParserObject;
import org.dromelvan.tools.parser.whoscored.WhoScoredPlayerPageParser;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JukitoRunner.class)
public class WhoScoredPlayerPageParserTests {

	private final static Logger logger = LoggerFactory.getLogger(WhoScoredPlayerPageParserTests.class);

	public static class Module extends JukitoModule {
		@Override
		protected void configureTest() {
			try {
				bindManyInstances(URL.class, new URL("http://www.whoscored.com/Players/22221/"),
						new URL("http://www.whoscored.com/Players/22222/"),
						new URL("http://www.whoscored.com/Players/22223/"));
				bindManyInstances(File.class, new File("src/test/resources/Luis Suarez Football Statistics   WhoScored.com.htm"));
			} catch (MalformedURLException e) {
			}
		}
	}

	// @Test
	public void fileTest(@All File file, WhoScoredPlayerPageParser parser) throws IOException {
		JSoupFileReader jSoupFileReader = new JSoupFileReader(file);
		parser.setDocument(jSoupFileReader.read());
		Set<PlayerInformationParserObject> playerInformationParserObjects = parser.parse();

		logger.info("Information for {}:", file.getName());
		for (PlayerInformationParserObject playerInformationParserObject : playerInformationParserObjects) {
			logger.info("{}", playerInformationParserObject);
		}
	}

	// @Test
	public void urlTest(@All URL url, WhoScoredPlayerPageParser parser) throws IOException {
		JSoupURLReader jSoupFileReader = new JSoupURLReader(url);
		parser.setDocument(jSoupFileReader.read());
		Set<PlayerInformationParserObject> playerInformationParserObjects = parser.parse();

		logger.info("Information for {}:", url);
		for (PlayerInformationParserObject playerInformationParserObject : playerInformationParserObjects) {
			logger.info("{}", playerInformationParserObject);
		}
	}

	@Test
	public void createFile(WhoScoredPlayerPageParser parser) throws IOException {
		File output = new File("players.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		File failed = new File("failed.txt");
		BufferedWriter failedWriter = new BufferedWriter(new FileWriter(failed));

		for (int i = 1; i < 50; ++i) {
			URL url = new URL("http://www.whoscored.com/Players/" + i + "/");

			try {
				JSoupURLReader jSoupFileReader = new JSoupURLReader(url);
				parser.setDocument(jSoupFileReader.read());
				Set<PlayerInformationParserObject> playerInformationParserObjects = parser.parse();

				logger.info("Information for {}:", url);
				for (PlayerInformationParserObject playerInformationParserObject : playerInformationParserObjects) {
					logger.info("{} {}", i, playerInformationParserObject);
					writer.write(playerInformationParserObject.getName() + ";" +
							playerInformationParserObject.getNationality() + ";" +
							playerInformationParserObject.getDateOfBirth() + ";" +
							playerInformationParserObject.getHeight() + ";" +
							playerInformationParserObject.getWeight() + ";" +
							playerInformationParserObject.getShirtNumber() + ";" +
							playerInformationParserObject.getPositions() + "\n");
				}
			} catch (Exception e) {
				logger.error("Failed at {}:", url);
				logger.error("Exception:", e);
				failedWriter.write(url + "\n");
			}
		}

		writer.flush();
		writer.close();
		failedWriter.flush();
		failedWriter.close();
	}
}
