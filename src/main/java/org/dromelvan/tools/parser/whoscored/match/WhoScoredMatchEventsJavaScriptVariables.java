package org.dromelvan.tools.parser.whoscored.match;

import java.util.Map;

import org.dromelvan.tools.parser.javascript.JavaScriptVariables;
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
	public final static String START_TIME = "startTime";

	public WhoScoredMatchEventsJavaScriptVariables(Map map) {
		super(map);
		System.out.println(getMatchCentreData().keySet());
	}

	public int getMatchId() {
		return (Integer) get(MATCH_ID);
	}

	public DateTime getDateTime() {
		DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("MM/dd/YYYY HH:mm:ss");
		return DateTime.parse((String) getMatchCentreData().get(START_TIME), dateTimeFormat);
	}

	private Map getMatchCentreData() {
		return (Map) get(MATCH_CENTRE_DATA);
	}
}
