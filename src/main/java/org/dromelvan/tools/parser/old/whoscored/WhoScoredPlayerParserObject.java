package org.dromelvan.tools.parser.old.whoscored;

import org.dromelvan.tools.parser.old.PlayerParserObject;

public class WhoScoredPlayerParserObject extends PlayerParserObject {

	private WhoScoredPlayerStatsMap whoScoredPlayerStatsMap;

	public WhoScoredPlayerParserObject(WhoScoredPlayerStatsMap whoScoredPlayerStatsMap) {
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
		return String.format("Name: %s Participated: %s Rating: %s Stats: %s", getName(), getParticipated(), getRating(), getWhoScoredPlayerStatsMap());
	}
}
