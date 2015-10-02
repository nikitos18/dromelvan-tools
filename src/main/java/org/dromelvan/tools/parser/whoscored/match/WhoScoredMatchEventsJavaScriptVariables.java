package org.dromelvan.tools.parser.whoscored.match;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dromelvan.tools.parser.javascript.JavaScriptVariables;

public class WhoScoredMatchEventsJavaScriptVariables extends JavaScriptVariables {

	/**
	 *
	 */
	private static final long serialVersionUID = 2635365858846187843L;
	public final static String MATCH_ID = "matchId";
	public final static String MATCH_CENTRE_DATA = "matchCentreData";
	public final static String QUALIFIERS = "qualifiers";
	public final static String HOME_TEAM = "home";
	public final static String AWAY_TEAM = "away";

	public final static int TYPE_ASSIST = 1;
	public final static int TYPE_SUBSTITUTION_OFF = 18;
	public final static int TYPE_SUBSTITUTION_ON = 19;
	public final static int TYPE_OWN_GOAL = 28;

	private Map<Integer, String> playerIdNameDictionary = new HashMap<Integer, String>();
	private Map<Integer, Map<Integer, Map>> incidentEvents = new HashMap<Integer, Map<Integer, Map>>();

	@Override
	public void init() {
		List<String> players = (List<String>) getMatchCentreData().get("playerIdNameDictionary");
		PlayerNameDictionary.init(players);

		List<Map> incidentEventList = new ArrayList<Map>();
		Map home = (Map) getMatchCentreData().get(HOME_TEAM);
		Map away = (Map) getMatchCentreData().get(AWAY_TEAM);

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

	public Map<Integer, String> getPlayerIdNameDictionary() {
		return playerIdNameDictionary;
	}

	public Map<Integer, Integer> getRatings() {
		Map<Integer, Integer> ratingMap = new HashMap<Integer, Integer>();

		Map home = (Map) getMatchCentreData().get(HOME_TEAM);
		Map away = (Map) getMatchCentreData().get(AWAY_TEAM);

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

	public WhoScoredMatchParserObject getMatchParserObject() {
		WhoScoredMatchParserObject matchParserObject = new WhoScoredMatchParserObject(this);

		WhoScoredTeamParserObject homeTeamParserObject = getTeamParserObject((Map) getMatchCentreData().get(HOME_TEAM));
		WhoScoredTeamParserObject awayTeamParserObject = getTeamParserObject((Map) getMatchCentreData().get(AWAY_TEAM));

		matchParserObject.setHomeTeam(homeTeamParserObject);
		matchParserObject.setAwayTeam(awayTeamParserObject);

		return matchParserObject;
	}

	public WhoScoredTeamParserObject getTeamParserObject(Map team) {
		WhoScoredTeamParserObject teamParserObject = new WhoScoredTeamParserObject(team);
		return teamParserObject;
	}

	public List<WhoScoredSubstitutionParserObject> getSubstitutionParserObjects() {
		List<WhoScoredSubstitutionParserObject> whoScoredSubstitutionParserObjects = new ArrayList<WhoScoredSubstitutionParserObject>();

		for (Map substitutionOutEvent : getIncidentEventsByType(TYPE_SUBSTITUTION_OFF)) {
			Map substitutionInEvent = incidentEvents.get(substitutionOutEvent.get("teamId")).get(substitutionOutEvent.get("relatedEventId"));

			WhoScoredSubstitutionParserObject whoScoredSubstitutionParserObject = new WhoScoredSubstitutionParserObject(substitutionOutEvent, substitutionInEvent);
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
