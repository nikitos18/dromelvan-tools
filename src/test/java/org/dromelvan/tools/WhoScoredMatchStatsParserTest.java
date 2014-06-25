package org.dromelvan.tools;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.dromelvan.tools.parser.jsoup.JSoupFileReader;
import org.dromelvan.tools.parser.jsoup.JSoupURLReader;
import org.dromelvan.tools.parser.match.CardParserObject;
import org.dromelvan.tools.parser.match.GoalParserObject;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.dromelvan.tools.parser.match.PlayerParserObject;
import org.dromelvan.tools.parser.match.SubstitutionParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;
import org.dromelvan.tools.parser.whoscored.WhoScoredMatchEventsParser;
import org.dromelvan.tools.parser.whoscored.WhoScoredPlayerStatsParser;
import org.jsoup.nodes.Document;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JukitoRunner.class)
public class WhoScoredMatchStatsParserTest {

	private final static Logger logger = LoggerFactory.getLogger(WhoScoredTests.class);

	public static class Module extends JukitoModule {
		@Override
		protected void configureTest() {
			try {
				bindManyInstances(URL.class, new URL("http://www.whoscored.com/Matches/720016/LiveStatistics/England-Premier-League-2013-2014-Manchester-City-Everton"));
				bindManyInstances(File.class, new File("src/test/resources/Manchester City-Everton - Live Statistics.htm"));
			} catch (MalformedURLException e) {
			}
		}
	}

	// @Test
	public void parsePlayerStatsURLTest(@All URL url, WhoScoredPlayerStatsParser whoScoredPlayerStatsParser) throws MalformedURLException {
		JSoupURLReader jSoupURLReader = new JSoupURLReader(url);
		Document document = jSoupURLReader.read();

		whoScoredPlayerStatsParser.setDocument(document);

		Set<MatchParserObject> matchParserObjects = whoScoredPlayerStatsParser.parse();
		logMatches(matchParserObjects);
	}

	@Test
	public void parsePlayerStatsFileTest(@All File file, WhoScoredPlayerStatsParser whoScoredPlayerStatsParser, WhoScoredMatchEventsParser whoScoredMatchEventsParser) throws MalformedURLException {
		JSoupFileReader jSoupFileReader = new JSoupFileReader(file);
		Document document = jSoupFileReader.read();

		whoScoredPlayerStatsParser.setDocument(document);

		Set<MatchParserObject> matchParserObjects = whoScoredPlayerStatsParser.parse();

		file = new File(file.getParent(), file.getName().replace("- Live Statistics", "Live"));
		jSoupFileReader = new JSoupFileReader(file);
		document = jSoupFileReader.read();

		whoScoredMatchEventsParser.setDocument(document);
		whoScoredMatchEventsParser.parse();

		// logMatches(matchParserObjects);
	}

	private void logMatches(Set<MatchParserObject> matchParserObjects) {
		for (MatchParserObject matchParserObject : matchParserObjects) {
			TeamParserObject homeTeam = matchParserObject.getHomeTeam();
			TeamParserObject awayTeam = matchParserObject.getAwayTeam();

			logger.info("Statistics for {} vs {}:", homeTeam.getName(), awayTeam.getName());

			for (PlayerParserObject playerParserObject : homeTeam.getPlayers()) {
				logger.info("{} player: {}.", homeTeam, playerParserObject);
			}
			for (PlayerParserObject playerParserObject : awayTeam.getPlayers()) {
				logger.info("{} player: {}.", awayTeam, playerParserObject);
			}

			for (GoalParserObject goalParserObject : homeTeam.getGoals()) {
				logger.debug("{} goal: {}.", homeTeam, goalParserObject);
			}
			for (GoalParserObject goalParserObject : awayTeam.getGoals()) {
				logger.debug("{} goal: {}.", awayTeam, goalParserObject);
			}

			for (CardParserObject cardParserObject : homeTeam.getCards()) {
				logger.debug("{} card: {}.", homeTeam, cardParserObject);
			}
			for (CardParserObject cardParserObject : awayTeam.getCards()) {
				logger.debug("{} card: {}.", awayTeam, cardParserObject);
			}

			for (SubstitutionParserObject substitutionParserObject : homeTeam.getSubstitutions()) {
				logger.debug("{} substitution: {}.", homeTeam, substitutionParserObject);
			}
			for (SubstitutionParserObject substitutionParserObject : awayTeam.getSubstitutions()) {
				logger.debug("{} substitution: {}.", awayTeam, substitutionParserObject);
			}
		}
	}
}
