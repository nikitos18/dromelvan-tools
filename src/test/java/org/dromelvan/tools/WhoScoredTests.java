package org.dromelvan.tools;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.dromelvan.tools.parser.CardParserObject;
import org.dromelvan.tools.parser.GoalParserObject;
import org.dromelvan.tools.parser.JSoupFileReader;
import org.dromelvan.tools.parser.JSoupURLReader;
import org.dromelvan.tools.parser.MatchParserObject;
import org.dromelvan.tools.parser.PlayerParserObject;
import org.dromelvan.tools.parser.SubstitutionParserObject;
import org.dromelvan.tools.parser.TeamParserObject;
import org.dromelvan.tools.parser.whoscored.WhoScoredMatchStatisticsParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jukito.All;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JukitoRunner.class)
public class WhoScoredTests {

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
	public void testURL(@All URL url) {
		JSoupURLReader jSoupURLReader = new JSoupURLReader(url);
		System.out.println(jSoupURLReader.read());
	}

	// @Test
	public void testFile(@All File file) {
		JSoupFileReader jSoupFileReader = new JSoupFileReader(file);
		System.out.println(jSoupFileReader.read());
	}

	// @Test
	public void downloadTest(@All URL url) throws IOException {
		Document doc = Jsoup.connect(url.toString())
				.data("query", "Java")
				.userAgent("Chrome")
				.timeout(3000)
				.get();
		System.out.println(doc);
	}

	// @Test
	public void fileTest(@All File whoScoredFile, WhoScoredMatchStatisticsParser parser) throws IOException {
		parser.setFile(whoScoredFile);
		Set<MatchParserObject> matchParserObjects = parser.parse();

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
