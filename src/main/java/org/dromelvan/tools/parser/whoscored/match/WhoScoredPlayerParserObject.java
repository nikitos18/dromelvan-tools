package org.dromelvan.tools.parser.whoscored.match;

import org.dromelvan.tools.parser.match.PlayerParserObject;

public class WhoScoredPlayerParserObject extends PlayerParserObject {

    private int whoScoredId;
	private WhoScoredPlayerStatsMap whoScoredPlayerStatsMap;

	public WhoScoredPlayerParserObject(WhoScoredPlayerStatsMap whoScoredPlayerStatsMap) {
	    setWhoScoredId(whoScoredPlayerStatsMap.getWhoScoredId());
		setName(whoScoredPlayerStatsMap.getName());
		setParticipated(whoScoredPlayerStatsMap.getParticipated());
		setRating(whoScoredPlayerStatsMap.getRating());
		setGoals(whoScoredPlayerStatsMap.getGoals());
		setAssists(whoScoredPlayerStatsMap.getAssists());
		this.whoScoredPlayerStatsMap = whoScoredPlayerStatsMap;
	}

	public int getWhoScoredId() {
        return whoScoredId;
    }

    public void setWhoScoredId(int whoScoredId) {
        this.whoScoredId = whoScoredId;
    }

    public WhoScoredPlayerStatsMap getWhoScoredPlayerStatsMap() {
		return whoScoredPlayerStatsMap;
	}

	public void setWhoScoredPlayerStatsMap(WhoScoredPlayerStatsMap whoScoredPlayerStatsMap) {
		this.whoScoredPlayerStatsMap = whoScoredPlayerStatsMap;
	}

	@Override
	public String toString() {
		return String.format("Name: %s (%s) Participated: %s Rating: %s Stats: %s", getName(), getWhoScoredId(), getParticipated(), getRating(), getWhoScoredPlayerStatsMap());
	}
}
