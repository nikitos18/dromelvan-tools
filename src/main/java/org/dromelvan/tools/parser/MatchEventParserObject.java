package org.dromelvan.tools.parser;

public class MatchEventParserObject extends ParserObject {

    private int time;

    public MatchEventParserObject(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }

}
