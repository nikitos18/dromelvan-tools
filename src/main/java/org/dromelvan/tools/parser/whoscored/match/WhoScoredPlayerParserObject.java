package org.dromelvan.tools.parser.whoscored.match;

import org.dromelvan.tools.parser.match.PlayerParserObject;

public class WhoScoredPlayerParserObject extends PlayerParserObject {

	private WhoScoredPlayerStatsMap whoScoredPlayerStatsMap;

	public WhoScoredPlayerParserObject() {}

	public WhoScoredPlayerParserObject(WhoScoredPlayerStatsMap whoScoredPlayerStatsMap) {
		setWhoScoredId(whoScoredPlayerStatsMap.getWhoScoredId());
		setName(whoScoredPlayerStatsMap.getName());
		setParticipated(whoScoredPlayerStatsMap.getParticipated());
		setRating(whoScoredPlayerStatsMap.getRating());
		setGoals(whoScoredPlayerStatsMap.getGoals());
		setAssists(whoScoredPlayerStatsMap.getAssists());
		this.whoScoredPlayerStatsMap = whoScoredPlayerStatsMap;
	}

	public WhoScoredPlayerStatsMap getWhoScoredPlayerStatsMap() {
		return whoScoredPlayerStatsMap;
	}

	public void setWhoScoredPlayerStatsMap(WhoScoredPlayerStatsMap whoScoredPlayerStatsMap) {
		this.whoScoredPlayerStatsMap = whoScoredPlayerStatsMap;
	}

	@Override
	public String toString() {
		return String.format("Name: %s (%s) Participated: %s Rating: %s Assists: %s Stats: %s", getName(), getWhoScoredId(), getParticipated(), getRating(), getAssists(), getWhoScoredPlayerStatsMap());
	}
}
