package org.dromelvan.tools.parser.whoscored;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhoScoredPlayerStatsMap extends HashMap<String, String> {

    /**
     * 
     */
    private static final long serialVersionUID = 1313287906428296801L;
    private final static Pattern playerPattern = Pattern.compile("\\[\\d*,'(.*)',[\\d\\.]*,\\[\\[(.*)\\]\\],\\d*,'([\\w\\(\\)]*)',\\d*,\\d*,(\\d*),'([\\w\\(\\),]*)',.*\\]", Pattern.DOTALL);
    private final static Pattern playerMatchStatisticPattern = Pattern.compile("\\['(.*)',\\['?([^']*)'?\\]\\]");   
    private final static String NAME = "name";
    private final static String CURRENT_POSITION = "current_position";
    private final static String PLAYABLE_POSITIONS = "playable_positions";
    private final static String SUBSTITUTION_TIME = "substitution_time";
    private final static String RATING = "rating";
    private final static String ASSISTS = "goal_assist";
    private boolean valid = false;
    
    public WhoScoredPlayerStatsMap(String scriptVariable) {
        Matcher playerMatcher = playerPattern.matcher(scriptVariable);
        if(playerMatcher.matches()) {
            put(NAME, playerMatcher.group(1));            
            put(CURRENT_POSITION, playerMatcher.group(3));
            put(SUBSTITUTION_TIME, playerMatcher.group(4));
            put(PLAYABLE_POSITIONS, playerMatcher.group(5));                            
            
            String[] playerMatchStatistics = playerMatcher.group(2).replace("]],","]];").split(";");
            for(String playerMatchStatistic : playerMatchStatistics) {                                
                Matcher statsMatcher = playerMatchStatisticPattern.matcher(playerMatchStatistic);
                if(statsMatcher.matches()) {
                    put(statsMatcher.group(1), statsMatcher.group(2));
                }
            }            
            setValid(true);
        }
    }
    
    public String getName() {
        return get(NAME);
    }
    
    public int getParticipated() {
        String currentPosition = get(CURRENT_POSITION);
        return (currentPosition != null & currentPosition.equalsIgnoreCase("SUB") ? 1 : 2);
    }
    
    public int getRating() {
        String rating = get(RATING);
        if(rating != null) {
            BigDecimal bigDecimal = new BigDecimal(rating);
            bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP);            
            return bigDecimal.intValue();
        }
        return 0;
    }
    
    public int getAssists() {
        return getIntegerValue(ASSISTS);
    }
    
    public boolean isValid() {
        return valid;
    }
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private int getIntegerValue(String property) {
        try {
            String stringValue = get(property);
            if(stringValue != null) {
                return Integer.parseInt(stringValue);
            }
        } catch(NumberFormatException e) {}
        return 0;
    }
    
}
