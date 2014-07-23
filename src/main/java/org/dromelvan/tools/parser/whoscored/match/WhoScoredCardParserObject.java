package org.dromelvan.tools.parser.whoscored.match;

import org.dromelvan.tools.parser.match.CardParserObject;

public class WhoScoredCardParserObject extends CardParserObject {

	private int playerWhoScoredId;

	public WhoScoredCardParserObject(String player, int playerWhoScoredId, int time, CardType cardType) {
		super(player, playerWhoScoredId, time, cardType);
		this.playerWhoScoredId = playerWhoScoredId;
	}

	public int getPlayerWhoScoredId() {
		return playerWhoScoredId;
	}

	public void setPlayerWhoScoredId(int playerWhoScoredId) {
		this.playerWhoScoredId = playerWhoScoredId;
	}

	@Override
	public String toString() {
		return String.format("Card - Player: %s (%s) Time: %d Type: %s", getPlayer(), getPlayerWhoScoredId(), getTime(), getCardType());
	}

}
