package org.dromelvan.tools.parser.whoscored.match;

import org.dromelvan.tools.parser.match.PlayerParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;

public class WhoScoredTeamParserObject extends TeamParserObject {

	private int whoScoredId;

	public WhoScoredTeamParserObject(String name, int whoScoredId) {
		super(name);
		this.whoScoredId = whoScoredId;
	}

	public int getWhoScoredId() {
		return whoScoredId;
	}

	public void setWhoScoredId(int whoScoredId) {
		this.whoScoredId = whoScoredId;
	}

	public WhoScoredPlayerParserObject getPlayer(int whoScoredId) {
		for (PlayerParserObject playerParserObject : getPlayers()) {
			WhoScoredPlayerParserObject whoScoredPlayerParserObject = (WhoScoredPlayerParserObject) playerParserObject;
			if (whoScoredPlayerParserObject.getWhoScoredId() == whoScoredId) {
				return whoScoredPlayerParserObject;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", getName(), getWhoScoredId());
	}
}
