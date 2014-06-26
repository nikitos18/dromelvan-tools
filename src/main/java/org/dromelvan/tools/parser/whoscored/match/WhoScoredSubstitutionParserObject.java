package org.dromelvan.tools.parser.whoscored.match;

import org.dromelvan.tools.parser.match.SubstitutionParserObject;

public class WhoScoredSubstitutionParserObject extends SubstitutionParserObject {

	private int playerOutWhoScoredId;
	private int playerInWhoScoredId;

	public WhoScoredSubstitutionParserObject(String playerOut, int playerOutWhoScoredId, String playerIn, int playerInWhoScoredId, int time) {
		super(playerOut, playerIn, time);
		this.playerOutWhoScoredId = playerOutWhoScoredId;
		this.playerInWhoScoredId = playerInWhoScoredId;
	}

	public int getPlayerOutWhoScoredId() {
		return playerOutWhoScoredId;
	}

	public void setPlayerOutWhoScoredId(int playerOutWhoScoredId) {
		this.playerOutWhoScoredId = playerOutWhoScoredId;
	}

	public int getPlayerInWhoScoredId() {
		return playerInWhoScoredId;
	}

	public void setPlayerInWhoScoredId(int playerInWhoScoredId) {
		this.playerInWhoScoredId = playerInWhoScoredId;
	}

	@Override
	public String toString() {
		return String.format("Substitution - Player out: %s (%s) Player in: %s (%s) Time: %d", getPlayerOut(), getPlayerOutWhoScoredId(), getPlayerIn(), getPlayerInWhoScoredId(), getTime());
	}

}
