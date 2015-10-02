package org.dromelvan.tools.parser.match;

public class GoalParserObject extends MatchEventParserObject {

	private String player;
	private int whoScoredId;
	private boolean penalty;
	private boolean ownGoal;

	public GoalParserObject() {
	}

	public GoalParserObject(String player, int whoScoredId, int time, boolean penalty, boolean ownGoal) {
		super(time);
		this.player = player;
		this.whoScoredId = whoScoredId;
		this.penalty = penalty;
		this.ownGoal = ownGoal;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public int getWhoScoredId() {
		return whoScoredId;
	}

	public void setWhoScoredId(int whoScoredId) {
		this.whoScoredId = whoScoredId;
	}

	public boolean isPenalty() {
		return penalty;
	}

	public void setPenalty(boolean penalty) {
		this.penalty = penalty;
	}

	public boolean isOwnGoal() {
		return ownGoal;
	}

	public void setOwnGoal(boolean ownGoal) {
		this.ownGoal = ownGoal;
	}

	@Override
	public String toString() {
		return String.format("Goal - Player: %s (%s) Time: %d Penalty: %s Own Goal: %s", getPlayer(), getWhoScoredId(), getTime(), isPenalty(), isOwnGoal());
	}

}
