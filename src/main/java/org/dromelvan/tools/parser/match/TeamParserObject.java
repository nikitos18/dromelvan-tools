package org.dromelvan.tools.parser.match;

import java.util.ArrayList;
import java.util.List;

import org.dromelvan.tools.parser.ParserObject;

public class TeamParserObject extends ParserObject {

	private String name;
	private List<PlayerParserObject> players = new ArrayList<PlayerParserObject>();
	private List<GoalParserObject> goals = new ArrayList<GoalParserObject>();
	private List<CardParserObject> cards = new ArrayList<CardParserObject>();
	private List<SubstitutionParserObject> substitutions = new ArrayList<SubstitutionParserObject>();

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

	@Override
	public String toString() {
		return getName();
	}

}
