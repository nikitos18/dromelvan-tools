package org.dromelvan.tools.parser.match;

import java.util.HashSet;
import java.util.Set;

import org.dromelvan.tools.parser.ParserObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MatchParserObject extends ParserObject {

	private String dateTime;
	private String timeElapsed;
	private TeamParserObject homeTeam;
	private TeamParserObject awayTeam;
	private final static Logger logger = LoggerFactory.getLogger(MatchParserObject.class);

	public MatchParserObject() {
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getTimeElapsed() {
		return timeElapsed;
	}

	public void setTimeElapsed(String timeElapsed) {
		this.timeElapsed = timeElapsed;
	}

	public TeamParserObject getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(TeamParserObject homeTeam) {
		this.homeTeam = homeTeam;
	}

	public TeamParserObject getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(TeamParserObject awayTeam) {
		this.awayTeam = awayTeam;
	}

	public Set<TeamParserObject> getTeams() {
		Set<TeamParserObject> teams = new HashSet<TeamParserObject>();
		teams.add(this.homeTeam);
		teams.add(this.awayTeam);
		return teams;
	}

	public TeamParserObject getTeam(String name) {
		if (name.equalsIgnoreCase(this.homeTeam.getName())) {
			return this.homeTeam;
		} else if (name.equalsIgnoreCase(this.awayTeam.getName())) {
			return this.awayTeam;
		}
		logger.warn("Could not find team {} in {}.", name, this);
		return null;
	}

	@Override
	public String toString() {
		return String.format("Match: %s vs %s Kick off: %s Time lapsed: %s", getHomeTeam(), getAwayTeam(), getDateTime(), getTimeElapsed());
	}
}
