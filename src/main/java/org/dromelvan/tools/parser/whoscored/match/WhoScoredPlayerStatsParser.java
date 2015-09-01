package org.dromelvan.tools.parser.whoscored.match;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.jsoup.JSoupDocumentParser;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;
import org.dromelvan.tools.parser.whoscored.WhoScoredProperties;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class WhoScoredPlayerStatsParser extends JSoupDocumentParser<MatchParserObject, WhoScoredPlayerStatsJavaScriptVariables> {

	private final static Logger logger = LoggerFactory.getLogger(WhoScoredPlayerStatsParser.class);

	@Inject
	public WhoScoredPlayerStatsParser(WhoScoredProperties whoScoredProperties) {
		super(whoScoredProperties);
	}

	@Override
	public Set<MatchParserObject> parse() {
		return parseJavaScript();
		// return parseRegExp();
	}

	public Set<MatchParserObject> parseJavaScript() {
		WhoScoredPlayerStatsJavaScriptVariables whoScoredPlayerStatsJavaScriptVariables = getJavaScriptVariables();

		MatchParserObject matchParserObject = whoScoredPlayerStatsJavaScriptVariables.getMatchParserObject();

		getParserProperties().map(matchParserObject);

		Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
		matchParserObjects.add(matchParserObject);
		return matchParserObjects;
	}

	public Set<MatchParserObject> parseRegExp() {
		WhoScoredMatchParserObject matchParserObject = new WhoScoredMatchParserObject();

		Elements scriptElements = getDocument().getElementsByTag("script");
		Pattern matchIdPattern = Pattern.compile(".*parameters: \\{ id: (\\d*) \\}.*", Pattern.DOTALL);
		Pattern scriptPattern = Pattern.compile("(.*)(var initialData) = \\[\\[(.*)\\], \\d*\\] ;(.*)", Pattern.DOTALL);
		Pattern fixturePattern = Pattern.compile("\\[(\\d*),(\\d*),'([\\w ]*)','([\\w ]*)','(.*)','.*',\\d*?,'(.*?)',.*", Pattern.DOTALL);
		Pattern teamPattern = Pattern.compile("[\\[ ]*\\d*,'([\\w ]*)',\\d.*\\]\\]\\]\\],\\[.*", Pattern.DOTALL);

		SortedSet<String> keySet = new TreeSet<String>();

		for (Element scriptElement : scriptElements) {
			for (DataNode node : scriptElement.dataNodes()) {
				Matcher matchIdMatcher = matchIdPattern.matcher(node.toString());
				if (matchIdMatcher.matches()) {
					matchParserObject.setWhoScoredId(Integer.parseInt(matchIdMatcher.group(1)));
				}

				Matcher scriptMatcher = scriptPattern.matcher(node.toString());
				if (scriptMatcher.matches()) {

					String[] scriptVariables = scriptMatcher.group(3).replace("]]]],[[", "]]]],[\\\n,[").split("\n,");

					TeamParserObject teamParserObject = null;

					for (String scriptVariable : scriptVariables) {
						scriptVariable = scriptVariable.trim();
						Matcher fixtureMatcher = fixturePattern.matcher(scriptVariable);
						if (fixtureMatcher.matches()) {
							matchParserObject.setHomeTeam(new WhoScoredTeamParserObject(fixtureMatcher.group(3), Integer.parseInt(fixtureMatcher.group(1))));
							matchParserObject.setAwayTeam(new WhoScoredTeamParserObject(fixtureMatcher.group(4), Integer.parseInt(fixtureMatcher.group(2))));

							matchParserObject.setDateTime(fixtureMatcher.group(5));
							matchParserObject.setTimeElapsed(fixtureMatcher.group(6));
							continue;
						} else {
							Matcher teamMatcher = teamPattern.matcher(scriptVariable);
							if (teamMatcher.matches()) {
								teamParserObject = (teamParserObject == null ? matchParserObject.getHomeTeam() : matchParserObject.getAwayTeam());
							}
							WhoScoredPlayerStatsMap map = new WhoScoredPlayerStatsMap(scriptVariable);
							if (map.isValid()) {
								WhoScoredPlayerParserObject playerParserObject = new WhoScoredPlayerParserObject(map);
								teamParserObject.getPlayers().add(playerParserObject);
							} else {
								logger.debug("Script variable {} did not produce a valid stats map.", scriptVariable);
							}
							keySet.addAll(map.keySet());
						}
					}
				}
			}
		}

		getParserProperties().map(matchParserObject);

		Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
		matchParserObjects.add(matchParserObject);
		return matchParserObjects;
	}

	public Set<MatchParserObject> parseRegExpOld() {
		WhoScoredMatchParserObject matchParserObject = new WhoScoredMatchParserObject();

		Elements scriptElements = getDocument().getElementsByTag("script");
		Pattern matchIdPattern = Pattern.compile(".*parameters: \\{ id: (\\d*) \\}.*", Pattern.DOTALL);
		Pattern scriptPattern = Pattern.compile("(.*)(var initialData) = \\[\\[(.*)\\], \\d*\\] ;(.*)", Pattern.DOTALL);
		Pattern fixturePattern = Pattern.compile("\\[(\\d*),(\\d*),'([\\w ]*)','([\\w ]*)','(.*)','.*',\\d*?,'(.*?)',.*", Pattern.DOTALL);
		Pattern teamPattern = Pattern.compile("[\\[ ]*\\d*,'([\\w ]*)',\\d.*\\]\\]\\]\\],\\[(\\[.*)", Pattern.DOTALL);

		SortedSet<String> keySet = new TreeSet<String>();

		for (Element scriptElement : scriptElements) {
			for (DataNode node : scriptElement.dataNodes()) {
				Matcher matchIdMatcher = matchIdPattern.matcher(node.toString());
				if (matchIdMatcher.matches()) {
					matchParserObject.setWhoScoredId(Integer.parseInt(matchIdMatcher.group(1)));
				}

				Matcher scriptMatcher = scriptPattern.matcher(node.toString());
				if (scriptMatcher.matches()) {

					String[] scriptVariables = scriptMatcher.group(3).split("\n,");

					TeamParserObject teamParserObject = null;

					for (String scriptVariable : scriptVariables) {
						scriptVariable = scriptVariable.trim();
						Matcher fixtureMatcher = fixturePattern.matcher(scriptVariable);
						if (fixtureMatcher.matches()) {
							matchParserObject.setHomeTeam(new WhoScoredTeamParserObject(fixtureMatcher.group(3), Integer.parseInt(fixtureMatcher.group(1))));
							matchParserObject.setAwayTeam(new WhoScoredTeamParserObject(fixtureMatcher.group(4), Integer.parseInt(fixtureMatcher.group(2))));

							matchParserObject.setDateTime(fixtureMatcher.group(5));
							matchParserObject.setTimeElapsed(fixtureMatcher.group(6));
							continue;
						} else {
							Matcher teamMatcher = teamPattern.matcher(scriptVariable);
							if (teamMatcher.matches()) {
								teamParserObject = (teamParserObject == null ? matchParserObject.getHomeTeam() : matchParserObject.getAwayTeam());
								scriptVariable = teamMatcher.group(2);
							}
							WhoScoredPlayerStatsMap map = new WhoScoredPlayerStatsMap(scriptVariable);
							if (map.isValid()) {
								WhoScoredPlayerParserObject playerParserObject = new WhoScoredPlayerParserObject(map);
								teamParserObject.getPlayers().add(playerParserObject);
							} else {
								logger.debug("Script variable {} did not produce a valid stats map.", scriptVariable);
							}
							keySet.addAll(map.keySet());
						}
					}
				}
			}
		}

		getParserProperties().map(matchParserObject);

		Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
		matchParserObjects.add(matchParserObject);
		return matchParserObjects;
	}
}
