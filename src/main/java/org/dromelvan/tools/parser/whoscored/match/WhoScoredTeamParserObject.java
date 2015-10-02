package org.dromelvan.tools.parser.whoscored.match;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dromelvan.tools.parser.match.PlayerParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;

public class WhoScoredTeamParserObject extends TeamParserObject {

	private int whoScoredId;
	private Map<Integer, Map> incidentEvents = new HashMap<Integer, Map>();

	public final static int TYPE_ASSIST = 1;
	public final static int TYPE_GOAL = 16;
	public final static int TYPE_CARD = 17;
	public final static int TYPE_SUBSTITUTION_OFF = 18;
	public final static int TYPE_SUBSTITUTION_ON = 19;
	public final static int TYPE_OWN_GOAL = 28;

	public WhoScoredTeamParserObject(Map team) {
		setName((String) team.get("name"));
		setWhoScoredId((int) team.get("teamId"));

		List<Map> players = (List<Map>) team.get("players");
		for (Map player : players) {
			WhoScoredPlayerParserObject playerParserObject = new WhoScoredPlayerParserObject(player);
			getPlayers().add(playerParserObject);
		}

		for (Map incidentEvent : (List<Map>) team.get("incidentEvents")) {
			int eventId = (Integer) incidentEvent.get("eventId");
			incidentEvents.put(eventId, incidentEvent);
		}

		for (Map incidentEvent : incidentEvents.values()) {
			Map type = (Map) incidentEvent.get("type");
			int typeValue = (int) type.get("value");

			if (typeValue == TYPE_GOAL) {
				WhoScoredGoalParserObject whoScoredGoalParserObject = new WhoScoredGoalParserObject(incidentEvent);
				getGoals().add(whoScoredGoalParserObject);
			} else if (typeValue == TYPE_CARD) {
				WhoScoredCardParserObject whoScoredCardParserObject = new WhoScoredCardParserObject(incidentEvent);
				getCards().add(whoScoredCardParserObject);
			}
		}

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
