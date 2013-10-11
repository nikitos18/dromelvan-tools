package org.dromelvan.tools.parser;

public class CardParserObject extends MatchEventParserObject {

    public enum CardType {
        YELLOW,
        RED;
    }

    private String player;
    private CardType type;

    public CardParserObject(String player, int time, CardType type) {
        super(time);
        this.player = player;
        this.type = type;
    }

    public String getPlayer() {
        return player;
    }
    public void setPlayer(String player) {
        this.player = player;
    }

    public CardType getCardType() {
        return type;
    }
    public void setCardType(CardType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("Card - Player: %s Time: %d Type: %s", getPlayer(), getTime(), getCardType());
    }

}
