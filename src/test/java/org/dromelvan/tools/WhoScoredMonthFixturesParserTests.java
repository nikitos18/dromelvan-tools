package org.dromelvan.tools;

import java.io.File;
import java.io.IOException;

import org.dromelvan.tools.parser.jsoup.JSoupFileReader;
import org.dromelvan.tools.parser.whoscored.fixtures.WhoScoredMonthFixturesParser;
import org.jsoup.nodes.Document;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JukitoRunner.class)
public class WhoScoredMonthFixturesParserTests {

	private final static Logger logger = LoggerFactory.getLogger(WhoScoredMonthFixturesParserTests.class);

	public static class Module extends JukitoModule {
		@Override
		protected void configureTest() {
			bindManyInstances(File.class, new File("src/test/resources/whoscored-month-fixtures-1508.html"));
		}
	}

	@Test
	public void parseMonthFixturesFile(@All File monthFixturesFile, WhoScoredMonthFixturesParser whoScoredMonthFixturesParser) throws IOException {
		logger.info("Parsing {}...", monthFixturesFile);

		JSoupFileReader reader = new JSoupFileReader(monthFixturesFile);
		Document document = reader.read();

		whoScoredMonthFixturesParser.setDocument(document);
		whoScoredMonthFixturesParser.parse();
	}
}
