package org.dromelvan.tools;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.dromelvan.tools.parser.match.CardParserObject;
import org.dromelvan.tools.parser.match.GoalParserObject;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.dromelvan.tools.parser.match.PlayerParserObject;
import org.dromelvan.tools.parser.match.SubstitutionParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;
import org.dromelvan.tools.parser.whoscored.match.WhoScoredMatchParser;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JukitoRunner.class)
public class WhoScoredMatchParserTests {

	private final static Logger logger = LoggerFactory.getLogger(WhoScoredMatchParserTests.class);

	public static class Module extends JukitoModule {
		@Override
		protected void configureTest() {
			try {
				bindManyInstances(URL.class, new URL("http://www.whoscored.com/Matches/720889/Live/England-Premier-League-2013-2014-Tottenham-Aston-Villa"));
				bindManyInstances(File.class, new File("src/test/resources/Manchester City-Liverpool.html"));
			} catch (MalformedURLException e) {
			}
		}
	}

	@Test
	public void parsePlayerStatsFileTest(@All File file, WhoScoredMatchParser whoScoredMatchEventsParser) throws IOException {
		whoScoredMatchEventsParser.setFile(file);
		Set<MatchParserObject> matchParserObjects = whoScoredMatchEventsParser.parse();
		logMatches(matchParserObjects);
	}

	private void logMatches(Set<MatchParserObject> matchParserObjects) {
		for (MatchParserObject matchParserObject : matchParserObjects) {
			TeamParserObject homeTeam = matchParserObject.getHomeTeam();
			TeamParserObject awayTeam = matchParserObject.getAwayTeam();

			logger.info("Match {}", matchParserObject);
			logger.info("Statistics for {} vs {}:", homeTeam, awayTeam);

			for (PlayerParserObject playerParserObject : homeTeam.getPlayers()) {
				logger.info("{} player: {}.", homeTeam, playerParserObject);
			}
			for (PlayerParserObject playerParserObject : awayTeam.getPlayers()) {
				logger.info("{} player: {}.", awayTeam, playerParserObject);
			}

			for (GoalParserObject goalParserObject : homeTeam.getGoals()) {
				logger.info("{} goal: {}.", homeTeam, goalParserObject);
			}
			for (GoalParserObject goalParserObject : awayTeam.getGoals()) {
				logger.info("{} goal: {}.", awayTeam, goalParserObject);
			}

			for (CardParserObject cardParserObject : homeTeam.getCards()) {
				logger.info("{} card: {}.", homeTeam, cardParserObject);
			}
			for (CardParserObject cardParserObject : awayTeam.getCards()) {
				logger.info("{} card: {}.", awayTeam, cardParserObject);
			}

			for (SubstitutionParserObject substitutionParserObject : homeTeam.getSubstitutions()) {
				logger.info("{} substitution: {}.", homeTeam, substitutionParserObject);
			}
			for (SubstitutionParserObject substitutionParserObject : awayTeam.getSubstitutions()) {
				logger.info("{} substitution: {}.", awayTeam, substitutionParserObject);
			}
		}
	}
}
