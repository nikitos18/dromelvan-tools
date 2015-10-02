package org.dromelvan.tools.parser.match;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dromelvan.tools.parser.ParserObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeamParserObject extends ParserObject {

	private String name;
	private List<PlayerParserObject> players = new ArrayList<PlayerParserObject>();
	private List<GoalParserObject> goals = new ArrayList<GoalParserObject>();
	private List<CardParserObject> cards = new ArrayList<CardParserObject>();
	private List<SubstitutionParserObject> substitutions = new ArrayList<SubstitutionParserObject>();
	private final static Logger logger = LoggerFactory.getLogger(TeamParserObject.class);

	public TeamParserObject() {
	}

	public TeamParserObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PlayerParserObject> getPlayers() {
		return players;
	}

	public void setPlayers(List<PlayerParserObject> players) {
		this.players = players;
	}

	public List<GoalParserObject> getGoals() {
		return goals;
	}

	public void setGoals(List<GoalParserObject> goals) {
		this.goals = goals;
	}

	public List<CardParserObject> getCards() {
		return cards;
	}

	public void setCards(List<CardParserObject> cards) {
		this.cards = cards;
	}

	public List<SubstitutionParserObject> getSubstitutions() {
		return substitutions;
	}

	public void setSubstitutions(List<SubstitutionParserObject> substitutions) {
		this.substitutions = substitutions;
	}

	public PlayerParserObject getPlayer(String name, String context) {
		Set<PlayerParserObject> matchingPlayers = new HashSet<PlayerParserObject>();
		for (PlayerParserObject player : this.players) {
			if (player.getName().toLowerCase().endsWith(name.toLowerCase())) {
				matchingPlayers.add(player);
			}
		}
		if (matchingPlayers.size() == 1) {
			return matchingPlayers.iterator().next();
		} else if (!matchingPlayers.isEmpty()) {
			logger.warn("More than one match for player {} in team {}:", name, getName());
			PlayerParserObject matchingPlayer = null;
			for (PlayerParserObject player : matchingPlayers) {
				if (played(player)) {
					matchingPlayer = player;
					logger.warn("{} played.", player.getName());
				} else {
					logger.warn("{} did not play.", player.getName());
				}
			}
			if (matchingPlayer == null) {
				matchingPlayer = matchingPlayers.iterator().next();
			}
			logger.warn("Updating {} for {}.", context, matchingPlayer.getName());
			return matchingPlayer;
		}
		return null;
	}

	private boolean played(PlayerParserObject player) {
		if (player.getParticipated() == 2) {
			return true;
		}
		if (player.getParticipated() == 1) {
			for (SubstitutionParserObject substition : getSubstitutions()) {
				if (substition.getPlayerIn().equalsIgnoreCase(player.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return getName();
	}

}
