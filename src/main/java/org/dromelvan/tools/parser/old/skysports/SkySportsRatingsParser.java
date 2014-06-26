package org.dromelvan.tools.parser.old.skysports;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dromelvan.tools.parser.match.MatchParserObject;
import org.dromelvan.tools.parser.match.PlayerParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;
import org.dromelvan.tools.parser.old.MatchStatisticsHTMLFileParser;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class SkySportsRatingsParser extends MatchStatisticsHTMLFileParser {

	private final static Logger logger = LoggerFactory.getLogger(SkySportsRatingsParser.class);

	@Inject
	public SkySportsRatingsParser(SkySportsProperties parserProperties) {
		super(parserProperties);
	}

	@Override
	protected Set<MatchParserObject> parseMatches() {
		MatchParserObject matchParserObject = new MatchParserObject();

		Elements teamHeaders = getDocument().select("th.team");
		String homeTeam = teamHeaders.first().text();
		String awayTeam = teamHeaders.last().text();

		matchParserObject.setHomeTeam(new TeamParserObject(homeTeam));
		matchParserObject.setAwayTeam(new TeamParserObject(awayTeam));

		Elements lineupTables = getDocument().select("table.tm-lineup");
		matchParserObject.getHomeTeam().setPlayers(parsePlayers(lineupTables.first()));
		matchParserObject.getAwayTeam().setPlayers(parsePlayers(lineupTables.last()));

		Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
		matchParserObjects.add(matchParserObject);
		return matchParserObjects;
	}

	private List<PlayerParserObject> parsePlayers(Element statTable) {
		List<PlayerParserObject> players = new ArrayList<PlayerParserObject>();
		for (Element tr : statTable.select("tr")) {
			Element td = tr.select("td.plyr > h5").first();
			if (td != null) {
				String playerName = td.ownText();
				if (!playerName.equalsIgnoreCase("averages")) {
					PlayerParserObject player = new PlayerParserObject(playerName);
					int rating = 0;
					try {
						rating = Integer.parseInt(tr.select("td.ss-rate").text());
					} catch (NumberFormatException e) {
					}
					if (rating <= 0) {
						logger.warn("Rating for row {} was {}.", tr.text(), rating);
					}
					player.setRating(rating);
					logger.debug("Parsed row {} to player {}.", tr, player);
					players.add(player);
				}
			}
		}
		return players;
	}

}
