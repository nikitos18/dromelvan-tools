package org.dromelvan.tools.parser.whoscored;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dromelvan.tools.parser.jsoup.JSoupDocumentParser;
import org.dromelvan.tools.parser.match.MatchParserObject;
import org.dromelvan.tools.parser.match.TeamParserObject;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

public class WhoScoredPlayerStatsParser extends JSoupDocumentParser<MatchParserObject> {

	private final static Logger logger = LoggerFactory.getLogger(WhoScoredPlayerStatsParser.class);

	@Inject
	public WhoScoredPlayerStatsParser(WhoScoredProperties whoScoredProperties) {
		super(whoScoredProperties);
	}

	@Override
	public Set<MatchParserObject> parse() {
		MatchParserObject matchParserObject = new MatchParserObject();

		Elements scriptElements = getDocument().getElementsByTag("script");
		Pattern scriptPattern = Pattern.compile("(.*)(var initialData) = \\[\\[(.*)\\], 0\\] ;(.*)", Pattern.DOTALL);
		Pattern fixturePattern = Pattern.compile("\\[\\d*,\\d*,'([\\w ]*)','([\\w ]*)',.*", Pattern.DOTALL);
		Pattern teamPattern = Pattern.compile("[\\[ ]*\\d*,'([\\w ]*)',\\d.*\\]\\]\\]\\],\\[(\\[.*)", Pattern.DOTALL);

		SortedSet<String> keySet = new TreeSet<String>();

		for (Element scriptElement : scriptElements) {
			for (DataNode node : scriptElement.dataNodes()) {
				Matcher scriptMatcher = scriptPattern.matcher(node.toString());
				if (scriptMatcher.matches()) {

					String[] scriptVariables = scriptMatcher.group(3).split("\n,");
					TeamParserObject teamParserObject = null;

					for (String scriptVariable : scriptVariables) {
						scriptVariable = scriptVariable.trim();
						Matcher fixtureMatcher = fixturePattern.matcher(scriptVariable);
						if (fixtureMatcher.matches()) {
							matchParserObject.setHomeTeam(new TeamParserObject(fixtureMatcher.group(1)));
							matchParserObject.setAwayTeam(new TeamParserObject(fixtureMatcher.group(2)));
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

		Set<MatchParserObject> matchParserObjects = new HashSet<MatchParserObject>();
		matchParserObjects.add(matchParserObject);
		return matchParserObjects;
	}
}
