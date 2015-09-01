package org.dromelvan.tools.parser.whoscored.fixtures;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.dromelvan.tools.parser.javascript.JavaScriptVariables;
import org.dromelvan.tools.parser.jsoup.JSoupDocumentParser;
import org.dromelvan.tools.parser.whoscored.MatchDayParserObject;
import org.dromelvan.tools.parser.whoscored.MatchParserObject;
import org.dromelvan.tools.parser.whoscored.SeasonParserObject;
import org.dromelvan.tools.parser.whoscored.WhoScoredProperties;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class WhoScoredFixturesParser extends JSoupDocumentParser<SeasonParserObject, JavaScriptVariables> {

	private final static Logger logger = LoggerFactory.getLogger(WhoScoredFixturesParser.class);

	@Inject
	public WhoScoredFixturesParser(WhoScoredProperties whoScoredProperties) {
		super(whoScoredProperties);
	}

	@Override
	public Set<SeasonParserObject> parse() throws IOException {
		SeasonParserObject seasonParserObject = new SeasonParserObject();
		MatchDayParserObject matchDayParserObject = null;
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("E, MMM dd yyyy");

		Elements tableElements = getDocument().select("table.fixture");

		for (Element tableElement : tableElements) {
			Elements tableRowElements = tableElement.getElementsByTag("tr");

			LocalDate localDate = null;

			for (int i = 0; i < tableRowElements.size(); ++i) {
				Element tableRowElement = tableRowElements.get(i);

				if (tableRowElement.hasClass("rowgroupheader")) {
					localDate = LocalDate.parse(tableRowElement.text(), dateTimeFormatter);
					if (matchDayParserObject == null || matchDayParserObject.getMatchParserObjects().size() >= 10) {
						matchDayParserObject = new MatchDayParserObject();
						matchDayParserObject.setLocalDate(localDate);
						seasonParserObject.getMatchDayParserObjects().add(matchDayParserObject);
						matchDayParserObject.setMatchDayNumber(seasonParserObject.getMatchDayParserObjects().size());
						logger.debug("Added match day for date {}.", matchDayParserObject.getLocalDate());
					}
				} else if (tableRowElement.hasClass("item")) {
					MatchParserObject matchParserObject = new MatchParserObject();
					matchParserObject.setWhoScoredId(tableRowElement.attr("data-id"));
					matchParserObject.setMatchDayNumber(matchDayParserObject.getMatchDayNumber());
					matchParserObject.setLocalDate(localDate);
					matchParserObject.setTime(tableRowElement.select("td.time").first().text());
					matchParserObject.setWhoScoredHomeTeamId(Integer.parseInt(tableRowElement.select("td.team.home").first().attr("data-id")));
					matchParserObject.setHomeTeamName(tableRowElement.select("td.team.home").first().text());
					matchParserObject.setHomeTeamId(Integer.parseInt(getParserProperties().getProperty(matchParserObject.getHomeTeamName().replace(' ', '_'))));
					matchParserObject.setWhoScoredAwayTeamId(Integer.parseInt(tableRowElement.select("td.team.away").first().attr("data-id")));
					matchParserObject.setAwayTeamName(tableRowElement.select("td.team.away").first().text());
					matchParserObject.setAwayTeamId(Integer.parseInt(getParserProperties().getProperty(matchParserObject.getAwayTeamName().replace(' ', '_'))));

					matchDayParserObject.getMatchParserObjects().add(matchParserObject);
					logger.debug(matchParserObject.toString());
				}
			}
		}
		Set<SeasonParserObject> result = new HashSet<SeasonParserObject>();
		result.add(seasonParserObject);
		return result;
	}

}
