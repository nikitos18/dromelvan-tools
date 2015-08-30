package org.dromelvan.tools;

import java.io.File;
import java.io.IOException;

import org.dromelvan.tools.parser.javascript.JavaScriptParser;
import org.dromelvan.tools.parser.jsoup.JSoupFileReader;
import org.dromelvan.tools.parser.whoscored.match.WhoScoredMatchEventsJavaScriptVariables;
import org.jsoup.nodes.Document;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JukitoRunner.class)
public class JavaScriptParserTests {

	private final static Logger logger = LoggerFactory.getLogger(JavaScriptParserTests.class);

	public static class Module extends JukitoModule {
		@Override
		protected void configureTest() {
			bindManyInstances(File.class, new File("src/test/resources/Aston Villa-Sunderland.html"));
		}
	}

	@Test
	public void test(@All File file) throws IOException {
		logger.info("Reading file: " + file);

		JSoupFileReader reader = new JSoupFileReader(file);
		Document document = reader.read();

		JavaScriptParser javaScriptParser = new JavaScriptParser();
		WhoScoredMatchEventsJavaScriptVariables whoScoredMatchEventsJavaScriptVariables = new WhoScoredMatchEventsJavaScriptVariables(javaScriptParser.parse(document));

		System.out.println("matchId: " + whoScoredMatchEventsJavaScriptVariables.getMatchId());
		System.out.println("Date: " + whoScoredMatchEventsJavaScriptVariables.getDateTime());
		System.out.println("IncidentEvents:");
		System.out.println(whoScoredMatchEventsJavaScriptVariables.getGoalParserObjects());
//		for(Map map : whoScoredMatchEventsJavaScriptVariables.getIncidentEvents()) {
//		    System.out.println(map.get("eventId") + " " + map.get("minute") + " " + whoScoredMatchEventsJavaScriptVariables.getPlayerIdNameDictionary().get(map.get("playerId")) + " " + map.get("type") + " " + map.get("qualifiers"));
//		    System.out.println();
//		}
	}
}
