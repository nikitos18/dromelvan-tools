package org.dromelvan.tools.parser.whoscored;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dromelvan.tools.parser.ParserObject;

public class MatchDayParserObject extends ParserObject implements Comparable<MatchDayParserObject> {

    private Date date;
    private int matchDayNumber;
    private List<MatchParserObject> matchParserObjects = new ArrayList<MatchParserObject>();

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public int getMatchDayNumber() {
        return matchDayNumber;
    }
    public void setMatchDayNumber(int matchDayNumber) {
        this.matchDayNumber = matchDayNumber;
    }

    public List<MatchParserObject> getMatchParserObjects() {
        return matchParserObjects;
    }
    public void setMatchParserObjects(List<MatchParserObject> matchParserObjects) {
        this.matchParserObjects = matchParserObjects;
    }

    @Override
    public int compareTo(MatchDayParserObject matchDayParserObject) {
        return getDate().compareTo(matchDayParserObject.getDate());
    }

}
