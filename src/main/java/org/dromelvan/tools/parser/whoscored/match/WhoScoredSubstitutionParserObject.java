package org.dromelvan.tools.parser.whoscored.match;

import java.util.Map;

import org.dromelvan.tools.parser.match.SubstitutionParserObject;

public class WhoScoredSubstitutionParserObject extends SubstitutionParserObject {

	public WhoScoredSubstitutionParserObject(Map substitutionOutEvent) {
		setPlayerOutWhoScoredId((int) substitutionOutEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_PLAYER_ID));
		setPlayerOut(PlayerNameDictionary.getName(getPlayerOutWhoScoredId()));
		setTime((int) substitutionOutEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_MINUTE) + 1);

		setPlayerInWhoScoredId((int) substitutionOutEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_RELATED_PLAYER_ID));
		setPlayerIn(PlayerNameDictionary.getName(getPlayerInWhoScoredId()));
	}

}
