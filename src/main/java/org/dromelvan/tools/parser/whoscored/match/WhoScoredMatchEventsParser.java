package org.dromelvan.tools.parser.whoscored.match;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dromelvan.tools.parser.jsoup.JSoupDocumentParser;
import org.dromelvan.tools.parser.match.CardParserObject.CardType;
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
		return parse(new WhoScoredMatchParserObject());
	}

	public Set<MatchParserObject> parse(WhoScoredMatchParserObject whoScoredMatchParserObject) {
		return parseJavaScript(whoScoredMatchParserObject);
	}

	public Set<MatchParserObject> parseJavaScript(WhoScoredMatchParserObject matchParserObject) {
		WhoScoredMatchEventsJavaScriptVariables whoScoredMatchEventsJavaScriptVariables = getJavaScriptVariables();

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

		for (WhoScoredGoalParserObject whoScoredGoalParserObject : whoScoredMatchEventsJavaScriptVariables.getGoalParserObjects()) {
			TeamParserObject teamParserObject = matchParserObject.getTeamForPlayer(whoScoredGoalParserObject.getPlayerWhoScoredId());
			teamParserObject.getGoals().add(whoScoredGoalParserObject);
		}

		for (WhoScoredCardParserObject whoScoredCardParserObject : whoScoredMatchEventsJavaScriptVariables.getCardParserObjects()) {
			TeamParserObject teamParserObject = matchParserObject.getTeamForPlayer(whoScoredCardParserObject.getPlayerWhoScoredId());
			teamParserObject.getCards().add(whoScoredCardParserObject);
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

	public Set<MatchParserObject> parseDom(WhoScoredMatchParserObject matchParserObject) {
		Elements keyIncidentsElements = getDocument().getElementsByClass("match-centre-key-incidents");
		for (Element keyIncidentsElement : keyIncidentsElements) {
			Elements cellElements = keyIncidentsElement.getElementsByTag("td");

			for (Element cellElement : cellElements) {
				// We'll just assume that we won't get more than one goal/assist per minute.
				Element goalElement = cellElement.getElementsByAttributeValue("data-type", "16").first();
				Element assistElement = cellElement.getElementsByAttributeValue("data-type", "1").first();

				List<Element> cardElements = getMatchEventElements(cellElement, "17");
				List<Element> substitutionOffElements = getMatchEventElements(cellElement, "18");
				List<Element> substitutionOnElements = getMatchEventElements(cellElement, "19");

				if (goalElement != null) {
					String player = goalElement.getElementsByClass("player-name").text();

					Element dataElement = goalElement.getElementsByClass("incident-icon").first();
					int playerId = Integer.parseInt(dataElement.attr("data-player-id"));
					int time = Integer.parseInt(dataElement.attr("data-minute")) + 1;
					boolean penalty = dataElement.hasAttr("data-event-satisfier-penaltyscored");
					boolean ownGoal = dataElement.hasAttr("data-event-satisfier-goalown");

					String assistPlayer = "";
					int assistPlayerId = 0;

					if (assistElement != null) {
						assistPlayer = assistElement.getElementsByClass("player-name").text();
						Element assistDataElement = assistElement.getElementsByClass("incident-icon").first();
						assistPlayerId = Integer.parseInt(assistDataElement.attr("data-player-id"));
					}
					WhoScoredGoalParserObject goalParserObject = new WhoScoredGoalParserObject(player, playerId, assistPlayer, assistPlayerId, time, penalty, ownGoal);

					WhoScoredTeamParserObject team = matchParserObject.getTeamForGoal(goalParserObject);
					if (team == null) {
						logger.error("Could not find team for goal {}.", goalParserObject);
					} else {
						team.getGoals().add(goalParserObject);
					}
				}
				if (!cardElements.isEmpty()) {
					for (Element cardElement : cardElements) {
						String player = cardElement.getElementsByClass("player-name").text();
						Element dataElement = cardElement.getElementsByClass("incident-icon").first();

						int playerId = Integer.parseInt(dataElement.attr("data-player-id"));
						int time = Integer.parseInt(dataElement.attr("data-minute")) + 1;
						CardType cardType = (dataElement.hasAttr("data-event-satisfier-yellowcard") ? CardType.YELLOW : CardType.RED);

						WhoScoredCardParserObject cardParserObject = new WhoScoredCardParserObject(player, playerId, time, cardType);
						matchParserObject.getTeamForPlayer(cardParserObject.getPlayerWhoScoredId()).getCards().add(cardParserObject);
					}
				}
				if (!substitutionOffElements.isEmpty()) {
					for (int i = 0; i < substitutionOffElements.size(); ++i) {
						Element substitutionOffElement = substitutionOffElements.get(i);
						Element substitutionOnElement = substitutionOnElements.get(i);

						Element playerOutDataElement = substitutionOffElement.getElementsByClass("incident-icon").first();
						Element playerInDataElement = substitutionOnElement.getElementsByClass("incident-icon").first();

						String playerOut = substitutionOffElement.getElementsByClass("player-name").text();
						int playerOutId = Integer.parseInt(playerOutDataElement.attr("data-player-id"));
						String playerIn = substitutionOnElement.getElementsByClass("player-name").text();
						int playerInId = Integer.parseInt(playerInDataElement.attr("data-player-id"));
						int time = Integer.parseInt(playerOutDataElement.attr("data-minute")) + 1;

						WhoScoredSubstitutionParserObject substitutionParserObject = new WhoScoredSubstitutionParserObject(playerOut, playerOutId, playerIn, playerInId, time);
						matchParserObject.getTeamForPlayer(substitutionParserObject.getPlayerOutWhoScoredId()).getSubstitutions().add(substitutionParserObject);
					}
				}
			}
		}

		getParserProperties().map(matchParserObject);

		Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
		matchParserObjects.add(matchParserObject);
		return matchParserObjects;
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
