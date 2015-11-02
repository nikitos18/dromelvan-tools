package org.dromelvan.tools;

import java.io.File;
import java.io.IOException;

import org.dromelvan.tools.parser.javascript.JavaScriptParser;
import org.dromelvan.tools.parser.jsoup.JSoupFileReader;
import org.dromelvan.tools.parser.whoscored.match.WhoScoredMatchJavaScriptVariables;
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
			bindManyInstances(File.class, new File("src/test/resources/Liverpool-West Ham.html"));
		}
	}

	@Test
	public void test(@All File file, JavaScriptParser<WhoScoredMatchJavaScriptVariables> javaScriptParser) throws IOException {
		logger.info("Reading file: " + file);

		JSoupFileReader reader = new JSoupFileReader(file);
		Document document = reader.read();

		WhoScoredMatchJavaScriptVariables whoScoredMatchEventsJavaScriptVariables = javaScriptParser.parse(document);
		System.out.println(whoScoredMatchEventsJavaScriptVariables);
	}
}
