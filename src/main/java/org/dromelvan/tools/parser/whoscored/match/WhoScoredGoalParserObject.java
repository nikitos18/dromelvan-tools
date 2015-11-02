package org.dromelvan.tools.parser.whoscored.match;

import java.util.List;
import java.util.Map;

import org.dromelvan.tools.parser.match.GoalParserObject;

public class WhoScoredGoalParserObject extends GoalParserObject {

	private int teamId;

	public WhoScoredGoalParserObject(Map goalEvent) {
		setTeamId((int) goalEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_TEAM_ID));
		setWhoScoredId((int) goalEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_PLAYER_ID));
		setPlayer(PlayerNameDictionary.getName(getWhoScoredId()));
		setTime((int) goalEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_MINUTE) + 1);
		Boolean ownGoal = (Boolean) goalEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_OWN_GOAL);
		setOwnGoal(ownGoal == null ? false : ownGoal);

		List<Map> qualifiers = (List<Map>) goalEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_QUALIFIERS);
		for (Map qualifier : qualifiers) {
			Map typeMap = (Map) qualifier.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_QUALIFIER_TYPE);
			int value = (int) typeMap.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_QUALIFIER_VALUE);
			if (value == WhoScoredMatchJavaScriptVariables.TYPE_PENALTY) {
				setPenalty(true);
				break;
			}
		}
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

}
