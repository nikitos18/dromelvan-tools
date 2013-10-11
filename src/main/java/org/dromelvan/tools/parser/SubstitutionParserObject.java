package org.dromelvan.tools.parser;

public class SubstitutionParserObject extends MatchEventParserObject {

    private String playerOut;
    private String playerIn;

    public SubstitutionParserObject(String playerOut, String playerIn, int time) {
        super(time);
        this.playerOut = playerOut;
        this.playerIn = playerIn;
    }

    public String getPlayerOut() {
        return playerOut;
    }
    public void setPlayerOut(String playerOut) {
        this.playerOut = playerOut;
    }

    public String getPlayerIn() {
        return playerIn;
    }
    public void setPlayerIn(String playerIn) {
        this.playerIn = playerIn;
    }

    @Override
    public String toString() {
        return String.format("Substitution - Player out: %s Player in: %s Time: %d", getPlayerOut(), getPlayerIn(), getTime());
    }

}
