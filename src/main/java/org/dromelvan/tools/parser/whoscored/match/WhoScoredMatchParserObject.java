package org.dromelvan.tools.parser.whoscored.match;

import java.util.Map;

import org.dromelvan.tools.parser.match.MatchParserObject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class WhoScoredMatchParserObject extends MatchParserObject {

	private int whoScoredId;

	public WhoScoredMatchParserObject(Map match) {
		setWhoScoredId((Integer) match.get(WhoScoredMatchJavaScriptVariables.MATCH_ID));

		Map matchCentreData = (Map) match.get(WhoScoredMatchJavaScriptVariables.MATCH_CENTRE_DATA);

		DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("MM/dd/YYYY HH:mm:ss 'PM'");
		DateTime dateTime = DateTime.parse((String) matchCentreData.get(WhoScoredMatchJavaScriptVariables.START_TIME), dateTimeFormat);
		setDateTime(dateTime.toString());

		setTimeElapsed((String) matchCentreData.get(WhoScoredMatchJavaScriptVariables.ELAPSED));

		WhoScoredTeamParserObject homeTeamParserObject = new WhoScoredTeamParserObject((Map) matchCentreData.get(WhoScoredMatchJavaScriptVariables.HOME_TEAM));
		WhoScoredTeamParserObject awayTeamParserObject = new WhoScoredTeamParserObject((Map) matchCentreData.get(WhoScoredMatchJavaScriptVariables.AWAY_TEAM));

		homeTeamParserObject.getGoals().addAll(awayTeamParserObject.getOwnGoals());
		awayTeamParserObject.getGoals().addAll(homeTeamParserObject.getOwnGoals());

		setHomeTeam(homeTeamParserObject);
		setAwayTeam(awayTeamParserObject);
	}

	public int getWhoScoredId() {
		return whoScoredId;
	}

	public void setWhoScoredId(int whoScoredId) {
		this.whoScoredId = whoScoredId;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", super.toString(), getWhoScoredId());
	}
}
