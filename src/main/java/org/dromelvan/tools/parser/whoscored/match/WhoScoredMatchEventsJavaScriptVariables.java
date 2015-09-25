package org.dromelvan.tools.parser.whoscored.match;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dromelvan.tools.parser.javascript.JavaScriptVariables;
import org.dromelvan.tools.parser.match.CardParserObject.CardType;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class WhoScoredMatchEventsJavaScriptVariables extends JavaScriptVariables {

	/**
	 *
	 */
	private static final long serialVersionUID = 2635365858846187843L;
	public final static String MATCH_ID = "matchId";
	public final static String MATCH_CENTRE_DATA = "matchCentreData";
	public final static String QUALIFIERS = "qualifiers";
	public final static String START_TIME = "startTime";

	public final static int TYPE_ASSIST = 1;
	public final static int TYPE_PENALTY = 9;
	public final static int TYPE_GOAL = 16;
	public final static int TYPE_CARD = 17;
	public final static int TYPE_SUBSTITUTION_OFF = 18;
	public final static int TYPE_SUBSTITUTION_ON = 19;
	public final static int TYPE_OWN_GOAL = 28;
	public final static int TYPE_CARD_YELLOW = 31;
	public final static int TYPE_CARD_RED = 33;

	private Map<Integer, String> playerIdNameDictionary = new HashMap<Integer, String>();
	private Map<Integer, Map<Integer, Map>> incidentEvents = new HashMap<Integer, Map<Integer, Map>>();

	@Override
	public void init() {
		List<String> playerNames = (List<String>) getMatchCentreData().get("playerIdNameDictionary");
		for (int i = 0; i < playerNames.size(); ++i) {
			String playerName = playerNames.get(i);
			if (playerName != null) {
				playerIdNameDictionary.put(i, playerName);
			}
		}

		List<Map> incidentEventList = new ArrayList<Map>();
		Map home = (Map) getMatchCentreData().get("home");
		Map away = (Map) getMatchCentreData().get("away");

		incidentEventList.addAll((List<Map>) home.get("incidentEvents"));
		incidentEventList.addAll((List<Map>) away.get("incidentEvents"));

		for (Map incidentEvent : incidentEventList) {
			int teamId = (Integer) incidentEvent.get("teamId");
			Map<Integer, Map> teamEvents = incidentEvents.get(teamId);
			if (teamEvents == null) {
				teamEvents = new HashMap<Integer, Map>();
				incidentEvents.put(teamId, teamEvents);
			}

			int eventId = (Integer) incidentEvent.get("eventId");
			teamEvents.put(eventId, incidentEvent);
		}
	}

	public int getMatchId() {
		return (Integer) get(MATCH_ID);
	}

	public DateTime getDateTime() {
		DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("MM/dd/YYYY HH:mm:ss");
		return DateTime.parse((String) getMatchCentreData().get(START_TIME), dateTimeFormat);
	}

	public Map<Integer, String> getPlayerIdNameDictionary() {
		return playerIdNameDictionary;
	}

	public Map<Integer, Integer> getRatings() {
		Map<Integer, Integer> ratingMap = new HashMap<Integer, Integer>();

		Map home = (Map) getMatchCentreData().get("home");
		Map away = (Map) getMatchCentreData().get("away");

		List<Map> players = (List<Map>) home.get("players");
		players.addAll((List<Map>) away.get("players"));

		for (Map player : players) {
			int playerId = (int) player.get("playerId");
			Map<String, Object> stats = (Map<String, Object>) player.get("stats");
			if (stats != null) {
				List<Double> ratings = (List<Double>) stats.get("ratings");
				if (ratings != null) {
					BigDecimal rating = BigDecimal.valueOf(ratings.get(ratings.size() - 1));
					rating = rating.movePointRight(2);
					ratingMap.put(playerId, rating.intValue());
				}
			}
		}

		return ratingMap;
	}

	public List<WhoScoredGoalParserObject> getGoalParserObjects() {
		List<WhoScoredGoalParserObject> whoScoredGoalParserObjects = new ArrayList<WhoScoredGoalParserObject>();

		for (Map goalEvent : getIncidentEventsByType(TYPE_GOAL)) {
			int playerWhoScoredId = (int) goalEvent.get("playerId");
			String player = playerIdNameDictionary.get(playerWhoScoredId);
			int time = (int) goalEvent.get("minute") + 1;
			boolean ownGoal = (goalEvent.get("isOwnGoal") == null ? false : (boolean) goalEvent.get("isOwnGoal"));

			boolean penalty = false;
			List<Map> qualifiers = (List<Map>) goalEvent.get("qualifiers");
			for (Map qualifier : qualifiers) {
				Map typeMap = (Map) qualifier.get("type");
				int value = (int) typeMap.get("value");
				if (value == TYPE_PENALTY) {
					penalty = true;
					break;
				}
			}

			int assistPlayerWhoScoredId = 0;
			String assistPlayer = "";
			Map assistEvent = incidentEvents.get(goalEvent.get("teamId")).get(goalEvent.get("relatedEventId"));
			if (assistEvent != null) {
				assistPlayerWhoScoredId = (int) assistEvent.get("playerId");
				assistPlayer = playerIdNameDictionary.get(assistPlayerWhoScoredId);
			}

			WhoScoredGoalParserObject whoScoredGoalParserObject = new WhoScoredGoalParserObject(player, playerWhoScoredId, assistPlayer, assistPlayerWhoScoredId, time, penalty, ownGoal);
			whoScoredGoalParserObjects.add(whoScoredGoalParserObject);
		}

		return whoScoredGoalParserObjects;
	}

	public List<WhoScoredCardParserObject> getCardParserObjects() {
		List<WhoScoredCardParserObject> whoScoredCardParserObjects = new ArrayList<WhoScoredCardParserObject>();

		for (Map cardEvent : getIncidentEventsByType(TYPE_CARD)) {
			int playerWhoScoredId = (int) cardEvent.get("playerId");
			String player = playerIdNameDictionary.get(playerWhoScoredId);
			int time = (int) cardEvent.get("minute") + 1;
			CardType cardType = ((int) ((Map) cardEvent.get("cardType")).get("value") == TYPE_CARD_YELLOW ? CardType.YELLOW : CardType.RED);

			WhoScoredCardParserObject whoScoredCardParserObject = new WhoScoredCardParserObject(player, playerWhoScoredId, time, cardType);
			whoScoredCardParserObjects.add(whoScoredCardParserObject);
		}
		return whoScoredCardParserObjects;
	}

	public List<WhoScoredSubstitutionParserObject> getSubstitutionParserObjects() {
		List<WhoScoredSubstitutionParserObject> whoScoredSubstitutionParserObjects = new ArrayList<WhoScoredSubstitutionParserObject>();

		for (Map substitutionEvent : getIncidentEventsByType(TYPE_SUBSTITUTION_OFF)) {
			int playerOutWhoScoredId = (int) substitutionEvent.get("playerId");
			String playerOut = playerIdNameDictionary.get(playerOutWhoScoredId);
			int time = (int) substitutionEvent.get("minute") + 1;

			Map substitutionInEvent = incidentEvents.get(substitutionEvent.get("teamId")).get(substitutionEvent.get("relatedEventId"));
			int playerInWhoScoredId = (int) substitutionInEvent.get("playerId");
			String playerIn = playerIdNameDictionary.get(playerInWhoScoredId);

			WhoScoredSubstitutionParserObject whoScoredSubstitutionParserObject = new WhoScoredSubstitutionParserObject(playerOut, playerOutWhoScoredId, playerIn, playerInWhoScoredId, time);
			whoScoredSubstitutionParserObjects.add(whoScoredSubstitutionParserObject);
		}
		return whoScoredSubstitutionParserObjects;
	}

	private Map getMatchCentreData() {
		return (Map) get(MATCH_CENTRE_DATA);
	}

	private List<Map> getIncidentEventsByType(int type) {
		List<Map> incidentEventsByType = new ArrayList<Map>();
		List<Map> allIncidentEvents = new ArrayList<Map>();

		for (Map teamIncidentEvents : incidentEvents.values()) {
			allIncidentEvents.addAll(teamIncidentEvents.values());
		}

		for (Map incidentEvent : allIncidentEvents) {
			Map typeMap = (Map) incidentEvent.get("type");
			if (type == (int) typeMap.get("value")) {
				incidentEventsByType.add(incidentEvent);
			}
		}

		return incidentEventsByType;
	}
}
