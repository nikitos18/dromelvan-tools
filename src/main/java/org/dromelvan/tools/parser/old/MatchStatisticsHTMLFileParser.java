package org.dromelvan.tools.parser.old;

import java.util.Set;


public abstract class MatchStatisticsHTMLFileParser extends HTMLFileParser<MatchParserObject> implements MatchStatisticsParser {

    public MatchStatisticsHTMLFileParser(ParserProperties parserProperties) {
        super(parserProperties);
    }

    @Override
    protected Set<MatchParserObject> parseDocument() {
        Set<MatchParserObject> matchParserObjects = parseMatches();
        postParseDocument(matchParserObjects);
        return matchParserObjects;
    }

    protected void postParseDocument(Set<MatchParserObject> matchParserObjects) {
        for(MatchParserObject matchParserObject : matchParserObjects) {
            getParserProperties().map(matchParserObject);
        }        
    }
    
    protected abstract Set<MatchParserObject> parseMatches();

    public MatchParserObject getMatchParserObject(String homeTeam, String awayTeam) {
        for(MatchParserObject matchParserObject : getParserObjects()) {
            if(matchParserObject.getHomeTeam().getName().equalsIgnoreCase(homeTeam)
               && matchParserObject.getAwayTeam().getName().equalsIgnoreCase(awayTeam)) {
                return matchParserObject;
            }
        }
        return null;
    }

}
