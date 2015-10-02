package org.dromelvan.tools.parser.whoscored.match;

import java.util.Map;

import org.dromelvan.tools.parser.match.MatchParserObject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class WhoScoredMatchParserObject extends MatchParserObject {

	public final static String MATCH_ID = "matchId";
	public final static String MATCH_CENTRE_DATA = "matchCentreData";
	public final static String START_TIME = "startTime";
	public final static String ELAPSED = "elapsed";

	private int whoScoredId;

	public WhoScoredMatchParserObject(Map match) {
		setWhoScoredId((Integer) match.get(MATCH_ID));

		Map matchCentreData = (Map) match.get(MATCH_CENTRE_DATA);

		DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("MM/dd/YYYY HH:mm:ss");
		DateTime dateTime = DateTime.parse((String) matchCentreData.get(START_TIME), dateTimeFormat);
		setDateTime(dateTime.toString());

		setTimeElapsed((String) matchCentreData.get(ELAPSED));
	}

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

		if ((homeTeam.getPlayer(whoScoredGoalParserObject.getWhoScoredId()) != null && !whoScoredGoalParserObject.isOwnGoal())
				|| (awayTeam.getPlayer(whoScoredGoalParserObject.getWhoScoredId()) != null && whoScoredGoalParserObject.isOwnGoal())) {
			return homeTeam;
		}
		if ((awayTeam.getPlayer(whoScoredGoalParserObject.getWhoScoredId()) != null && !whoScoredGoalParserObject.isOwnGoal())
				|| (homeTeam.getPlayer(whoScoredGoalParserObject.getWhoScoredId()) != null && whoScoredGoalParserObject.isOwnGoal())) {
			return awayTeam;
		}
		return null;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", super.toString(), getWhoScoredId());
	}
}
