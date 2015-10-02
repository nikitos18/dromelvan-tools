package org.dromelvan.tools.parser.whoscored.match;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dromelvan.tools.parser.jsoup.JSoupDocumentParser;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.dromelvan.tools.parser.match.PlayerParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;
import org.dromelvan.tools.parser.whoscored.WhoScoredProperties;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class WhoScoredMatchEventsParser extends JSoupDocumentParser<MatchParserObject, WhoScoredMatchEventsJavaScriptVariables> {

	private final static Logger logger = LoggerFactory.getLogger(WhoScoredMatchEventsParser.class);

	@Inject
	public WhoScoredMatchEventsParser(WhoScoredProperties whoScoredProperties) {
		super(whoScoredProperties);
	}

	@Override
	public Set<MatchParserObject> parse() {
		return parseJavaScript();
	}

	private Set<MatchParserObject> parseJavaScript() {
		WhoScoredMatchEventsJavaScriptVariables whoScoredMatchEventsJavaScriptVariables = getJavaScriptVariables();

		WhoScoredMatchParserObject matchParserObject = whoScoredMatchEventsJavaScriptVariables.getMatchParserObject();

		Map<Integer, Integer> ratingsMap = whoScoredMatchEventsJavaScriptVariables.getRatings();
		for (int whoScoredId : ratingsMap.keySet()) {
			int rating = ratingsMap.get(whoScoredId);
			WhoScoredTeamParserObject whoScoredTeamParserObject = matchParserObject.getTeamForPlayer(whoScoredId);
			PlayerParserObject playerParserObject = whoScoredTeamParserObject.getPlayer(whoScoredId);
			if (rating != playerParserObject.getRating()) {
				logger.info(" Changing rating for {} from {} to {}", playerParserObject.getName(), playerParserObject.getRating(), rating);
				playerParserObject.setRating(rating);
			}
		}

		for (WhoScoredSubstitutionParserObject whoScoredSubstitutionParserObject : whoScoredMatchEventsJavaScriptVariables.getSubstitutionParserObjects()) {
			TeamParserObject teamParserObject = matchParserObject.getTeamForPlayer(whoScoredSubstitutionParserObject.getPlayerOutWhoScoredId());
			teamParserObject.getSubstitutions().add(whoScoredSubstitutionParserObject);
		}

		getParserProperties().map(matchParserObject);

		Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
		matchParserObjects.add(matchParserObject);
		return matchParserObjects;
	}

	public URL getPlayerStatsURL() {
		Elements aElements = getDocument().getElementsByTag("a");
		for (Element aElement : aElements) {
			if (aElement.text() != null
					&& aElement.text().equalsIgnoreCase("player statistics")) {
				String urlString = "http://www.whoscored.com" + aElement.attr("href");
				try {
					URL url = new URL(urlString);
					return url;
				} catch (MalformedURLException e) {
					logger.error("Malformed URL exception when getting player stats URL: {}.", urlString);
				}
			}
		}
		return null;
	}

	private List<Element> getMatchEventElements(Element cellElement, String dataType) {
		List<Element> matchEventElements = new ArrayList<Element>();
		Elements keyIncidentElements = cellElement.getElementsByAttributeValue("data-type", dataType);
		for (Element keyIncidentElement : keyIncidentElements) {
			matchEventElements.addAll(keyIncidentElement.getElementsByClass("match-centre-header-team-key-incident"));
		}
		return matchEventElements;
	}
}
