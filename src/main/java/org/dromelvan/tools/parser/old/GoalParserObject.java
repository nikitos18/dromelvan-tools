package org.dromelvan.tools.parser.old;

public class GoalParserObject extends MatchEventParserObject {

    private String player;
    private boolean penalty;
    private boolean ownGoal;

    public GoalParserObject(String player, int time, boolean penalty, boolean ownGoal) {
        super(time);
        this.player = player;
        this.penalty = penalty;
        this.ownGoal = ownGoal;
    }

    public String getPlayer() {
        return player;
    }
    public void setPlayer(String player) {
        this.player = player;
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
        return String.format("Goal - Player: %s Time: %d Penalty: %s Own Goal: %s", getPlayer(), getTime(), isPenalty(), isOwnGoal());
    }

}
