package org.dromelvan.tools.parser.whoscored;

import java.util.Date;

import com.google.common.collect.ComparisonChain;

public class MatchParserObject implements Comparable<MatchParserObject> {

    private String whoScoredId;
    private int matchDayNumber;
    private Date date;
    private String time;
    private int homeTeamId;
    private int whoScoredHomeTeamId;
    private String homeTeamName;
    private int awayTeamId;
    private int whoScoredAwayTeamId;
    private String awayTeamName;

    public String getWhoScoredId() {
        return whoScoredId;
    }
    public void setWhoScoredId(String whoScoredId) {
        this.whoScoredId = whoScoredId;
    }

    public int getMatchDayNumber() {
        return matchDayNumber;
    }
    public void setMatchDayNumber(int matchDayNumber) {
        this.matchDayNumber = matchDayNumber;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }
    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public int getWhoScoredHomeTeamId() {
        return whoScoredHomeTeamId;
    }
    public void setWhoScoredHomeTeamId(int whoScoredHomeTeamId) {
        this.whoScoredHomeTeamId = whoScoredHomeTeamId;
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }
    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public int getAwayTeamId() {
        return awayTeamId;
    }
    public void setAwayTeamId(int awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public int getWhoScoredAwayTeamId() {
        return whoScoredAwayTeamId;
    }
    public void setWhoScoredAwayTeamId(int whoScoredAwayTeamId) {
        this.whoScoredAwayTeamId = whoScoredAwayTeamId;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }
    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    @Override
    public int compareTo(MatchParserObject matchParserObject) {
        return ComparisonChain.start()
                .compare(getDate(), matchParserObject.getDate())
                .compare(getTime(), matchParserObject.getTime())
                .result();

    }
    @Override
    public String toString() {
        return String.format("whoScoredId: %s matchDayNumber: %d date: %s time: %s homeTeamId: %d whoScoredHomeTeamId: %d homeTeamName: %s awayTeamId: %d whoScoredAwayTeamId: %d awayTeamName: %s",
                             getWhoScoredId(), getMatchDayNumber(), getDate().toString(), getTime(), getHomeTeamId(), getWhoScoredHomeTeamId(), getHomeTeamName(), getAwayTeamId(), getWhoScoredAwayTeamId(), getAwayTeamName());
    }
}
