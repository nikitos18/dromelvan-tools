package org.dromelvan.tools.parser.whoscored;

import org.dromelvan.tools.parser.match.MatchParserObject;

public class WhoScoredMatchParserObject extends MatchParserObject {

    private int whoScoredId;

    public int getWhoScoredId() {
        return whoScoredId;
    }

    public void setWhoScoredId(int whoScoredId) {
        this.whoScoredId = whoScoredId;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", super.toString(), getWhoScoredId());
    }
}
