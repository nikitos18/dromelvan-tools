package org.dromelvan.tools.parser.old;

import java.io.IOException;
import java.util.Set;

import org.dromelvan.tools.parser.ParserProperties;
import org.dromelvan.tools.parser.match.MatchParserObject;

public abstract class MatchStatisticsHTMLFileParser extends HTMLFileParser<MatchParserObject> implements MatchStatisticsParser {

	public MatchStatisticsHTMLFileParser(ParserProperties parserProperties) {
		super(parserProperties);
	}

	@Override
	protected Set<MatchParserObject> parseDocument() throws IOException {
		Set<MatchParserObject> matchParserObjects = parseMatches();
		postParseDocument(matchParserObjects);
		return matchParserObjects;
	}

	protected void postParseDocument(Set<MatchParserObject> matchParserObjects) throws IOException {
		for (MatchParserObject matchParserObject : matchParserObjects) {
			getParserProperties().map(matchParserObject);
		}
	}

	protected abstract Set<MatchParserObject> parseMatches();

	public MatchParserObject getMatchParserObject(String homeTeam, String awayTeam) {
		for (MatchParserObject matchParserObject : getParserObjects()) {
			if (matchParserObject.getHomeTeam().getName().equalsIgnoreCase(homeTeam)
					&& matchParserObject.getAwayTeam().getName().equalsIgnoreCase(awayTeam)) {
				return matchParserObject;
			}
		}
		return null;
	}

}
