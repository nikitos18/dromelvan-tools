package org.dromelvan.tools.parser.whoscored;

import org.dromelvan.tools.parser.match.TeamParserObject;

public class WhoScoredTeamParserObject extends TeamParserObject {

    private int whoScoredId;

    public WhoScoredTeamParserObject(String name, int whoScoredId) {
        super(name);
        this.whoScoredId = whoScoredId;
    }

    public int getWhoScoredId() {
        return whoScoredId;
    }

    public void setWhoScoredId(int whoScoredId) {
        this.whoScoredId = whoScoredId;
    }


    @Override
    public String toString() {
        return String.format("%s (%s)", getName(), getWhoScoredId());
    }
}
