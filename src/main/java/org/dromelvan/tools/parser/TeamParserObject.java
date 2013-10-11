package org.dromelvan.tools.parser;

import java.util.ArrayList;
import java.util.List;

public class TeamParserObject extends ParserObject {

    private String name;
    private List<PlayerParserObject> players = new ArrayList<PlayerParserObject>();
    private List<GoalParserObject> goals = new ArrayList<GoalParserObject>();
    private List<CardParserObject> cards = new ArrayList<CardParserObject>();
    private List<SubstitutionParserObject> substitutions = new ArrayList<SubstitutionParserObject>();

    public TeamParserObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<PlayerParserObject> getPlayers() {
        return players;
    }
    public void setPlayers(List<PlayerParserObject> players) {
        this.players = players;
    }

    public List<GoalParserObject> getGoals() {
        return goals;
    }
    public void setGoals(List<GoalParserObject> goals) {
        this.goals = goals;
    }

    public List<CardParserObject> getCards() {
        return cards;
    }
    public void setCards(List<CardParserObject> cards) {
        this.cards = cards;
    }

    public List<SubstitutionParserObject> getSubstitutions() {
        return substitutions;
    }
    public void setSubstitutions(List<SubstitutionParserObject> substitutions) {
        this.substitutions = substitutions;
    }

    public PlayerParserObject getPlayer(String name) {
        for(PlayerParserObject player : this.players) {
            if(player.getName().toLowerCase().endsWith(name.toLowerCase())) {
                return player;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }

}
