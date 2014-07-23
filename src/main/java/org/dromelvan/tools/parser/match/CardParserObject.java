package org.dromelvan.tools.parser.match;

public class CardParserObject extends MatchEventParserObject {

	public enum CardType {
		YELLOW,
		RED;
	}

	private String player;
	private int whoScoredId;
	private CardType type;

	public CardParserObject(String player, int whoScoredId, int time, CardType type) {
		super(time);
		this.player = player;
		this.whoScoredId = whoScoredId;
		this.type = type;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public int getWhoScoredId() {
		return whoScoredId;
	}

	public void setWhoScoredId(int whoScoredId) {
		this.whoScoredId = whoScoredId;
	}

	public CardType getCardType() {
		return type;
	}

	public void setCardType(CardType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return String.format("Card - Player: %s Time: %d Type: %s", getPlayer(), getTime(), getCardType());
	}

}
