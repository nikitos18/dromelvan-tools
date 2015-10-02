package org.dromelvan.tools.parser.whoscored.match;

import java.util.Map;

import org.dromelvan.tools.parser.match.CardParserObject;

public class WhoScoredCardParserObject extends CardParserObject {

	public final static int TYPE_CARD_YELLOW = 31;
	public final static int TYPE_CARD_RED = 33;

	public WhoScoredCardParserObject(Map cardEvent) {
		setWhoScoredId((int) cardEvent.get("playerId"));
		setPlayer(PlayerNameDictionary.getName(getWhoScoredId()));
		setTime((int) cardEvent.get("minute") + 1);
		setCardType(((int) ((Map) cardEvent.get("cardType")).get("value") == TYPE_CARD_YELLOW ? CardType.YELLOW : CardType.RED));
	}

}
