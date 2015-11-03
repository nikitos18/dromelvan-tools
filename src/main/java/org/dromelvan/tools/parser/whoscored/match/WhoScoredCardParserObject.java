package org.dromelvan.tools.parser.whoscored.match;

import java.util.Map;

import org.dromelvan.tools.parser.match.CardParserObject;

public class WhoScoredCardParserObject extends CardParserObject {

	public WhoScoredCardParserObject(Map cardEvent) {
		setWhoScoredId((int) cardEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_PLAYER_ID));
		setPlayer(PlayerNameDictionary.getName(getWhoScoredId()));
		setTime((int) cardEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_MINUTE) + 1);
		setCardType(((int) ((Map) cardEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_CARD_TYPE))
				.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_QUALIFIER_VALUE) == WhoScoredMatchJavaScriptVariables.TYPE_CARD_YELLOW ? CardType.YELLOW : CardType.RED));
	}

}
