package org.dromelvan.tools.parser.whoscored.match;

import org.dromelvan.tools.parser.match.GoalParserObject;

public class WhoScoredGoalParserObject extends GoalParserObject {

    private int teamId;
	private int playerWhoScoredId;
	private String assistPlayer;
	private int assistPlayerWhoScoredId;

	public WhoScoredGoalParserObject(int teamId, String player, int playerWhoScoredId, String assistPlayer, int assistPlayerWhoScoredId, int time, boolean penalty, boolean ownGoal) {
		super(player, playerWhoScoredId, time, penalty, ownGoal);
		this.teamId = teamId;
		this.playerWhoScoredId = playerWhoScoredId;
		this.assistPlayer = assistPlayer;
		this.assistPlayerWhoScoredId = assistPlayerWhoScoredId;
	}

	public int getTeamId() {
        return teamId;
    }
    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getPlayerWhoScoredId() {
		return playerWhoScoredId;
	}

	public void setPlayerWhoScoredId(int playerWhoScoredId) {
		this.playerWhoScoredId = playerWhoScoredId;
	}

	public String getAssistPlayer() {
		return assistPlayer;
	}

	public void setAssistPlayer(String assistPlayer) {
		this.assistPlayer = assistPlayer;
	}

	public int getAssistPlayerWhoScoredId() {
		return assistPlayerWhoScoredId;
	}

	public void setAssistPlayerWhoScoredId(int assistPlayerWhoScoredId) {
		this.assistPlayerWhoScoredId = assistPlayerWhoScoredId;
	}

	@Override
	public String toString() {
		return String.format("Goal - Player: %s (%s) Time: %d Penalty: %s Own Goal: %s Assist: %s (%s)", getPlayer(), getPlayerWhoScoredId(), getTime(), isPenalty(), isOwnGoal(), getAssistPlayer(), getAssistPlayerWhoScoredId());
	}

}
