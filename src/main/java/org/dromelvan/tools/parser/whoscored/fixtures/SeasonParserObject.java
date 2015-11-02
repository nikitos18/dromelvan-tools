package org.dromelvan.tools.parser.whoscored.fixtures;

import java.util.ArrayList;
import java.util.List;

import org.dromelvan.tools.parser.ParserObject;

public class SeasonParserObject extends ParserObject {

    private List<MatchDayParserObject> matchDayParserObjects = new ArrayList<MatchDayParserObject>();

    public List<MatchDayParserObject> getMatchDayParserObjects() {
        return matchDayParserObjects;
    }

    public void setMatchDayParserObjects(List<MatchDayParserObject> matchDayParserObjects) {
        this.matchDayParserObjects = matchDayParserObjects;
    }
}
