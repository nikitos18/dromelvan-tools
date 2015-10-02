package org.dromelvan.tools.parser.whoscored.match;

import java.util.List;
import java.util.Map;

import org.dromelvan.tools.parser.match.GoalParserObject;

public class WhoScoredGoalParserObject extends GoalParserObject {

	public final static int TYPE_PENALTY = 9;

	private int teamId;

	public WhoScoredGoalParserObject(Map goalEvent) {
		setTeamId((int) goalEvent.get("teamId"));
		setWhoScoredId((int) goalEvent.get("playerId"));
		setPlayer(PlayerNameDictionary.getName(getWhoScoredId()));
		setTime((int) goalEvent.get("minute") + 1);
		setOwnGoal((goalEvent.get("isOwnGoal") == null ? false : (boolean) goalEvent.get("isOwnGoal")));

		List<Map> qualifiers = (List<Map>) goalEvent.get("qualifiers");
		for (Map qualifier : qualifiers) {
			Map typeMap = (Map) qualifier.get("type");
			int value = (int) typeMap.get("value");
			if (value == TYPE_PENALTY) {
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
