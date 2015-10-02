package org.dromelvan.tools.parser.whoscored.match;

import java.util.Map;

import org.dromelvan.tools.parser.match.SubstitutionParserObject;

public class WhoScoredSubstitutionParserObject extends SubstitutionParserObject {

	public WhoScoredSubstitutionParserObject(Map substitutionOutEvent, Map substitutionInEvent) {
		setPlayerOutWhoScoredId((int) substitutionOutEvent.get("playerId"));
		setPlayerOut(PlayerNameDictionary.getName(getPlayerOutWhoScoredId()));
		setTime((int) substitutionOutEvent.get("minute") + 1);

		setPlayerInWhoScoredId((int) substitutionInEvent.get("playerId"));
		setPlayerIn(PlayerNameDictionary.getName(getPlayerInWhoScoredId()));
	}

}
