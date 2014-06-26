package org.dromelvan.tools.parser.whoscored;

import org.dromelvan.tools.parser.match.MatchParserObject;

public class WhoScoredMatchParserObject extends MatchParserObject {

	private int whoScoredId;

	public int getWhoScoredId() {
		return whoScoredId;
	}

	public void setWhoScoredId(int whoScoredId) {
		this.whoScoredId = whoScoredId;
	}

	protected WhoScoredTeamParserObject getTeamForPlayer(int whoScoredId) {
		WhoScoredTeamParserObject whoScoredTeamParserObject = (WhoScoredTeamParserObject) getHomeTeam();
		if (whoScoredTeamParserObject.getPlayer(whoScoredId) != null) {
			return whoScoredTeamParserObject;
		}
		whoScoredTeamParserObject = (WhoScoredTeamParserObject) getAwayTeam();
		if (whoScoredTeamParserObject.getPlayer(whoScoredId) != null) {
			return whoScoredTeamParserObject;
		}
		return null;
	}

	protected WhoScoredTeamParserObject getTeamForGoal(WhoScoredGoalParserObject whoScoredGoalParserObject) {
		WhoScoredTeamParserObject homeTeam = (WhoScoredTeamParserObject) getHomeTeam();
		WhoScoredTeamParserObject awayTeam = (WhoScoredTeamParserObject) getAwayTeam();

		if ((homeTeam.getPlayer(whoScoredGoalParserObject.getPlayerWhoScoredId()) != null && !whoScoredGoalParserObject.isOwnGoal())
				|| (awayTeam.getPlayer(whoScoredGoalParserObject.getPlayerWhoScoredId()) != null && whoScoredGoalParserObject.isOwnGoal())) {
			return homeTeam;
		}
		if ((awayTeam.getPlayer(whoScoredGoalParserObject.getPlayerWhoScoredId()) != null && !whoScoredGoalParserObject.isOwnGoal())
				|| (homeTeam.getPlayer(whoScoredGoalParserObject.getPlayerWhoScoredId()) != null && whoScoredGoalParserObject.isOwnGoal())) {
			return awayTeam;
		}
		return null;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", super.toString(), getWhoScoredId());
	}
}
