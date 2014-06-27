package org.dromelvan.tools.util.parser.whoscored.fixtures;

import org.dromelvan.tools.parser.ParserObject;

public class FixtureParserObject extends ParserObject {

	private int whoScoredId;
	private int homeTeamId;
	private int awayTeamId;
	private String date;
	private String time;

	public FixtureParserObject(int whoScoredId, int homeTeamId, int awayTeamId, String date, String time) {
		this.whoScoredId = whoScoredId;
		this.homeTeamId = homeTeamId;
		this.awayTeamId = awayTeamId;
		this.date = date;
		this.time = time;
	}

	public int getWhoScoredId() {
		return whoScoredId;
	}

	public void setWhoScoredId(int whoScoredId) {
		this.whoScoredId = whoScoredId;
	}

	public int getHomeTeamId() {
		return homeTeamId;
	}

	public void setHomeTeamId(int homeTeamId) {
		this.homeTeamId = homeTeamId;
	}

	public int getAwayTeamId() {
		return awayTeamId;
	}

	public void setAwayTeamId(int awayTeamId) {
		this.awayTeamId = awayTeamId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return String.format("Id: %s, HT: %s, AT: %s, Date: %s, Time: %s", getWhoScoredId(), getHomeTeamId(), getAwayTeamId(), getDate(), getTime());
	}
}
