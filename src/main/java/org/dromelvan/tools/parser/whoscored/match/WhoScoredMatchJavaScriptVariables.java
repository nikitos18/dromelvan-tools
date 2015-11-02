package org.dromelvan.tools.parser.whoscored.match;

import java.util.List;
import java.util.Map;

import org.dromelvan.tools.parser.javascript.JavaScriptVariables;

public class WhoScoredMatchJavaScriptVariables extends JavaScriptVariables {

	/**
	 *
	 */
	private static final long serialVersionUID = 2635365858846187843L;
	public final static String MATCH_ID = "matchId";
	public final static String MATCH_CENTRE_DATA = "matchCentreData";
	public final static String START_TIME = "startTime";
	public final static String ELAPSED = "elapsed";
	public final static String QUALIFIERS = "qualifiers";
	public final static String HOME_TEAM = "home";
	public final static String AWAY_TEAM = "away";
	public final static String PLAYER_ID = "playerId";
	public final static String PLAYER_NAME = "name";
	public final static String PLAYER_POSITION = "position";
	public final static String PLAYER_POSITION_SUBSTITUTE = "SUB";
	public final static String PLAYER_STATS = "stats";
	public final static String PLAYER_STATS_RATINGS = "ratings";
	public final static String TEAM_ID = "teamId";
	public final static String TEAM_NAME = "name";
	public final static String TEAM_PLAYERS = "players";
	public final static String TEAM_INCIDENT_EVENTS = "incidentEvents";
	public final static String TEAM_INCIDENT_EVENT_ID = "eventId";
	public final static String TEAM_INCIDENT_EVENT_TEAM_ID = "teamId";
	public final static String TEAM_INCIDENT_EVENT_PLAYER_ID = "playerId";
	public final static String TEAM_INCIDENT_EVENT_TYPE = "type";
	public final static String TEAM_INCIDENT_EVENT_VALUE = "value";
	public final static String TEAM_INCIDENT_EVENT_MINUTE = "minute";
	public final static String TEAM_INCIDENT_EVENT_QUALIFIERS = "qualifiers";
	public final static String TEAM_INCIDENT_EVENT_QUALIFIER_TYPE = "type";
	public final static String TEAM_INCIDENT_EVENT_QUALIFIER_VALUE = "value";

	public final static String TEAM_INCIDENT_EVENT_OWN_GOAL = "isOwnGoal";
	public final static String TEAM_INCIDENT_EVENT_CARD_TYPE = "cardType";
	public final static String TEAM_INCIDENT_EVENT_RELATED_PLAYER_ID = "relatedPlayerId";

	public final static int TYPE_ASSIST = 1;
	public final static int TYPE_PENALTY = 9;
	public final static int TYPE_GOAL = 16;
	public final static int TYPE_CARD = 17;
	public final static int TYPE_SUBSTITUTION_OFF = 18;
	public final static int TYPE_SUBSTITUTION_ON = 19;
	public final static int TYPE_OWN_GOAL = 28;
	public final static int TYPE_CARD_YELLOW = 31;
	public final static int TYPE_CARD_RED = 33;

	public final static String PLAYER_ID_NAME_DICTIONARY = "playerIdNameDictionary";

	@Override
	public void init() {
		Map matchCentreData = (Map) get(MATCH_CENTRE_DATA);
		List<String> players = (List<String>) matchCentreData.get(PLAYER_ID_NAME_DICTIONARY);
		PlayerNameDictionary.init(players);
	}

	public WhoScoredMatchParserObject getMatchParserObject() {
		WhoScoredMatchParserObject matchParserObject = new WhoScoredMatchParserObject(this);
		return matchParserObject;
	}
}
