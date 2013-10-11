package org.dromelvan.tools.parser;

public class PlayerParserObject extends ParserObject {

    public String name;
    public int participated;
    public int assists;
    public int rating;

    public PlayerParserObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getParticipated() {
        return participated;
    }
    public void setParticipated(int participated) {
        this.participated = participated;
    }

    public int getAssists() {
        return assists;
    }
    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return String.format("%s Participated: %d Assists: %d Rating: %d", getName(), getParticipated(), getAssists(), getRating());
    }
}
