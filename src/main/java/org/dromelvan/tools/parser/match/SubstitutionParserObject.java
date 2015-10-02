package org.dromelvan.tools.parser.match;

public class SubstitutionParserObject extends MatchEventParserObject {

	private String playerOut;
	private String playerIn;
	private int playerOutWhoScoredId;
	private int playerInWhoScoredId;

	public SubstitutionParserObject() {
	}

	public SubstitutionParserObject(String playerOut, String playerIn, int playerOutWhoScoredId, int playerInWhoScoredId, int time) {
		super(time);
		this.playerOut = playerOut;
		this.playerIn = playerIn;
		this.playerOutWhoScoredId = playerOutWhoScoredId;
		this.playerInWhoScoredId = playerInWhoScoredId;
	}

	public String getPlayerOut() {
		return playerOut;
	}

	public void setPlayerOut(String playerOut) {
		this.playerOut = playerOut;
	}

	public String getPlayerIn() {
		return playerIn;
	}

	public void setPlayerIn(String playerIn) {
		this.playerIn = playerIn;
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
		return String.format("Substitution - Player out: %s Player in: %s Time: %d", getPlayerOut(), getPlayerIn(), getTime());
	}

}
