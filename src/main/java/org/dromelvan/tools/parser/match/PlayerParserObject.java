package org.dromelvan.tools.parser.match;

import org.dromelvan.tools.parser.ParserObject;

public class PlayerParserObject extends ParserObject {

	private String name;
	private int whoScoredId;
	private int participated;
	private int goals;
	private int assists;
	private int rating;

	public PlayerParserObject() {
	}

	public PlayerParserObject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWhoScoredId() {
		return whoScoredId;
	}

	public void setWhoScoredId(int whoScoredId) {
		this.whoScoredId = whoScoredId;
	}

	public int getParticipated() {
		return participated;
	}

	public void setParticipated(int participated) {
		this.participated = participated;
	}

	public int getGoals() {
		return goals;
	}

	public void setGoals(int goals) {
		this.goals = goals;
	}

	public int getAssists() {
		return assists;
	}

	public void setAssists(int assists) {
		this.assists = assists;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return String.format("%s Participated: %d Goals: %d Assists: %d Rating: %d", getName(), getParticipated(), getGoals(), getAssists(), getRating());
	}
}
