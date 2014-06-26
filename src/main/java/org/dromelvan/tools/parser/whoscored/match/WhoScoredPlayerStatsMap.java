package org.dromelvan.tools.parser.whoscored.match;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhoScoredPlayerStatsMap extends HashMap<String, String> {

	/**
     *
     */
	private static final long serialVersionUID = 1313287906428296801L;
	private final static Pattern playerPattern = Pattern.compile("\\[(\\d*),'(.*)',[\\d\\.]*,\\[\\[(.*)\\]\\],\\d*,'([\\w\\(\\)]*)',\\d*,\\d*,(\\d*),'([\\w\\(\\),]*)',.*\\]", Pattern.DOTALL);
	private final static Pattern playerMatchStatisticPattern = Pattern.compile("\\['(.*)',\\['?([^']*)'?\\]\\]");
	public final static String WHOSCORED_ID = "whoscored_id";
	public final static String NAME = "name";
	public final static String PLAYED_POSITION = "played_position";
	public final static String PLAYABLE_POSITIONS = "playable_positions";
	public final static String SUBSTITUTION_TIME = "substitution_time";
	public final static String RATING = "rating";
	public final static String GOALS = "goals";
	public final static String ASSISTS = "goal_assist";

	public final static String KEY_PASS = "total_att_assist";
	public final static String SHOT_BLOCKED = "outfielder_block";
	public final static String CLAIMED_CROSS = "good_high_claim";

	public final static String TOTAL_SHOTS = "total_scoring_att";
	public final static String SHOTS_ON_TARGET = "ontarget_scoring_att";

	public final static String TOUCHES = "touches";
	public final static String UNSUCCESSFUL_TOUCH = "unsuccessful_touch";
	public final static String SUCCESSFUL_TOUCH = "successful_touch";

	public final static String TOTAL_PASS = "total_pass";
	public final static String ACCURATE_PASS = "accurate_pass";
	public final static String ACCURATE_PASS_PERCENTAGE = "accurate_pass_percentage";

	public final static String TOTAL_DRIBBLE = "total_contest";
	public final static String WON_DRIBBLE = "won_contest";

	public final static String GOAL_SCORED_BY_TEAM = "goal_scored_by_team";
	private final static String GOAL_SCORED_BY_TEAM_ = "goal_scored_by_team_";

	public final static String GOALS_CONCEDED = "goals_conceded";
	private final static String GOALS_CONCEDED_ = "goals_conceded_";
	private final static String GOALS_CONCEDED_IBOX = "goals_conceded_ibox";
	public final static String GOALS_CONCEDED_TOTAL = "goals_conceded_total";
	private boolean valid = false;

	public WhoScoredPlayerStatsMap(String scriptVariable) {
		Matcher playerMatcher = playerPattern.matcher(scriptVariable);
		if (playerMatcher.matches()) {
		    put(WHOSCORED_ID, playerMatcher.group(1));
			put(NAME, playerMatcher.group(2));
			put(PLAYED_POSITION, playerMatcher.group(3));
			put(SUBSTITUTION_TIME, playerMatcher.group(4));
			put(PLAYABLE_POSITIONS, playerMatcher.group(5));

			String[] playerMatchStatistics = playerMatcher.group(3).replace("]],", "]];").split(";");
			for (String playerMatchStatistic : playerMatchStatistics) {
				Matcher statsMatcher = playerMatchStatisticPattern.matcher(playerMatchStatistic);
				if (statsMatcher.matches()) {
					String key = statsMatcher.group(1);
					String value = statsMatcher.group(2);
					put(key, value);

				}
			}

			int totalPass = getIntegerValue(TOTAL_PASS);
			int accuratePass = getIntegerValue(ACCURATE_PASS);
			if (totalPass > 0) {
				double accuratePassPercentage = ((double) accuratePass / (double) totalPass) * 1000;
				put(ACCURATE_PASS_PERCENTAGE, String.valueOf((int) accuratePassPercentage));
			}

			put(SUCCESSFUL_TOUCH, String.valueOf(getIntegerValue(TOUCHES) - getIntegerValue(UNSUCCESSFUL_TOUCH)));
			setValid(true);
		}
	}

	@Override
	public String put(String key, String value) {
		String returnValue = super.put(key, value);
		if (key.startsWith(GOALS_CONCEDED_)
				&& !key.equals(GOALS_CONCEDED_IBOX)) {
			int goalsConceded = getIntegerValue(GOALS_CONCEDED);
			goalsConceded += getIntegerValue(key);
			super.put(GOALS_CONCEDED, String.valueOf(goalsConceded));
		} else if (key.startsWith(GOAL_SCORED_BY_TEAM_)) {
			int goalScoredByTeam = getIntegerValue(GOAL_SCORED_BY_TEAM);
			goalScoredByTeam += getIntegerValue(key);
			super.put(GOAL_SCORED_BY_TEAM, String.valueOf(goalScoredByTeam));
		}
		return returnValue;
	}

    public int getWhoScoredId() {
        return Integer.parseInt(get(WHOSCORED_ID));
    }

	public String getName() {
		return get(NAME);
	}

	public int getParticipated() {
		String playedPosition = get(PLAYED_POSITION);
		return (playedPosition != null && playedPosition.equalsIgnoreCase("SUB") ? 1 : 2);
	}

	public int getRating() {
		String rating = get(RATING);
		if (rating != null) {
			BigDecimal bigDecimal = new BigDecimal(rating);
			bigDecimal = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP);
			return bigDecimal.intValue();
		}
		return 0;
	}

	public int getGoals() {
		return getIntegerValue(GOALS);
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

	public int getIntegerValue(String property) {
		try {
			String stringValue = get(property);
			if (stringValue != null) {
				return Integer.parseInt(stringValue);
			}
		} catch (NumberFormatException e) {
		}
		return 0;
	}

	@Override
	public String toString() {
		// SortedSet<String> keys = new TreeSet<String>(keySet());
		// StringBuilder stringBuilder = new StringBuilder();
		//
		// for (String key : keys) {
		// stringBuilder.append(key);
		// stringBuilder.append(" == ");
		// stringBuilder.append(get(key));
		// stringBuilder.append(" ");
		// }
		//
		// return stringBuilder.toString().trim();
		return "Stats";
	}
}
