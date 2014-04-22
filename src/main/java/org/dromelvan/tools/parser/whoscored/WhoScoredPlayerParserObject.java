package org.dromelvan.tools.parser.whoscored;

import org.dromelvan.tools.parser.PlayerParserObject;

public class WhoScoredPlayerParserObject extends PlayerParserObject {

    public WhoScoredPlayerParserObject(WhoScoredPlayerStatsMap map) {
        setName(map.getName());
        setParticipated(map.getParticipated());
        setRating(map.getRating());        
        setAssists(map.getAssists());
    }
}
