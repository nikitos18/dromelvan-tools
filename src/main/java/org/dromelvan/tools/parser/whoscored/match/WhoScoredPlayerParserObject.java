package org.dromelvan.tools.parser.whoscored.match;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.dromelvan.tools.parser.match.PlayerParserObject;

public class WhoScoredPlayerParserObject extends PlayerParserObject {

	public WhoScoredPlayerParserObject(Map player) {
		setWhoScoredId((int) player.get("playerId"));
		setName((String) player.get("name"));
		setParticipated((((String) player.get("position")).toUpperCase().equals("SUB") ? 1 : 2));

		Map<String, Object> stats = (Map<String, Object>) player.get("stats");
		if (stats != null) {
			List<Object> ratings = (List<Object>) stats.get("ratings");
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
