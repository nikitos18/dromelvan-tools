package org.dromelvan.tools.parser.whoscored.match;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.match.MatchParserObject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhoScoredMatchParserObject extends MatchParserObject {

	private int whoScoredId;
	private final static Logger logger = LoggerFactory.getLogger(WhoScoredMatchParserObject.class);

	public WhoScoredMatchParserObject(Map match) {
		setWhoScoredId((Integer) match.get(WhoScoredMatchJavaScriptVariables.MATCH_ID));

		Map matchCentreData = (Map) match.get(WhoScoredMatchJavaScriptVariables.MATCH_CENTRE_DATA);
		Pattern startTimePattern = Pattern.compile("(\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}).*");
		Matcher startTimeMatcher = startTimePattern.matcher((String) matchCentreData.get(WhoScoredMatchJavaScriptVariables.START_TIME));
		if (startTimeMatcher.matches()) {
			DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("MM/dd/YYYY HH:mm:ss");
			DateTime dateTime = DateTime.parse(startTimeMatcher.group(1), dateTimeFormat);
			setDateTime(dateTime.toString());
		} else {
			logger.warn("Could not parse start time from input {}.", matchCentreData.get(WhoScoredMatchJavaScriptVariables.START_TIME));
		}

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
