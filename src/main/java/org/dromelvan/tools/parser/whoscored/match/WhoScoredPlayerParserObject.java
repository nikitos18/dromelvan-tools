package org.dromelvan.tools.parser.whoscored.match;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.dromelvan.tools.parser.match.PlayerParserObject;

public class WhoScoredPlayerParserObject extends PlayerParserObject {

	public WhoScoredPlayerParserObject(Map player) {
		setWhoScoredId((int) player.get(WhoScoredMatchJavaScriptVariables.PLAYER_ID));
		setName((String) player.get(WhoScoredMatchJavaScriptVariables.PLAYER_NAME));
		setParticipated((((String) player.get(WhoScoredMatchJavaScriptVariables.PLAYER_POSITION)).toUpperCase().equals(WhoScoredMatchJavaScriptVariables.PLAYER_POSITION_SUBSTITUTE) ? 1 : 2));

		Map<String, Object> stats = (Map<String, Object>) player.get(WhoScoredMatchJavaScriptVariables.PLAYER_STATS);
		if (stats != null) {
			List<Object> ratings = (List<Object>) stats.get(WhoScoredMatchJavaScriptVariables.PLAYER_STATS_RATINGS);
			if (ratings != null) {
				Object ratingObject = ratings.get(ratings.size() - 1);
				if (ratingObject instanceof Double) {
					BigDecimal bigDecimal = BigDecimal.valueOf((Double) ratingObject);
					bigDecimal = bigDecimal.movePointRight(2);
					setRating(bigDecimal.intValue());
				} else if (ratingObject instanceof Integer) {
					setRating((int) ratingObject * 100);
				}
			}
		}
	}

	@Override
	public String toString() {
		return String.format("Name: %s (%s) Participated: %s Rating: %s Assists: %s", getName(), getWhoScoredId(), getParticipated(), getRating(), getAssists());
	}
}
