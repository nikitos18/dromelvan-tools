package org.dromelvan.tools.parser.whoscored.match;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dromelvan.tools.parser.match.TeamParserObject;

public class WhoScoredTeamParserObject extends TeamParserObject {

	private int whoScoredId;

	public WhoScoredTeamParserObject(Map team) {
		setName((String) team.get(WhoScoredMatchJavaScriptVariables.TEAM_NAME));
		setWhoScoredId((int) team.get(WhoScoredMatchJavaScriptVariables.TEAM_ID));

		Map<Integer, WhoScoredPlayerParserObject> playerMap = new HashMap<Integer, WhoScoredPlayerParserObject>();
		List<Map> players = (List<Map>) team.get(WhoScoredMatchJavaScriptVariables.TEAM_PLAYERS);
		for (Map player : players) {
			WhoScoredPlayerParserObject whoScoredPlayerParserObject = new WhoScoredPlayerParserObject(player);
			getPlayers().add(whoScoredPlayerParserObject);
			playerMap.put(whoScoredPlayerParserObject.getWhoScoredId(), whoScoredPlayerParserObject);
		}

		List<Map> incidentEvents = (List<Map>) team.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENTS);

		for (Map incidentEvent : incidentEvents) {
			Map type = (Map) incidentEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_TYPE);
			int typeValue = (int) type.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_VALUE);

			if (typeValue == WhoScoredMatchJavaScriptVariables.TYPE_GOAL) {
				WhoScoredGoalParserObject whoScoredGoalParserObject = new WhoScoredGoalParserObject(incidentEvent);
				getGoals().add(whoScoredGoalParserObject);
			} else if (typeValue == WhoScoredMatchJavaScriptVariables.TYPE_ASSIST) {
				WhoScoredPlayerParserObject whoScoredPlayerParserObject = playerMap.get(incidentEvent.get(WhoScoredMatchJavaScriptVariables.PLAYER_ID));
				whoScoredPlayerParserObject.setAssists(whoScoredPlayerParserObject.getAssists() + 1);
			} else if (typeValue == WhoScoredMatchJavaScriptVariables.TYPE_CARD) {
				WhoScoredCardParserObject whoScoredCardParserObject = new WhoScoredCardParserObject(incidentEvent);
				getCards().add(whoScoredCardParserObject);
			} else if (typeValue == WhoScoredMatchJavaScriptVariables.TYPE_SUBSTITUTION_OFF) {
				WhoScoredSubstitutionParserObject whoScoredSubstitutionParserObject = new WhoScoredSubstitutionParserObject(incidentEvent);
				getSubstitutions().add(whoScoredSubstitutionParserObject);
			}
		}

	}

	public int getWhoScoredId() {
		return whoScoredId;
	}

	public void setWhoScoredId(int whoScoredId) {
		this.whoScoredId = whoScoredId;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", getName(), getWhoScoredId());
	}
}
