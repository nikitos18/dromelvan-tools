package org.dromelvan.tools.parser.whoscored;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.dromelvan.tools.parser.Parser;
import org.dromelvan.tools.parser.jsoup.JSoupDocumentReader;
import org.dromelvan.tools.parser.jsoup.JSoupFileReader;
import org.dromelvan.tools.parser.jsoup.JSoupURLReader;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhoScoredMatchParser implements Parser {

	private final WhoScoredMatchEventsParser whoScoredMatchEventsParser;
	private final WhoScoredPlayerStatsParser whoScoredPlayerStatsParser;
	private JSoupDocumentReader matchEventsReader;
	private JSoupDocumentReader playerStatsReader;
	private final static Logger logger = LoggerFactory.getLogger(WhoScoredMatchParser.class);

	public WhoScoredMatchParser(WhoScoredMatchEventsParser whoScoredMatchEventsParser, WhoScoredPlayerStatsParser whoScoredPlayerStatsParser) {
		this.whoScoredMatchEventsParser = whoScoredMatchEventsParser;
		this.whoScoredPlayerStatsParser = whoScoredPlayerStatsParser;
	}

	public WhoScoredMatchParser(WhoScoredMatchEventsParser whoScoredMatchEventsParser, WhoScoredPlayerStatsParser whoScoredPlayerStatsParser, URL url) throws MalformedURLException {
		this(whoScoredMatchEventsParser, whoScoredPlayerStatsParser);
		this.matchEventsReader = new JSoupURLReader(url);

		URL playerStatsUrl = new URL(url.toString().replace("Live", "LiveStatistics"));
		this.playerStatsReader = new JSoupURLReader(playerStatsUrl);
	}

	public WhoScoredMatchParser(WhoScoredMatchEventsParser whoScoredMatchEventsParser, WhoScoredPlayerStatsParser whoScoredPlayerStatsParser, File file) {
		this(whoScoredMatchEventsParser, whoScoredPlayerStatsParser);
		this.matchEventsReader = new JSoupFileReader(file);

		File playerStatsFile = new File(file.getParent(), file.getName().replace("Live.htm", "- Live Statistics.htm"));
		this.playerStatsReader = new JSoupFileReader(playerStatsFile);
	}

	@Override
	public Set parse() throws IOException {
		Document document = matchEventsReader.read();
		whoScoredMatchEventsParser.setDocument(document);

		try {
			document = playerStatsReader.read();
		} catch (FileNotFoundException e) {
			logger.info("Fetching player stats file from URL: {}.", whoScoredMatchEventsParser.getPlayerStatsURL());
			JSoupURLReader jSoupURLReader = new JSoupURLReader(whoScoredMatchEventsParser.getPlayerStatsURL());
			document = jSoupURLReader.read();

			String fileName = e.getMessage().substring(0, e.getMessage().indexOf("(The system cannot find the file specified)")).trim();
			File file = new File(fileName);
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.write(document.html());
			writer.flush();
			writer.close();
		}
		whoScoredPlayerStatsParser.setDocument(document);

		Set<MatchParserObject> matchParserObjects = whoScoredPlayerStatsParser.parse();
		matchParserObjects = whoScoredMatchEventsParser.parse((WhoScoredMatchParserObject) matchParserObjects.iterator().next());
		return matchParserObjects;
	}

}
