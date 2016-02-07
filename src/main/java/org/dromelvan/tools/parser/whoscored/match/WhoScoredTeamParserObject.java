package org.dromelvan.tools.parser.whoscored.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dromelvan.tools.parser.match.GoalParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhoScoredTeamParserObject extends TeamParserObject {

	private int whoScoredId;
	private List<GoalParserObject> ownGoals = new ArrayList<GoalParserObject>();
	private final static Logger logger = LoggerFactory.getLogger(WhoScoredTeamParserObject.class);

	public WhoScoredTeamParserObject(Map team) {
		setName((String) team.get(WhoScoredMatchJavaScriptVariables.TEAM_NAME));
		setWhoScoredId((int) team.get(WhoScoredMatchJavaScriptVariables.TEAM_ID));

		Map<Integer, WhoScoredPlayerParserObject> playerMap = new HashMap<Integer, WhoScoredPlayerParserObject>();
		List<Map> players = (List<Map>) team.get(WhoScoredMatchJavaScriptVariables.TEAM_PLAYERS);
		for (Map player : players) {
			WhoScoredPlayerParserObject whoScoredPlayerParserObject = new WhoScoredPlayerParserObject(player);
			getPlayers().add(whoScoredPlayerParserObject);
			playerMap.put(whoScoredPlayerParserObject.getWhoScoredId(), whoScoredPlayerParserObject);
		}

		List<Map> incidentEvents = (List<Map>) team.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENTS);

		for (Map incidentEvent : incidentEvents) {
			Map type = (Map) incidentEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_TYPE);
			int typeValue = (int) type.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_VALUE);

			if (typeValue == WhoScoredMatchJavaScriptVariables.TYPE_GOAL) {
				WhoScoredGoalParserObject whoScoredGoalParserObject = new WhoScoredGoalParserObject(incidentEvent);
				if(!whoScoredGoalParserObject.isOwnGoal()) {
				    getGoals().add(whoScoredGoalParserObject);
				    WhoScoredPlayerParserObject relatedWhoScoredPlayerParserObject = playerMap.get(incidentEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_RELATED_PLAYER_ID));
				    if(relatedWhoScoredPlayerParserObject != null) {
				        relatedWhoScoredPlayerParserObject.setAssists(relatedWhoScoredPlayerParserObject.getAssists() + 1);
				    }
				} else {
				    getOwnGoals().add(whoScoredGoalParserObject);
				}
			} else if (typeValue == WhoScoredMatchJavaScriptVariables.TYPE_CARD) {
			    // For example Stoke - Everton 6.2 2016
	            if(incidentEvent.get(WhoScoredMatchJavaScriptVariables.TEAM_INCIDENT_EVENT_PLAYER_ID) == null) {
	                logger.error("Value playerId missing in incident event {}.", incidentEvent);
	                continue;
	            }
				WhoScoredCardParserObject whoScoredCardParserObject = new WhoScoredCardParserObject(incidentEvent);
				getCards().add(whoScoredCardParserObject);
			} else if (typeValue == WhoScoredMatchJavaScriptVariables.TYPE_SUBSTITUTION_OFF) {
				WhoScoredSubstitutionParserObject whoScoredSubstitutionParserObject = new WhoScoredSubstitutionParserObject(incidentEvent);
				getSubstitutions().add(whoScoredSubstitutionParserObject);
			}
		}

	}

	public int getWhoScoredId() {
		return whoScoredId;
	}

	public void setWhoScoredId(int whoScoredId) {
		this.whoScoredId = whoScoredId;
	}

	public List<GoalParserObject> getOwnGoals() {
        return ownGoals;
    }

    public void setOwnGoals(List<GoalParserObject> ownGoals) {
        this.ownGoals = ownGoals;
    }

    @Override
	public String toString() {
		return String.format("%s (%s)", getName(), getWhoScoredId());
	}
}
